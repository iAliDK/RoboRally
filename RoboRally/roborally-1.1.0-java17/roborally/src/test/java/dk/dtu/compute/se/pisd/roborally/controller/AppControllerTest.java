package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppControllerTest {

    /**
     * Method under test: {@link AppController#newGame()}
     */
    @Test
    // TODO: Complete this test
    void testNewGame() {

        // Arrange
        // TODO: Populate arranged inputs
        AppController appController = new AppController(new RoboRally());


        // Act
        appController.newGame();

        // Assert
        assertEquals(1, appController.saves.length);
        assertArrayEquals(new String[]{"test.json"}, appController.saves);
        assertFalse(appController.isGameRunning());
    }

    /**
     * Method under test: {@link AppController#AppController(RoboRally)}
     * this test verifies that the constructor of the AppController class
     * initializes the object correctly by checking the initial state of the object,
     * the length and content of the saves array, and the value of the first element in the array.
     */
    @Test
    void testConstructor() {
        AppController actualAppController = new AppController(new RoboRally());
        assertFalse(actualAppController.isGameRunning());
        String[] stringArray = actualAppController.saves;
        assertEquals(1, stringArray.length);
        assertArrayEquals(new String[]{"test.json"}, stringArray);
        assertEquals("test.json", stringArray[0]);
    }

    /**
     * Method under test: {@link AppController#AppController(RoboRally)}
     * passing null to the AppController constructor should
     * result in an IllegalArgumentException being thrown.
     */
    @Test
    void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new AppController(null));
    }



    /**
     * Method under test: {@link AppController#saveGame()}
     */
    @Test
    //TODO: Complete this test
    void testSaveGame() {

        (new AppController(new RoboRally())).saveGame();

    }

    /**
     * Method under test: {@link AppController#loadGame()}
     */
    @Test
    //TODO: Complete this test
    void testLoadGame() {

        (new AppController(new RoboRally())).loadGame();

        assertArrayEquals(new String[]{"test.json"}, (new AppController(new RoboRally())).saves);

    }

    /**
     * Method under test: {@link AppController#stopGame()}
     * The test is checking that invoking the stopGame method on a newly
     * created AppController instance returns false. It indicates that
     * the game is not stopped immediately upon its creation.
     */
    @Test
    void testStopGame() {
        assertFalse((new AppController(new RoboRally())).stopGame());
    }

    /**
     * Method under test: {@link AppController#exit()}
     * This test ensures that when the `exit()` method is called,
     * it correctly stops the game and saves the game state.
     */
    @Test
    void testExit() {
        AppController appController = new AppController(new RoboRally());
        appController.exit();
        assertFalse(appController.isGameRunning());
        assertEquals(1, appController.saves.length);
    }

    /**
     * Method under test: {@link AppController#isGameRunning()}
     */
    @Test
    void testIsGameRunning() {
        assertFalse((new AppController(new RoboRally())).isGameRunning());
    }
}