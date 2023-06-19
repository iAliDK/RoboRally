package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import static dk.dtu.compute.se.pisd.roborally.controller.AppController.gameWon;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Player;


/**
 * This class represents a checkpoint on the board.
 */
public class Checkpoint extends FieldAction {



    int checkpointNumber;

    /**
     * Constructor for Checkpoint
     *
     * @param checkpointNumber the number of the checkpoint
     */

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    /**
     * Executes the field action for a given space. In order to be able to do so, the player on the space must be set
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return true if the action was executed successfully, false otherwise
     */


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (checkpointNumber == player.getPlayerCounter()) {
            player.playerCounter++;
            if (player.playerCounter == 5) {
                gameWon(player);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the number of the checkpoint
     *
     * @return the number of the checkpoint
     */
    public int getCheckpointNumber() {
        return checkpointNumber;
    }
}
