package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class ExtraHandCard extends Upgrade {
    public ExtraHandCard() {
        this.upgradeResponsibility = UpgradeResponsibility.EXTRAHANDCARD;
    }


    @Override
    public void doAction(Player player, GameController gameController) {
        if (player.getNumberOfCards() == 8) {
            player.setExtraHandCard();
        }
    }
}
