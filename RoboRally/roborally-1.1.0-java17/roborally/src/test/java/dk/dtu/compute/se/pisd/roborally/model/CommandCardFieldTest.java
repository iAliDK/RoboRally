package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommandCardFieldTest {

    private Player player;

    @Test
    @DisplayName("Should set the visibility to false when the input is false")
    void setVisibleWhenInputIsFalse() {
        CommandCardField commandCardField = new CommandCardField(player);
        commandCardField.setVisible(false);
        assertEquals(false, commandCardField.isVisible());
    }

    @Test
    @DisplayName("Should notify change when the visibility is changed")
    void setVisibleShouldNotifyChangeWhenVisibilityChanged() {
        CommandCardField commandCardField;

        commandCardField = new CommandCardField(player);

        commandCardField.setVisible(false);

        assertEquals(false, commandCardField.isVisible());
        assertNull(commandCardField.getCard());
    }

    @Test
    @DisplayName("Should set the visibility to true when the input is true")
    void setVisibleWhenInputIsTrue() {
        CommandCardField commandCardField = new CommandCardField(player);
        commandCardField.setVisible(false);
        commandCardField.setVisible(true);
        assertEquals(true, commandCardField.isVisible());
    }

    @Test
    @DisplayName("Should not change the visibility when the input is the same as the current visibility")
    void setVisibleWhenInputIsSameAsCurrentVisibility() {
        CommandCardField commandCardField = new CommandCardField(player);
        boolean initialVisibility = commandCardField.isVisible();

        commandCardField.setVisible(initialVisibility);

        assertEquals(initialVisibility, commandCardField.isVisible());
    }

    @Test
    @DisplayName("Should return null when the card is not set")
    void getCardWhenCardIsNotSet() {
        CommandCardField commandCardField = new CommandCardField(player);

        CommandCard card = commandCardField.getCard();

        assertNull(card);
    }

    @Test
    @DisplayName("Should return the card when it is set")
    void getCardWhenCardIsSet() {
        CommandCardField commandCardField = new CommandCardField(player);
        CommandCard card = new CommandCard(Command.FORWARD);

        commandCardField.setCard(card);

        assertEquals(card, commandCardField.getCard());
    }
}