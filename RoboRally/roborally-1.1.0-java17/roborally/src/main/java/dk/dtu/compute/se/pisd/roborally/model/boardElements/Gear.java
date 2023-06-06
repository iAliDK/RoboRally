package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

public class Gear extends FieldAction {
    private boolean clockwise;

    public Gear(@NotNull boolean clockwise) {
        this.clockwise = clockwise;
    }

    @Override
    public boolean doAction(GameController gameController, Player player) {
        if (clockwise) {
            gameController.turnRight(player);
        } else {
            gameController.turnLeft(player);
        }
        return false;
    }

    public boolean isClockwise() {
        return clockwise;
    }
}
