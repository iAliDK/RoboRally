package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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
    void testDoAction() {
        //ConveyorBelt conveyorBelt = new ConveyorBelt(1, Heading.SOUTH);
        Board board = new Board(8,8);
        GameController gameController = new GameController(board);
        Space space = new Space(board, 2,3);
        Player player = new Player(board, "red", "player 1", gameController);

        player.setSpace(space, true);
        space.setPlayer(player, gameController, true);
        Player currentPlayer = space.getPlayer();
        Space initial_space = space;
        Random r = new Random();
        int random = r.nextInt(4);
        ConveyorBelt belt = new ConveyorBelt(1, Heading.NORTH);
        switch(random) {
            case 0 : belt = new ConveyorBelt(1, Heading.SOUTH);
                break;
            case 1 : belt = new ConveyorBelt(1, Heading.EAST);
                break;
            case 2 : belt = new ConveyorBelt(1, Heading.WEST);
                break;
            case 3 : belt = new ConveyorBelt(1, Heading.NORTH);
                break;
        }

        boolean result = belt.doAction(gameController, space);

        assertNotNull(currentPlayer);
        assertTrue(result);
        assertEquals(gameController.board.getNeighbour(initial_space, belt.getHeading()), currentPlayer.getSpace());



    }
}

