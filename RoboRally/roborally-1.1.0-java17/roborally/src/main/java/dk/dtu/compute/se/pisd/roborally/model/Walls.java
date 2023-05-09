package dk.dtu.compute.se.pisd.roborally.model;

import java.sql.Array;
import java.util.ArrayList;

/**
 * @author Daniel.
 */
public class Walls {
    private ArrayList<GameWalls> wall;
    private int x;
    private int y;


    public Walls(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void addAWall(GameWalls gameWalls) {
        wall.add(gameWalls);

    }

    public int getY(){
        return y;
    }

    public int getX(){
        return x;
    }

    //public ArrayList<GameWalls> getWall() {
    //    return wall;
    //}
}
