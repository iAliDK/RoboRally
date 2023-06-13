package dk.dtu.compute.se.pisd.roborally.model;

import java.sql.Array;
import java.util.ArrayList;

/**
 * This class is used to represent a wall in the game.
 * @author Daniel.
 */
public class Walls {
    private ArrayList<GameWalls> wall;
    private int x;
    private int y;

    /**
     * Constructor for a wall.
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     */
    public Walls(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Adds a wall to the game.
     * @param gameWalls the wall to be added
     */
    public void addAWall(GameWalls gameWalls) {
        wall.add(gameWalls);

    }

    /**
     * Gets the y-coordinate of the wall.
     * @return the x-coordinate of the wall
     */
    public int getY(){
        return y;
    }

    /**
     * Gets the x-coordinate of the wall.
     * @return the x-coordinate of the wall
     */
    public int getX(){
        return x;
    }

    //public ArrayList<GameWalls> getWall() {
    //    return wall;
    //}
}
