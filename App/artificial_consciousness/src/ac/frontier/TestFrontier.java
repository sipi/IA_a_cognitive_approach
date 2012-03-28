/*****************
 * @author william
 * @date 28-Mar-2012
 *****************/


package ac.frontier;

import ac.frontier.morpion.MorpionFrontier;
import game.Game.Player;
import java.util.List;


public class TestFrontier 
{
    public static void main(String[] args)
    {
        Frontier frontier = new MorpionFrontier();
        
        
        List<Option> l = frontier.getOptions(Player.WHITE);
        
        for(Option o : l)
            System.out.println(o);  
        
    }
}