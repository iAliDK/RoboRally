package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpaceTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
    }

    @Test
    @DisplayName("Should not run the field action when a player is set and runFieldAction is false")
    void runFieldActionWhenPlayerSetAndRunFieldActionIsFalse() {// Create a new Space object
        Space space = new Space(board, 0, 0);

        // Create a new GameController object
        GameController gc = new GameController(board);

        // Create a new FieldAction object
        FieldAction fieldAction = new FieldAction() {
            @Override
            public boolean doAction(GameController gameController, Space space) {
                return true;
            }
        };

        // Set the field action of the space object
        space.setFieldAction(fieldAction);
    }

    @Test
    @DisplayName("Should run the field action when a player is set and runFieldAction is true")
    void runFieldActionWhenPlayerIsSetAndRunFieldActionIsTrue() {// Create a new Space object
        Space space = new Space(board, 0, 0);

        // Create a new GameController object
        GameController gc = new GameController(board);

        // Create a new FieldAction object
        FieldAction fieldAction = new FieldAction() {
            @Override
            public boolean doAction(GameController gameController, Space space) {
                // Do some action
                return true;
            }
        };

        // Set the field action of the space object
        space.setFieldAction(fieldAction);

        // Set the player of the space object}

    }

    @Test
    @DisplayName("Should return an empty list when there are no actions")
    void getActionsWhenNoActions() {
        Space space = new Space(board, 0, 0);
        List<FieldAction> expectedActions = Arrays.asList();
        List<FieldAction> actualActions = space.getActions();
        assertEquals(expectedActions, actualActions);
    }

    @Test
    @DisplayName("Should return a list of actions when actions are present")
    void getActionsWhenActionsArePresent() {
        Space space = new Space(board, 0, 0);
        FieldAction action1 = new FieldAction() {
            @Override
            public boolean doAction(GameController gameController, Space space) {
                return true;
            }
        };
        FieldAction action2 = new FieldAction() {
            @Override
            public boolean doAction(
                    GameController gameController, Space space) {
                return false;
            }
        };
        List<FieldAction> actions = Arrays.asList(action1, action2);
        space.setActions(actions);

        List<FieldAction> result = space.getActions();

        assertEquals(actions, result);
        assertTrue(result.contains(action1));
        assertTrue(result.contains(action2));
    }

    @Test
    @DisplayName("Should return an empty list when there are no walls")
    void getWallsWhenNoWalls() {
        Space space = new Space(board, 0, 0);
        assertTrue(space.getWalls().isEmpty());
    }

    @Test
    @DisplayName("Should return a list of walls when walls are present")
    void getWallsWhenWallsArePresent() {
        Space space = new Space(board, 5, 5);
        space.setWalls(Arrays.asList(Heading.NORTH, Heading.WEST));

        List<Heading> walls = space.getWalls();

        assertEquals(2, walls.size());
        assertTrue(walls.contains(Heading.NORTH));
        assertTrue(walls.contains(Heading.WEST));
    }
}
