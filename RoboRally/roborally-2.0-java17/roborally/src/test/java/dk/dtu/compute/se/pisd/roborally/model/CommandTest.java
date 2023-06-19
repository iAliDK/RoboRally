package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {


    @Test
    @DisplayName("Should return an empty list when the command has no options")
    void getOptionsWhenCommandHasNoOptions() {
        Command command = Command.FORWARD;
        assertTrue(command.getOptions().isEmpty());
    }

    @Test
    @DisplayName("Should return a list of options when the command has options")
    void getOptionsWhenCommandHasOptions() {
        Command command = Command.OPTION_LEFT_RIGHT;
        List<Command> options = command.getOptions();

        assertNotNull(options);
        assertFalse(options.isEmpty());
        assertEquals(2, options.size());
        assertTrue(options.contains(Command.LEFT));
        assertTrue(options.contains(Command.RIGHT));
    }

    @Test
    @DisplayName("Should return false when the command has no options")
    void isInteractiveWhenCommandHasNoOptions() {
        Command command = Command.FORWARD;
        assertFalse(command.isInteractive());
    }

    @Test
    @DisplayName("Should return true when the command has options")
    void isInteractiveWhenCommandHasOptions() {
        Command command = Command.OPTION_LEFT_RIGHT;
        assertTrue(command.isInteractive());
    }

    /**
     * Method under test: {@link Command#isInteractive()}
     */
    @Test
    void testIsInteractive() {
        assertFalse(Command.FORWARD.isInteractive());
        assertTrue(Command.OPTION_LEFT_RIGHT.isInteractive());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Command#valueOf(String)}
     *   <li>{@link Command#getOptions()}
     * </ul>
     */
    @Test
    void testValueOf() {
        Command actualValueOfResult = Command.valueOf("FORWARD");
        assertTrue(actualValueOfResult.getOptions().isEmpty());
        assertEquals("Fwd", actualValueOfResult.displayName);
        assertFalse(actualValueOfResult.isInteractive());
    }
}

