package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        Checkpoint checkpoint = new Checkpoint(2);
        GameController gameController = new GameController(new Board(8, 8));
        checkpoint.doAction(gameController, new Space(new Board(8, 8), 2, 3));
        assertEquals(2, checkpoint.getCheckpointNumber());
    }
}

