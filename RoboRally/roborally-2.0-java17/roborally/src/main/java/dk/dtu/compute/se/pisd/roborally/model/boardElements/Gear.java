package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * @author Ismail
 * This class represents a gear on the board. It can be either clockwise or counter clockwise.
 */
public class Gear extends FieldAction {

    private Heading heading;

    /**
     * @author Ismail
     * This method returns the heading of the gear.
     * @return the heading of the gear.
     */

    public Heading getHeading() {
        return heading;
    }

    /**
     * @author Ismail
     * This method sets the heading of the gear.
     * @param heading the heading of the gear to be set to the gear.
     */

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    private final boolean isClockwise;

    /**
     * @author Ismail
     * This method is the constructor of the gear.
     * @param isClockwise whether the gear is clockwise or counter clockwise.
     */
    public Gear(boolean isClockwise) {

        this.isClockwise = isClockwise;

    }

    /**
     * @author Ismail
     * This method executes the action of the gear. It turns the player either clockwise or counter clockwise.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return true if the action was executed successfully, false otherwise
     */

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player currentPlayer = space.getPlayer();
        if (currentPlayer != null) {
            if(isClockwise){
                gameController.turnRight(currentPlayer);
                return true;
            }else{
                gameController.turnLeft(currentPlayer);
                return true;
            }
        }
        return false;
    }

    /**
     * @author Ismail
     * This method returns whether the gear is clockwise or counter clockwise.
     * @return true if the gear is clockwise, false otherwise.
     */

    public boolean isClockwise() {
        return isClockwise;
    }
}
