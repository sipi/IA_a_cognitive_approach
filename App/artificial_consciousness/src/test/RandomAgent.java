/*****************
 * @author william
 * @date 29-Mar-2012
 *****************/


package test;

import frontier.Action;
import frontier.Percept.Choices;
import frontier.Percept.Defeat;
import frontier.Percept.Draw;
import frontier.Percept.Victory;
import main.Agent;


public class RandomAgent extends Agent
{

    @Override
    protected void think() 
    {
        // do nothing
    }

    @Override
    protected Action choices_reaction(Choices percept) 
    {
        // choose random action from amongst options
        int rand_i = (int)(Math.random()*percept.getOptions().size());
        return percept.getOptions().get(rand_i).getAction();
    }

    @Override
    protected Action victory_reaction(Victory percept) 
    {
        // restart the game
        return new Action.Restart();
    }

    @Override
    protected Action defeat_reaction(Defeat percept) 
    {
        // restart the game
        return new Action.Restart();
    }

    @Override
    protected Action draw_reaction(Draw percept) 
    {
        // restart the game
        return new Action.Restart();
    }
    
    @Override
    protected void action_failed(Action action) 
    {
        // no fault tolerance implemented for this Agent
        state = State.ERROR;
    }

}