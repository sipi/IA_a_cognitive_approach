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
public class AttributeNode extends AbstractNode<AttributeNode, ObjectNode>
{
  /**
   * @param attributeNode
   */
  AttributeNode(Node attributeNode)
  {
    super(attributeNode);
    ID = "id_attr";
  }

  @Override
  protected IterableWrapper<ObjectNode, Path> createObjectsFromPath(
      Traverser iterableToWrap)
  {
    return new IterableWrapper<ObjectNode, Path>(iterableToWrap)
    {
      @Override
      protected ObjectNode underlyingObjectToObject(Path path)
      {
        return new ObjectNode(path.endNode());
      }
    };
  }

  @Override
  protected Relationship getRelationshipTo(ObjectNode object)
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
