/*****************
 * @author william
 * @date 28-Mar-2012
 *****************/


package ac.frontier;

import game.BoardMatrix.Position;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class Frontier 
{ 
    /* CONSTANTS */
    private static final String s_url = "http://localhost:8084/GameService/ws";
    
  
    /* ATTRIBUTES */
   
    private Actuator actuator;
    private Sensor sensor;
    
    
    
    /* METHODS */
    
    // creation
    public Frontier()
    {
        try 
        {
            // attempt to create the external interface
            actuator = new Actuator(s_url);
            sensor = new Sensor(s_url);
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(Frontier.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    // query
    
    public void performMove(Position p)
    {
        try 
        {
            // to send information via the external interface
            actuator.performMove(p);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Frontier.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(Frontier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Option> getOptions()
    {
        try 
        {
            // to receive information via the external interface
            return sensor.getOptions();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Frontier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(Frontier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    

    

}
