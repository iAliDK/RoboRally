package dk.dtu.compute.se.pisd.roborally.model;
import java.util.ArrayList;

public class GameWalls {
    Board board;
    public GameWalls(Board board){
        this.board = board;
        this.wall = new ArrayList<Walls>();
    }
   public Walls getWall(int index){
        return wall.get(index);
        //ændre så den henter en wall
    }

    private ArrayList<Walls> wall;

    public void addAWall(Walls walls) {
        wall.add(walls);
        Space space = board.getSpace(walls.getX(), walls.getY());
        space.setIsWall(true);
        //if (space != null) {
          //  space.setIsWall(true);
            //change space.isWall = true
        }

    public ArrayList<Walls> getWallArray() {
        return wall;
    }

}


