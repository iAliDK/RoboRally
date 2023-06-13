package dk.dtu.compute.se.pisd.roborally.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommandTest {
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

