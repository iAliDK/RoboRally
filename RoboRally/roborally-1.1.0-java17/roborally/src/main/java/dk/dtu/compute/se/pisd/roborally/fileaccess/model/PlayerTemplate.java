package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {

   
    public  int x;
    public  int y;
    public Heading heading;
    
    private int gameId;
    private String name;
    public String color;
    
    public PlayerTemplate(int x, int y, Heading heading, int gameId, String name, String color){
        
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.gameId = gameId;
        this.name = name;
        this.color = color;
        
    }
    
}
