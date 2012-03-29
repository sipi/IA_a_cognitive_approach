/*****************
 * @author wdyce
 * @date   Mar 27, 2012
 *****************/

package minimax;

import app.Agent;
import frontier.Action;
import frontier.Percept;
import game.BoardMatrix;
import game.BoardMatrix.Position;
import game.Game.Player;
import game.Rules;

public class MiniMax extends Agent
{
    /* CONSTANTS */
    /*private static final Player PMIN = Player.BLACK;
    private static final Player PMAX = Player.WHITE;
    * 
    */

    /* IMPLEMENTATIONS */

    /*public Position chooseMove(Rules rules, BoardMatrix board, Player player)
    {
        List<BoardMatrix> children = rules.getChildBoards(board, player);

        return null;
    }*/

    
    /* OVERRIDES */

    @Override
    protected void think() 
    {
        // minimax doesn't think
    }

    @Override
    protected Action choose_reaction(Percept percept) 
    {
        return new Action(new Position(0, 0));
    }

    @Override
    protected void receive_feedback(boolean success) 
    {
        System.out.println("did action succeed? " + success);
        // TODO
    }
    
    
    
    /* SUBROUTINES */

    private int evaluate(Rules rules, BoardMatrix board, Player player)
    {
        return 0;
        /*List<Position> options = rules.getLegalMoves(board, player);

        // Is leaf node ?
        if(options.isEmpty())
            return (rules.hasWon(board, PMAX)) ? 1 
                    : ((rules.hasWon(board, PMIN)) ? -1 : 0);


        // Is min node ?
        if(player == PMIN)
        {
            int beta = Integer.MAX_VALUE;
            for(Position option : options)
            {
                
            }
        }
        else // player == PMAX
        {
            int alpha = Integer.MIN_VALUE;
        }*/

    }
}