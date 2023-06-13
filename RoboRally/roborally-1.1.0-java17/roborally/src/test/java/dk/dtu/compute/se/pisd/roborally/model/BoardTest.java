package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        board = new Board(8, 8);
        gameController = new GameController(board);
    }

    @Test
    @DisplayName("Should return the correct neighbour space when heading is WEST")
    void getNeighbourWhenHeadingWest() {
        Space space = board.getSpace(3, 3);
        Space expectedNeighbour = board.getSpace(2, 3);
        Space actualNeighbour = board.getNeighbour(space, Heading.WEST);

        assertEquals(expectedNeighbour, actualNeighbour);
    }

    @Test
    @DisplayName("Should return the correct neighbour space when heading is NORTH")
    void getNeighbourWhenHeadingNorth() {
        Space space = board.getSpace(3, 3);
        Space expectedNeighbour = board.getSpace(3, 2);
        Space actualNeighbour = board.getNeighbour(space, Heading.NORTH);
        assertEquals(expectedNeighbour, actualNeighbour);
    }

    @Test
    @DisplayName("Should return the correct neighbour space when heading is EAST")
    void getNeighbourWhenHeadingEast() {
        Space space = board.getSpace(3, 3);
        Space expectedNeighbour = board.getSpace(4, 3);
        Space actualNeighbour = board.getNeighbour(space, Heading.EAST);
        assertEquals(expectedNeighbour, actualNeighbour);
    }

    @Test
    @DisplayName("Should return the correct neighbour space when heading is SOUTH")
    void getNeighbourWhenHeadingSouth() {
        Space space = board.getSpace(3, 3);
        Space expectedNeighbour = board.getSpace(3, 4);
        Space actualNeighbour = board.getNeighbour(space, Heading.SOUTH);
        assertEquals(expectedNeighbour, actualNeighbour);
    }

    @Test
    @DisplayName("Should return the correct number of players")
    void getPlayersNumberReturnsCorrectNumberOfPlayers() {
        Player player1 = new Player(board, "red", "Player 1", gameController);
        Player player2 = new Player(board, "blue", "Player 2", gameController);
        Player player3 = new Player(board, "green", "Player 3", gameController);

        board.addPlayer(player1);
        board.addPlayer(player2);
        board.addPlayer(player3);

        List<Player> players = board.getPlayers();
        int expectedPlayersNumber = players.size();

        int actualPlayersNumber = board.getPlayersNumber();

        assertEquals(expectedPlayersNumber, actualPlayersNumber);
    }

    @Test
    @DisplayName("Should throw an exception when trying to set a different gameId")
    void setGameIdWhenDifferentThenThrowException() {
        int gameId = 1;
        board.setGameId(gameId);
        int newGameId = 2;
        Exception exception = assertThrows(IllegalStateException.class, () -> board.setGameId(newGameId));
        String expectedMessage = "A game with a set id may not be assigned a new id!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(gameId, board.getGameId());
    }

    @Test
    @DisplayName("Should return the list of players")
    void getPlayersReturnsListOfPlayers() {
        Player player1 = new Player(board, "red", "Player 1", gameController);
        Player player2 = new Player(board, "blue", "Player 2", gameController);
        board.addPlayer(player1);
        board.addPlayer(player2);

        List<Player> players = board.getPlayers();

        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
    }
}
