package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class Checkpoint extends FieldAction {


    int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (checkpointNumber == player.getPlayerCounter()) {
            player.playerCounter++;
            if (player.playerCounter == 5) {
                player.setGameWon(true);
            }
            return true;
        }
        return false;
    }

    public int getCheckpointNumber(){
         return checkpointNumber;
    }
    }
