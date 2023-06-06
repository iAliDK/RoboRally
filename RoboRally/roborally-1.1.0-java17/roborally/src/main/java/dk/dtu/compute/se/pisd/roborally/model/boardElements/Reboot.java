package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

public class Reboot {
    public final Heading REBOOT_HEADING;
    public final int REBOOT_NUMBER;
    private final boolean startField;

    public Reboot(Heading REBOOT_HEADING, boolean startField, int number) {
        this.REBOOT_NUMBER = number;
        this.REBOOT_HEADING = REBOOT_HEADING;
        this.startField = startField;
    }

    public boolean isStartField() {
        return startField;
    }
}
