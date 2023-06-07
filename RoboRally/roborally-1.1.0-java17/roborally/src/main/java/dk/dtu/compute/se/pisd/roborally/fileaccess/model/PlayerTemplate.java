package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.*;

import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {

   
    public  int x;
    public  int y;
    public Heading heading;
    
    private int gameId = (int)(Math.random()*1001);
    private String name;
    public String color;
    public List<CommandCardFieldTemplate> cards;
    public List<CommandCardFieldTemplate> program;

    
    public PlayerTemplate(int x, int y, Heading heading, int gameId, String name, String color, List<CommandCardFieldTemplate> cards, List<CommandCardFieldTemplate> program){
        
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.gameId = gameId;
        this.name = name;
        this.color = color;
        this.cards = cards;
        this.program = program;
    }
    
}
