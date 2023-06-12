package dk.dtu.compute.se.pisd.roborally.model;
import java.util.ArrayList;

/**
 * This class is used to represent a wall in the game.
 * @author Daniel.
 */
public class GameWalls {
    Board board;
    public GameWalls(Board board){
        this.board = board;
        this.wall = new ArrayList<Walls>();
    }

    /**
     * Adds a wall to the game.
     * @param index the index of the wall
     * @return the wall
     */
    public Walls getWall(int index){
        return wall.get(index);
        //ændre så den henter en wall
    }

    private ArrayList<Walls> wall;

    /**
     * Adds a wall to the game.
     * @param walls the wall to be added
     */
    //DO not use this
    public void addAWall(Walls walls) {
        wall.add(walls);
        Space space = board.getSpace(walls.getX(), walls.getY());

        //if (space != null) {
          //  space.setIsWall(true);
            //change space.isWall = true
        }

    public ArrayList<Walls> getWallArray() {
        return wall;
    }

}


