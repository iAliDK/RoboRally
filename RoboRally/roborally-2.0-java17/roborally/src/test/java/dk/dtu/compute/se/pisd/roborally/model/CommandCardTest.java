package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandCardTest {

    @Test
    @DisplayName("Should return the display name of the command")
    void getNameReturnsDisplayNameOfCommand() {
        Command command = Command.FORWARD;
        CommandCard commandCard = new CommandCard(command);

        String expectedName = command.displayName;
        String actualName = commandCard.getName();

        assertEquals(expectedName, actualName);
    }

}