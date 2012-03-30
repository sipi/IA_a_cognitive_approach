/**
 * 
 */
package ac.memory.persistance;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.collection.IterableWrapper;

/**
 * @author Thibaut Marmin <marminthibaut@gmail.com>
 * @date 30 mars 2012
 * @version 0.1
 */
public class ObjectNode extends AbstractNode<ObjectNode, AttributeNode>
{

  /**
   * @param attributeNode
   */
  ObjectNode(Node objectNode)
  {
    super(objectNode);
    ID = "id_obj";
  }

  @Override
  protected IterableWrapper<AttributeNode, Path> createObjectsFromPath(
      Traverser iterableToWrap)
  {
    return new IterableWrapper<AttributeNode, Path>(iterableToWrap)
    {
      @Override
      protected AttributeNode underlyingObjectToObject(Path path)
      {
        return new AttributeNode(path.endNode());
      }
    };
  }

  @Override
  protected Relationship getRelationshipTo(AttributeNode object)
  {
    Node node = object.getUnderlyingNode();

    for (Relationship rel : underlyingNode.getRelationships(RelTypes.RELATED))
      {
        if (rel.getOtherNode(underlyingNode).equals(node))
          {
            return rel;
          }
      }
    return null;
  }

}
