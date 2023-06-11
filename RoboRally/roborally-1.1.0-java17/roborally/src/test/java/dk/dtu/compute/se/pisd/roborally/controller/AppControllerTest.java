package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppControllerTest {

    @Test
    void newGame() {
    }

    /**
     * Method under test: {@link AppController#AppController(RoboRally)}
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
     */
    @Test
    void testConstructor2() {
        String[] stringArray = (new AppController(new RoboRally())).saves;
        assertEquals(1, stringArray.length);
        assertEquals("test.json", stringArray[0]);
    }

    /**
     * Method under test: {@link AppController#AppController(RoboRally)}
     */
    @Test
    void testConstructor3() {
        assertThrows(IllegalArgumentException.class, () -> new AppController(null));
    }

    /**
     * Method under test: {@link AppController#newGame()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testNewGame() {
        // TODO: Complete this test.
        //   Reason: R006 Static initializer failed.
        //   The static initializer of
        //   javafx.scene.Node
        //   threw java.lang.RuntimeException while trying to load it.
        //   Make sure the static initializer of Node
        //   can be executed without throwing exceptions.
        //   Exception: java.lang.RuntimeException: No toolkit found
        //       at com.sun.javafx.tk.Toolkit.getToolkit(Toolkit.java:276)
        //       at com.sun.javafx.perf.PerformanceTracker.logEvent(PerformanceTracker.java:100)
        //       at javafx.scene.Node.<clinit>(Node.java:417)
        //       at javafx.scene.control.Dialog.<init>(Dialog.java:512)
        //       at javafx.scene.control.ChoiceDialog.<init>(ChoiceDialog.java:119)
        //       at dk.dtu.compute.se.pisd.roborally.controller.AppController.newGame(AppController.java:110)

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
     * Method under test: {@link AppController#saveGame()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSaveGame() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot read field "board" because "this.gameController" is null
        //       at dk.dtu.compute.se.pisd.roborally.controller.AppController.saveGame(AppController.java:168)

        (new AppController(new RoboRally())).saveGame();

    }

    /**
     * Method under test: {@link AppController#loadGame()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLoadGame() {
        // TODO: Complete this test.
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:209)
        //       at java.util.Arrays$ArrayList.<init>(Arrays.java:4137)
        //       at java.util.Arrays.asList(Arrays.java:4122)
        //       at dk.dtu.compute.se.pisd.roborally.controller.AppController.loadGame(AppController.java:180)

        (new AppController(new RoboRally())).loadGame();

        assertArrayEquals(new String[]{"test.json"}, (new AppController(new RoboRally())).saves);

    }

    /**
     * Method under test: {@link AppController#stopGame()}
     */
    @Test
    void testStopGame() {
        assertFalse((new AppController(new RoboRally())).stopGame());
    }

    /**
     * Method under test: {@link AppController#exit()}
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