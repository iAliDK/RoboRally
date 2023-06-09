package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Wall extends FieldAction{

    private Heading heading;

    public Wall(Heading heading){
        this.heading = heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }


    public Heading getHeading() {
        return heading;
    }
}
