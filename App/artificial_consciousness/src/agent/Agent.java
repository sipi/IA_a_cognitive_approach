/*****************
 * @author william
 * @date 29-Mar-2012
 *****************/


package agent;

import game.Game;
import game.Rules;


public abstract class Agent 
{
    /* NESTING */
    
    public static enum State
    {
        NORMAL,
        ERROR
        // ... etc
    }
    
    
    /* ATTRIBUTES */
    
    private Frontier frontier;
    private int sleep_time;
    protected State state;
    
    
    /* INTERFACE */
    
    /**
     * Called whenever the Agent tries to act but has no Percept to act upon:
     * this method can be implemented to perform some kind of optimisation or
     * compression while waiting for the enemy to make a move.
     */
    protected abstract void think();
    
    /**
     * Checks which Action the Agent would perform when faced with the specified
     * Percept: generally used for choosing reactions to stimuli.
     * @param percept
     * Percept to react to.
     * @return 
     * An Action to be performed based on the Percept and whatever internal 
     * structure the Agent might have.
     */
    protected abstract Action perceptReaction(Percept percept);
    
    /**
     * Results of Actions cannot be reported synchronously, due to the RESTful
     * communication with the environment: this method is called when the result
     * of an Action is known.
     * @param success
     * Whether or not the given Action was successful.
     * @param action
     * The Action the success or failure of which being reported.
     */
    protected abstract void actionResult(boolean success, Action action);
    
    
    
    /* METHODS */
    
    // creation
    /**
     * Basic constructor: sets state to State.NORMAL and sleep_time to 0.
     */
    protected Agent()
    {
        frontier = new Frontier();
        sleep_time = 0;
        state = State.NORMAL;
    }
    
    // modification
    
    /**
     * Order the Agent to skip a specified number of updates.
     * @param n_steps 
     * The number of steps to be skipped.
     */
    public void sleep(int n_steps)
    {
        sleep_time = n_steps;
    }
    
    /**
     * Asks the frontier for a Percept and either reacts on in accordingly or
     * thinks if there is no new Percept to be found. 
     */
    public void act()
    {
        if(sleep_time > 0)
        {
            sleep_time--;
            return;
        }
        
        // check environment for new information
        Percept percept = frontier.newPercept();
        
        // if there's nothing new in sight, reflect on what we already know
        if(percept == null)
            think();
        // otherwise react to this new environment state
        else
            react(percept);
       
    }
    
    // query
    
    /**
     * Check the State of the Agent.
     * @return 
     * The Agent's current State.
     */
    public State getState()
    {
        return state;
    }
    
    
    /* SUBROUTINES */
    
    // query
    
    /**
     * Check which Player this Agent's frontier is playing as.
     * @return 
     * Returns either Game.Player.WHITE or Game.Player.BLACK, depending on which
     * Player the Agent is currently controlling.
     */
    protected Game.Player getPlayer()
    {
        return frontier.getPlayer();
    }
    
    /**
     * Check which rules the Agent's frontier is using to judge the validity of 
     * it's moves and to generate potential ones.
     * @return 
     * The Rules object being used by the Agent.
     */
    protected Rules getRules()
    {
        return frontier.getRules();
    }
    
    /**
     * Chooses an action based on the given percept and passes it to the
     * frontier to be executed: the success or failure of the action
     * is communicated *asynchronously* using the 'actionResult' method.
     * @param percept 
     * The Percept to react to.
     */
    private void react(Percept percept)
    {
        // choose a reaction to the stimulus
        Action action = perceptReaction(percept);
        // the agent receives feedback based on the success of their action
        actionResult(frontier.tryAction(action), action);
    }
    
}
