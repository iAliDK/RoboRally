package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.*;

/**
 * This class is a template for the CommandCardField class.
 * It is used to save the CommandCardField to a file.
 * @author Qiao.
 */
@SuppressWarnings("CanBeFinal")
public class CommandCardFieldTemplate {


    public CommandCard card;
    public boolean visible;

    public CommandCardFieldTemplate(boolean visible, CommandCard card){

        this.visible = visible;
        this.card = card;
    }
    
}
