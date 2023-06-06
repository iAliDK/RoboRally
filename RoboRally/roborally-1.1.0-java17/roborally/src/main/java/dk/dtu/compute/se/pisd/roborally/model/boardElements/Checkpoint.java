package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class Checkpoint extends FieldAction {
    private final int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }


    @Override
    public boolean doAction(GameController gameController, Player player) {
        if (player != null) {
            if (checkpointNumber == player.getLastCheckpointVisited() + 1) {
                player.setLastCheckpointVisited(checkpointNumber);
                gameController.findWinner(player);
            }
        }
        return false;
    }
}
