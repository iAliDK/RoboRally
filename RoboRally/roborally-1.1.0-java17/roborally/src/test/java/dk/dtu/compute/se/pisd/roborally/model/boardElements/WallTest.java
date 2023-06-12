package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Wall#Wall(Heading)}
     *   <li>{@link Wall#getHeading()}
     * </ul>
     */
    @Test
    void testConstructor() {
        assertEquals(Heading.SOUTH, (new Wall(Heading.SOUTH)).getHeading());
    }

    /**
     * Method under test: {@link Wall#doAction(GameController, Space)}
     */
    @Test
    void testDoAction() {
        Wall wall = new Wall(Heading.SOUTH);
        Board board = new Board(8,8);
        GameController gameController = new GameController(board);
        Space space = new Space(board,2,3);
        boolean result = wall.doAction(gameController, space);
        Player player = new Player(board, "red", "player 1", gameController);
        player.setHeading(Heading.SOUTH);
        gameController.executeCommandOptionAndContinue(Command.FORWARD);
        player.setSpace(space, true);


        assertEquals(space, player.getSpace());
        assertFalse(result);
        assertEquals(player.getHeading().SOUTH, player.getHeading());
    }
}

