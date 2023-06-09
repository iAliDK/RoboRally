package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;


public class Gear extends FieldAction {

    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    private boolean isClockwise;


    public Gear(boolean isClockwise) {

        this.isClockwise = isClockwise;

    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Heading heading = getHeading();
        Player currentPlayer = space.getPlayer();
        if (currentPlayer != null) {
            if(isClockwise){
                gameController.turnRight(currentPlayer);
            }else{
                gameController.turnLeft(currentPlayer);
            }
            return false;
        }
        return false;
    }

    public boolean isClockwise() {
        return isClockwise;
    }
    /*
    public boolean isClockwise() {
        return clockwise;
    }
    public boolean isCounterClockwise() {
        return clockwise;
    }

     */
}
