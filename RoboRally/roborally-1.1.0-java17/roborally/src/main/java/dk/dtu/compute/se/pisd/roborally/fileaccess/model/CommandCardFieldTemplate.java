package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.*;

public class CommandCardFieldTemplate {


    public CommandCard card;
    private boolean visible;

    public CommandCardFieldTemplate(boolean visible, CommandCard card){

        this.visible = visible;
        this.card = card;
    }
    
}
