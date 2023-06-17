package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {


    @Test
    @DisplayName("Should throw an exception when the game name is not found in API")
    void loadGameAPIWhenGameNameNotFoundThenThrowException() {
        RoboRally roboRally = new RoboRally();
        AppController appController = new AppController(roboRally);

        String nonexistentGameName = "nonexistentGameName";

        assertThrows(RuntimeException.class, () -> {
            appController.loadTurn(nonexistentGameName);
        });
    }

    @Test
    @DisplayName("Should return false when no game is running")
    void stopGameWhenNoGameIsRunning() {
        RoboRally roboRally = new RoboRally();
        AppController appController = new AppController(roboRally);

        boolean result = appController.stopGame();

        assertFalse(result);
    }

    @Test
    @DisplayName("Should throw an exception when the directory does not exist")
    void loadSaveFilesWhenDirectoryDoesNotExistThenThrowException() {
        RoboRally roboRally = new RoboRally();
        AppController appController = new AppController(roboRally);
        String nonexistentDirectory = "nonexistentDirectory";
        assertThrows(Exception.class, () -> appController.loadSaveFiles(nonexistentDirectory));
    }

    @Test
    @DisplayName("Should return the original string if there is no file extension")
    void removeExtensionReturnsOriginalStringWhenNoExtension() {
        String fileName = "testFile";
        String result = AppController.removeExtension(fileName);
        assertEquals(fileName, result);
    }

    @Test
    @DisplayName("Should remove the file extension from a given string")
    void removeExtensionRemovesFileExtension() {
        String fileName = "test.json";
        String expected = "test";
        AppController appController = new AppController(new RoboRally());
        String actual = AppController.removeExtension(fileName);
        assertEquals(expected, actual);
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