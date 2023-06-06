package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.UpgradeResponsibility;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PushPanel extends FieldAction {
    public final Heading pushingDirection;
    private final List<Integer> activatingTurns;

    public PushPanel(Heading pushingDirection, Integer... activatingTurns) {
        this.pushingDirection = pushingDirection;
        this.activatingTurns = Arrays.asList(activatingTurns);
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, Player player) {
        if (!activatingTurns.isEmpty() && player != null && player.getSpace() != null) {
            if (activatingTurns.contains(gameController.board.getStep())) {
                for (Upgrade upgrade : player.getUpgrades()) {
                    if (upgrade.responsible(UpgradeResponsibility.PUSHPANELLODGER)) {
                        return false;
                    }
                }
                gameController.directionMove(player, pushingDirection);
            }
        }
        return false;
    }
}
