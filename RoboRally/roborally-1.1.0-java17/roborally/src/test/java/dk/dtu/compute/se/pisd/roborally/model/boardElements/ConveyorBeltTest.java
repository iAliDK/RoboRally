package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ConveyorBeltTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ConveyorBelt#ConveyorBelt(int, Heading)}
     *   <li>{@link ConveyorBelt#setHeading(Heading)}
     *   <li>{@link ConveyorBelt#getHeading()}
     *   <li>{@link ConveyorBelt#getSpeed()}
     * </ul>
     */
    @Test
    void testConstructor() {
        ConveyorBelt actualConveyorBelt = new ConveyorBelt(1, Heading.SOUTH);
        actualConveyorBelt.setHeading(Heading.SOUTH);
        assertEquals(Heading.SOUTH, actualConveyorBelt.getHeading());
        assertEquals(1, actualConveyorBelt.getSpeed());
    }

    /**
     * Method under test: {@link ConveyorBelt#doAction(GameController, Space)}
     */
    @Test
    //@Disabled("TODO: Complete this test")
    void testDoAction() {
        // TODO: Complete this test.
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 1
        //       at dk.dtu.compute.se.pisd.roborally.model.Board.<init>(Board.java:81)
        //       at dk.dtu.compute.se.pisd.roborally.model.Board.<init>(Board.java:134)

        ConveyorBelt conveyorBelt = new ConveyorBelt(1, Heading.SOUTH);
        GameController gameController = new GameController(new Board(8, 8));
        conveyorBelt.doAction(gameController, new Space(new Board(8, 8), 3, 3));

    }
}

