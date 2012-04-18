package ac.analysis;

import java.io.IOException;

import game.BoardMatrix;
import game.ReversiRules;
import game.BoardMatrix.*;
import ac.AC;
import ac.analysis.structure.*;
import ac.memory.MemoryException;
import ac.shared.CompleteBoardState;
import ac.shared.FOLObjects.*;
import agent.Percept.*;

/**
 * This class represents the basic conceptual analyzer : It converts a board
 * matrix into a board state described in first order logic
 * 
 * @author Namrata Patel
 * 
 */
public class BasicAnalysisEngine
{
  /* **************************************************************************
   * FIELD
   * ************************************************************************* */

  /**
   * an instance of {@link Choices}
   */
  private Choices input;
  /**
   * an instance of {@link Choices_FOL}
   */
  private Choices_FOL output;

  /* **************************************************************************
   * CONSTRUCTOR
   * ************************************************************************* */

  /**
   * Default constructor for the basic conceptual analyzer
   * 
   * @param choices
   *          a list of available choices
   */
  public BasicAnalysisEngine(Choices choices)
  {
    super();
    this.setInput(choices);
    output = new Choices_FOL();
  }

  /* **************************************************************************
   * GETTERS & SETTERS
   * ************************************************************************* */

  /**
   * Gets the input (an instance of {@link Choices})
   * 
   * @return the {@code Choices} to be converted
   */
  public Choices getInput()
  {
    return input;
  }

  /**
   * Sets the input (an instance of {@link Choices})
   * 
   * @param choices
   * 
   */
  public void setInput(Choices choices)
  {
    this.input = choices;
  }

  /**
   * @return the output (an instance of {@link Choices_FOL})
   */
  public Choices_FOL getOutput()
  {
    return output;
  }

  /* **************************************************************************
   * METHODS
   * ************************************************************************* */

  /**
   * the method that runs the basic analyzer
   */
  public void runEngine()
  {
    BoardMatrix board = input.getCurrentBoard();
    CompleteBoardState current_board = convertMatrixtoCBS(board);

    output.setCurrent_board(current_board);

    CompleteBoardState result;
    Option tmp;
    for (agent.Action.Option o : input.getOptions())
      {
        result = convertMatrixtoCBS(o.getResult());
        tmp = new Option(o.getAction(), result);
        output.addOption(tmp);
      }
  }

  /**
   * @param matrix
   *          a BoardMatrix
   * @return an instance of a {@link CompleteBoardState} class which represents
   *         the converted BoardMatrix
   */
  private CompleteBoardState convertMatrixtoCBS(BoardMatrix matrix)
  {
    BoardMatrix.Position p = new Position(0, 0);
    Cell c;
    String s;
    Atom a;
    CompleteBoardState cbs = new CompleteBoardState();
    for (p.row = 0; p.row < matrix.getSize().n_rows; p.row++)
      for (p.col = 0; p.col < matrix.getSize().n_cols; p.col++)
        {
          c = matrix.getCell(p);
          s = "is_" + c + "('c_" + p.row + p.col + "')";
          a = new Atom(s);
          cbs.getBoardStateFacts().addNewFact(a);
        }

    return cbs;

  }
  
//just for tests
  /**
   * @param args
   * @throws IOException
   * @throws MemoryException 
   */
  public static void main(String[] args) throws IOException, MemoryException
  {
    BoardMatrix b = ReversiRules.getInstance().createBoard();
    Choices test = new Choices(b); 
    
    BasicAnalysisEngine bae = new BasicAnalysisEngine(test);
    bae.runEngine();
    
    System.out.println(bae.getOutput());
  }
}


