package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class EnergySpace extends FieldAction {
    private boolean energyAvailable = true;

    @Override
    public boolean doAction(GameController gameController, Player player) {
        if (player != null) {
            if (energyAvailable && gameController.board.getStep() == 4) {
                gameController.givePlayerRandomUpgrade(player, this);
                gameController.givePlayerRandomUpgrade(player, this);
            } else if (energyAvailable || gameController.board.getStep() == 4) {
                gameController.givePlayerRandomUpgrade(player, this);
            }
        }
        return false;
    }

    public void setEnergyAvailable(boolean energyAvailable) {
        this.energyAvailable = energyAvailable;
    }

}
