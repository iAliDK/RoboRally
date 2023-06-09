package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoint extends FieldAction{



    int checkpointNumber;
    public Checkpoint(int checkpointNumber)  {
      this.checkpointNumber = checkpointNumber;
    }
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }

    public int getCheckpointNumber(){
         return checkpointNumber;
    }
}
