package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GearTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Gear#Gear(boolean)}
     *   <li>{@link Gear#setHeading(Heading)}
     *   <li>{@link Gear#getHeading()}
     *   <li>{@link Gear#isClockwise()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Gear actualGear = new Gear(true);
        actualGear.setHeading(Heading.SOUTH);
        assertEquals(Heading.SOUTH, actualGear.getHeading());
        assertTrue(actualGear.isClockwise());
    }

    /**
     * Method under test: {@link Gear#doAction(GameController, Space)}
     */
    @Test
    void testDoAction() {
        Gear gear = new Gear(true);
        GameController gameController = new GameController(new Board(8, 8));
        gear.doAction(gameController, new Space(new Board(8, 8), 2, 3));
    }
}

