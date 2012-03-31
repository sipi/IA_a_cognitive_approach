package ac.memory.persistance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.collection.IterableWrapper;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

/**
 * Wrapping class for node
 * 
 * @author Thibaut Marmin <marminthibaut@gmail.com>
 * @date 30 mars 2012
 * @version 0.1
 * @param <ObjectType>
 *          Object type to store
 * @param <RelatedObjectType>
 *          Related object type
 */
abstract public class AbstractNode<ObjectType, RelatedObjectType>
{
  private static final Logger logger = Logger.getLogger(AbstractNode.class);

  private String ID_FIELD;
  static String OBJECT = "object";

  // START SNIPPET: the-node
  protected final Node underlyingNode;

  protected AbstractNode(Node node, String id_field)
  {
    this.underlyingNode = node;
    this.ID_FIELD = id_field;
  }

  protected Node getUnderlyingNode()
  {
    return underlyingNode;
  }

  /**
   * @return the id of the node
   */
  public Long getId()
  {
    return (Long) underlyingNode.getProperty(ID_FIELD);
  }

  /**
   * Return the RelevantPartialBoardState unserialized
   * 
   * @return the object of the node
   * @throws IOException
   *           Should never happen
   * @throws ClassNotFoundException
   *           Error when unserialize
   */
  @SuppressWarnings("unchecked")
  public ObjectType getObject() throws IOException, ClassNotFoundException
  {
    logger.debug("Getting the object");
    logger.debug("Un-serialize field");
    // / UNSERIALIZE
    ByteArrayInputStream bis = new ByteArrayInputStream(
        (byte[]) underlyingNode.getProperty(OBJECT));
    ObjectInput in = new ObjectInputStream(bis);
    return (ObjectType) in.readObject();
  }

  /**
   * @return Node related to this Node
   */
  public Iterable<RelatedObjectType> getRelatedObjects()
  {
    logger.debug("Getting the related objects");
    @SuppressWarnings("deprecation")
    TraversalDescription travDesc = Traversal.description().breadthFirst()
        .relationships(RelTypes.RELATED).uniqueness(Uniqueness.NODE_GLOBAL)
        .depthFirst().filter(Traversal.returnAll());

    return createObjectsFromPath(travDesc.traverse(underlyingNode));
  }

  /**
   * Associate new node to this node
   * 
   * @param object
   *          the object to associate
   */
  @SuppressWarnings("unchecked")
  public void addRelatedObject(RelatedObjectType object)
  {
    logger.debug("Relate new object to the node");
    logger.debug("Opening transaction");
    Transaction tx = underlyingNode.getGraphDatabase().beginTx();
    try
      {
        if (!this.equals(object))
          {
            Relationship related = getRelationshipTo(object);
            if (related == null)
              {
                underlyingNode.createRelationshipTo(
                    ((AbstractNode<ObjectType, RelatedObjectType>) object)
                        .getUnderlyingNode(), RelTypes.RELATED);
              }
            tx.success();
          }
      }
    finally
      {
        logger.debug("Transaction finished");
        tx.finish();
      }
  }

  /**
   * Remove a relationship between the tow objects
   * 
   * @param object
   *          the object
   */
  public void removeRelatedObject(RelatedObjectType object)
  {
    logger.debug("Removing relation between tow nodes");
    logger.debug("Opening transaction");
    Transaction tx = underlyingNode.getGraphDatabase().beginTx();
    try
      {
        if (!this.equals(object))
          {
            Relationship related = getRelationshipTo(object);
            if (related != null)
              {
                related.delete();
              }
            tx.success();
          }
      }
    finally
      {
        tx.finish();
        logger.debug("Transaction finished");
      }
  }

  @Override
  public int hashCode()
  {
    return underlyingNode.hashCode();
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o)
  {
    return underlyingNode
        .equals(((AbstractNode<ObjectType, RelatedObjectType>) o)
            .getUnderlyingNode());
  }

  @Override
  public String toString()
  {
    return "Node[" + getId() + "]";
  }

  protected abstract IterableWrapper<RelatedObjectType, Path> createObjectsFromPath(
      Traverser iterableToWrap);

  protected abstract Relationship getRelationshipTo(RelatedObjectType object);

}