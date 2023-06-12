package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * This class implements a wall on board. It has a heading.
 */
public class Wall extends FieldAction{

    private Heading heading;

    /**
     * Constructor for Wall
     * @param heading the heading of the wall
     */
    public Wall(Heading heading){
        this.heading = heading;
    }

    /**
     * Returns the heading of the wall as a string.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return the heading of the wall as a string.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }

    /**
     * Returns the heading of the wall.
     * @return the heading of the wall.
     */
    public Heading getHeading() {
        return heading;
    }
}
