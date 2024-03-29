package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.*;

import java.util.List;


/**
 *
 * @author Qiao
 *
 */
@SuppressWarnings("ALL")
public class PlayerTemplate {

   
    public  int x;
    public  int y;
    public Heading heading;

    private int gameId;
    public String name;
    public String color;
    public List<CommandCardFieldTemplate> cards;
    public List<CommandCardFieldTemplate> program;

    public int playerCounter;
    
    public PlayerTemplate(int x, int y, Heading heading, int playerCounter, int gameId, String name, String color, List<CommandCardFieldTemplate> cards, List<CommandCardFieldTemplate> program){
        
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.playerCounter = playerCounter;
        this.gameId = gameId;
        this.name = name;
        this.color = color;
        this.cards = cards;
        this.program = program;
    }
    
}
