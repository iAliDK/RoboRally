package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import com.beust.ah.A;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.Random.*;


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
        Gear gear1 = new Gear(false);
        Board board = new Board(8,8);
        GameController gameController = new GameController(board);
        Space space = new Space(board, 2,3);
        Player player = new Player(board, "red", "player 1", gameController);

        player.setSpace(space, true);
        space.setPlayer(player, gameController, true);
        Player currentPlayer = space.getPlayer();

        Random r = new Random();
        int random = r.nextInt(4);
        Heading initial_heading = Heading.NORTH;
        switch(random) {
            case 0 : initial_heading = Heading.SOUTH;
            break;
            case 1 : initial_heading = Heading.WEST;
            break;
            case 2 : initial_heading = Heading.NORTH;
            break;
            case 3 : initial_heading = Heading.EAST;
            break;
        }

        currentPlayer.setHeading(initial_heading);

        boolean result = gear.doAction(gameController, space);

        assertNotNull(currentPlayer);
        assertTrue(result);
        assertEquals(initial_heading.next(), currentPlayer.getHeading());

        currentPlayer.setHeading(initial_heading);
        space.setPlayer(currentPlayer, gameController, true);

        boolean result1 = gear1.doAction(gameController, space);

        assertNotNull(currentPlayer);
        assertTrue(result1);
        assertEquals(initial_heading.prev(), currentPlayer.getHeading());
        /*
        // Der er noget underligt her som vi skal se på, der er fejl da next og prev
        // er blevet exchanged i forhold til om det er med eller mod uret man drejer
        if (gear.isClockwise()) {
            // Ensure the player has turned right
            assertEquals(initial_heading.next(), currentPlayer.getHeading());
        } else {
            // Ensure the player has turned left
            assertEquals(initial_heading.prev(), currentPlayer.getHeading());
        }
        if (gear1.isClockwise()) {
            // Ensure the player has turned right
            assertEquals(initial_heading.prev(), currentPlayer.getHeading());
        } else {
            // Ensure the player has turned left
            assertEquals(initial_heading.next(), currentPlayer.getHeading());
        }*/
    }
}

