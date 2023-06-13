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

class CheckpointTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Checkpoint#Checkpoint(int)}
     *   <li>{@link Checkpoint#getCheckpointNumber()}
     * </ul>
     */
    @Test
    void testConstructor() {
        assertEquals(10, (new Checkpoint(10)).getCheckpointNumber());
    }

    /**
     * Method under test: {@link Checkpoint#doAction(GameController, Space)}
     */
    @Test
    void testDoAction() {
        //Checkpoint checkpoint = new Checkpoint(1);
        Board board = new Board(8,8);
        GameController gameController = new GameController(board);
        Space space = new Space(board, 2,3);
        Player player = new Player(board, "red", "player 1", gameController);



        player.setSpace(space, true);
        space.setPlayer(player, gameController, true);
        Player currentPlayer = space.getPlayer();

        Random r = new Random();
        int random = r.nextInt(4);
        Checkpoint checkpointNo = new Checkpoint(1);
        switch(random) {
            case 0 : checkpointNo = new Checkpoint(1);
                break;
            case 1 : checkpointNo = new Checkpoint(2);
                break;
            case 2 : checkpointNo = new Checkpoint(3);
                break;
            case 3 : checkpointNo = new Checkpoint(4);
                break;
        }

        currentPlayer.setPlayerCounter(checkpointNo.getCheckpointNumber());
        //currentPlayer.setPlayerCounter(1);


        boolean result = checkpointNo.doAction(gameController, space);

        assertNotNull(currentPlayer);
        assertTrue(result);


        assertEquals(checkpointNo.getCheckpointNumber()+1,currentPlayer.getPlayerCounter());

    }
}

