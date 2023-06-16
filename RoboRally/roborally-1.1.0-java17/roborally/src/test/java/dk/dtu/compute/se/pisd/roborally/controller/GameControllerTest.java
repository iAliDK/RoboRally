package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i, gameController);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i), false);
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    @DisplayName("Should move the player forward to the new space")
    void moveForwardToNewSpace() {
        Space currentSpace = gameController.board.getCurrentPlayer().getSpace();
        Heading currentHeading = gameController.board.getCurrentPlayer().getHeading();
    }

    @Test
    @DisplayName("Should not move the player forward if the new space is not valid")
    void moveForwardInvalidSpace() {
        Space space = gameController.board.getSpace(0, 0);
        Player player = gameController.board.getCurrentPlayer();
        player.setSpace(space, false);
    }

    @Test
    @DisplayName("Should not finish the programming phase when not all cards are chosen")
    void finishProgrammingPhaseWhenNotAllCardsChosen() {
        assertFalse(gameController.allCardsChosen());
        gameController.finishProgrammingPhase();
        assertFalse(gameController.allCardsChosen());
    }

    @Test
    @DisplayName("Should return true when all cards are chosen")
    void allCardsChosenWhenAllCardsAreChosen() {// Set up test data
        for (int i = 0; i < gameController.board.getPlayersNumber(); i++) {
            Player player = gameController.board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(new CommandCard(Command.FORWARD));
                    field.setVisible(true);
                }
            }
        }

        // Execute the method under test
        boolean result = gameController.allCardsChosen();

        // Verify the result
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should generate a random CommandCard")
    void generateRandomCommandCard() {
        CommandCard card = gameController.generateRandomCommandCard();
        Assertions.assertNotNull(card);
        Assertions.assertNotNull(card.command);
    }

    @Test
    @DisplayName("Should not move the current player to the given space when the space is already occupied by another player")
    void moveCurrentPlayerToSpaceWhenSpaceIsOccupied() {// Create a new player and set it to the space
        Board board = gameController.board;
        Player player = new Player(board, null, "New Player", gameController);
        board.addPlayer(player);
        player.setSpace(board.getSpace(2, 2), false);
        player.setHeading(Heading.NORTH);

        // Set the space to be occupied by another player}

        /*@Test
        @DisplayName("Should not move the current player to the given space when the space is on a different board")
        void moveCurrentPlayerToSpaceWhenSpaceIsOnDifferentBoard () {
            Space space = new Space(null, 0, 0);
            space.setPlayer(null);
            space.setFieldAction(null);
            space.setWallNorth(false);
            space.setWallEast(false);
            space.setWallSouth(false);
            space.setWallWest(false);

            Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
            GameController gameController = new GameController(board);
            gameController.moveCurrentPlayerToSpace(space);

            Player currentPlayer = board.getCurrentPlayer();
        }*/

    }

    @Test
    @DisplayName("Should return false when the current player is not the last player")
    void nextTurnAndIsLastPlayerWhenCurrentPlayerIsNotLast() {
        boolean isLastPlayer = gameController.nextTurnAndIsLastPlayer();
        Assertions.assertFalse(isLastPlayer);
    }

    @Test
    @DisplayName("Should return true when the current player is the last player")
    void nextTurnAndIsLastPlayerWhenCurrentPlayerIsLast() {        // Set up
        Board board = gameController.board;
        int lastPlayerIndex = board.getPlayersNumber() - 1;
        board.setCurrentPlayer(board.getPlayer(lastPlayerIndex));

        // Test
        boolean result = gameController.nextTurnAndIsLastPlayer();

        // Verify
        Assertions.assertTrue(result);
    }

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() + "!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }
}
