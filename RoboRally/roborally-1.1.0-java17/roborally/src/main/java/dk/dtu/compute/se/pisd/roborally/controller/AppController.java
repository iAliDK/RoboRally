/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.API.Repository;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.SaveBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;

import dk.dtu.compute.se.pisd.roborally.model.*;

import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;
import javafx.scene.control.TextField;



import java.util.*;
import java.io.*;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.SaveBoard.*;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.SaveBoard.saveBoard;

/**
 * This class is the controller for the whole application. It is the
 * observer of the {@link RoboRally} class, and it is the controller
 * for the {@link GameController}.
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> GAMEBOARD_OPTIONS = Arrays.asList("testboard", "defaultboard", "javaboard", "manualboard");
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private RoboRally roboRally;
    public static String gameName = null;
    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = ".json";

    // private ArrayList<GameWalls> wall;
    private GameController gameController;

    //Variables for saves
    public String[] saves = {"test.json"};
    int inc = 0;
    String suffix = ("." + inc);
    int noPlayers = 0;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * @param s This method removes the extension of a filename.
     * @return String without extension.
     * @author Qiao.
     */
    public static String removeExtension(final String s) {
        return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
    }

    /**
     *
     * @return
     * @throws Exception
     * @author Qiao.
     */
    public String[] loadSaveFiles() throws Exception {
        String dirName;
        dirName = System.getProperty("user.dir");
        dirName += "\\RoboRally\\roborally-1.1.0-java17\\roborally\\target\\classes\\boards";
//        dirName = "C:\\Users\\s205412\\IdeaProjects\\RoboRally\\RoboRally\\roborally-1.1.0-java17\\roborally\\target\\classes\\boards";
        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            System.out.println("*** Not a directory! ***");
            throw new Exception();
        } else {
            String[] nameOfFiles = dir.list((dir1, name) -> name.contains("save"));
            for (int i = 0; i < nameOfFiles.length; i++) {
                nameOfFiles[i] = removeExtension(nameOfFiles[i]);
            }
            System.out.println(nameOfFiles);
            return nameOfFiles;
        }
    }
    public String showGameNameDialog() {
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter game name");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Game Name");
        alert.setHeaderText("Enter a name for the game");
        alert.getDialogPane().setContent(nameTextField);
        alert.showAndWait();

        return nameTextField.getText().trim();
    }

/**
 *
 * This method is called by the {@link RoboRally} class when the
 * application is started. It sets up the application and shows
 * the main window.
 */

    /**
     *
     * Show dialog and makes players choose number of players
     * creates a new game, and sets up the board.
     * If a game is already in progress, prompts the user to
     * save the game or abort before starting a new game.
     * @author Qiao.
     */

    public void newGame() {

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }
             noPlayers = result.get();

            ChoiceDialog<String> boardSelection = new ChoiceDialog<>(GAMEBOARD_OPTIONS.get(0), GAMEBOARD_OPTIONS);
            dialog.setTitle("Board Select");
            dialog.setHeaderText("Start board");
            Optional<String> startBoard = boardSelection.showAndWait();

            if (startBoard.isPresent()) {
                if (gameController == null) {
//                    Board board = new Board(15,15, "javaboard", 1);
                    loadOfflineBoard(startBoard.get());
                    gameName = showGameNameDialog();
                    if (gameName != null && !gameName.isEmpty()) {
                        gameController.startProgrammingPhase();
                        roboRally.createBoardView(gameController);
                    } else {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Invalid Game Name");
                        alert.setHeaderText("Please enter a valid game name");
                        alert.setContentText("The game name cannot be empty.");
                        alert.showAndWait();
                    }
                }
            }
        }
        try {
            api.newGame(newTemplate, gameName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * This method is called by the {@link RoboRally} class when game is to be saved.
     * The method prompts the user whether to overwrite the current save or create a new one.
     * <p>
     *     @author Qiao.
     *
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
        if (this.gameController.board.boardName.contains("save")) {
            {

                ButtonType yes = new ButtonType("Overwrite", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("New save", ButtonBar.ButtonData.OTHER);
                Alert alert = new Alert(AlertType.CONFIRMATION, "Do you wish to overwrite your current save?", yes, no);
                alert.setTitle("Overwrite save?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == yes) {
                    if (inc != 0) {
                        if (this.gameController.board.boardName.contains(".")) {
                            this.gameController.board.boardName = this.gameController.board.boardName.substring(0, this.gameController.board.boardName.indexOf("."));
                        }
                        suffix = ("." + inc);
                        String temp = this.gameController.board.boardName;
                        this.gameController.board.boardName += suffix;
                        saveBoard(this.gameController.board, this.gameController.board.boardName);
                        this.gameController.board.boardName = temp;
                    } else {
                        saveBoard(this.gameController.board, this.gameController.board.boardName);
                    }
                } else if (result.get() == no) {
                    inc++;
                    if (this.gameController.board.boardName.contains(".")) {
                        this.gameController.board.boardName = this.gameController.board.boardName.substring(0, this.gameController.board.boardName.indexOf("."));
                    }
                    suffix = ("." + inc);
                    saveBoard(this.gameController.board, this.gameController.board.boardName + suffix);
                }
            }
        } else {
            saveBoard(this.gameController.board, this.gameController.board.boardName + "save" + this.gameController.board.gameId);
            this.gameController.board.boardName += "save" + this.gameController.board.gameId;
        }
    }

    /**
     *
     * This method is called by the {@link RoboRally} class when game is to be loaded.
     * The method prompts the user to select a save file to load.
     * @author Qiao.
     */
    public void loadGame() {

        // XXX needs to be implemented eventually
        // for now, we just create a new game
        try {
            saves = loadSaveFiles();
        } catch (Exception e) {
            System.out.println("No directories found");
            saves = null;
        }
        List<String> GAME_SAVES = Arrays.asList(saves);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(GAME_SAVES.get(0), GAME_SAVES);
        dialog.setTitle("Save");
        dialog.setHeaderText("Select save you wish to load");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
                Board board = loadOfflineBoard(result.get());
                if (board.boardName.contains(".")) {
                    inc = Integer.parseInt(board.boardName.substring(board.boardName.indexOf(".") + 1));
                }
            roboRally.createBoardView(gameController);
        }
    }

    /**
     * @Author Qiao.
     * @param boardname
     * @return Loads an initial board based on jsons or an ongoing game.
     */
    public Board loadOfflineBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }
        ClassLoader classLoader = SaveBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + JSON_EXT);
        if (inputStream == null) {
            inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + "javaboard" + JSON_EXT);
        }
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);
            result = new Board(template.width, template.height, boardname, (int) (Math.random() * 1001));
            template.width = result.width;
            template.height = result.height;
            template.boardName = result.boardName;
            template.saveName = gameName;
            template.phase = result.getPhase();
            template.step = result.getStep();
            //Comment spaces out to use board manual creation.
            if (boardname != "manualboard"){
                for (SpaceTemplate spaceTemplate : template.spaces) {
                    Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                    space.setFieldAction(spaceTemplate.action);
                }
            }
            gameController = new GameController(result);
            if (noPlayers!=0) {
                for (int i = 0; i < noPlayers; i++) {
                    Player player = new Player(result, PLAYER_COLORS.get(i), "Player " + (i + 1), gameController);
                    result.addPlayer(player);
                    player.setSpace(result.getSpace(i % result.width, i), false);
                }
            } else {
                //Player positions
                for (int i = 0; i < template.players.size(); i++) {
                    Player player = new Player(result, template.players.get(i).color, "Player " + (i + 1), gameController);
                    result.addPlayer(player);
                    player.setSpace(result.getSpace(template.players.get(i).x, template.players.get(i).y), false);
                    player.setHeading(template.players.get(i).heading);
                    //Player cards
                    if (player != null && !template.players.get(i).cards.isEmpty()) {
                        for (int j = 0; j < Player.NO_CARDS; j++) {
                            CommandCardField field = player.getCardField(j);
                            if (template.players.get(i).cards.get(j).visible) {
                                field.setCard(template.players.get(i).cards.get(j).card);
                                field.setVisible(true);
                            } else {
                                field.setVisible(false);
                            }
                        }
                        for (int j = 0; j < Player.NO_REGISTERS; j++) {
                            CommandCardField field = player.getProgramField(j);
                            if (template.players.get(i).program.get(j).visible) {
                                field.setCard(template.players.get(i).program.get(j).card);
                                field.setVisible(true);
                            } else {
                                field.setVisible(false);
                            }
                        }
                    }
                }
            }
                reader.close();
                result.setPhase(template.phase);
                result.setCurrentPlayer(result.getPlayer(template.currentplayer));
                result.setStep(template.step);
                newTemplate = template;


            {return result;}
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
        return null;
    }
    public void loadOnlineGame() {
        gameName = showGameNameDialog();
        if (gameName != null && !gameName.isEmpty()) {
            loadTurn(gameName);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Game Name");
            alert.setHeaderText("Please enter a valid game name");
            alert.setContentText("The game name cannot be empty.");
            alert.showAndWait();
        }
    }


    /**
     * This method is called by the {@link RoboRally} class when game is to be stopped.
     * @return true if the game was stopped, false if the game was not running.
     */
    public boolean stopGame() {
        if (gameController != null) {
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Exits the application, prompts the user whether they are sure to exit the application.
     */

    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

   /* public void gameWon(Player player){
        if(Checkpoint.getGameWinner(true)){
            Alert winner = new Alert(AlertType.CONFIRMATION);
            winner.setTitle("Player"+ this.player.getPlayerCounter()==4);
            winner.setContentText("Are you sure you want to exit RoboRally?");
        }
    }*/

    /**
     * Returns true if there is currently a game running, false otherwise.
     */

    public boolean isGameRunning() {
        return gameController != null;
    }

//    @Override
//    public void update(Subject subject) {
//        // XXX do nothing for now
//    }
    static Repository api = new Repository();

public void updateButton() {
    loadTurn(gameName);

    //TODO Make timer auto refresh save very x seconds.
//    Timer timer = new Timer();
//
//    timer.schedule( new TimerTask() {
//        public void run() {
//            loadTurn(gameName);
//        }
//    }, 0, 30*1000);
}
//Works as intended.
    public void loadTurn(String name) {
        Board result;
        try {
            BoardTemplate template = api.getBoardTemplate(name);
            result = new Board(template.width, template.height, name, (int) (Math.random() * 1001));
            for (SpaceTemplate spaceTemplate : template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                space.setFieldAction(spaceTemplate.action);
            }
            gameController = new GameController(result);
            //Player positions
            for (int i = 0; i < template.players.size(); i++) {
                Player player = new Player(result, template.players.get(i).color, "Player " + (i + 1), gameController);
                result.addPlayer(player);
                player.setSpace(result.getSpace(template.players.get(i).x, template.players.get(i).y), true);
                player.setHeading(template.players.get(i).heading);
                //Player cards
                if (player != null && !template.players.get(i).cards.isEmpty()) {
                    for (int j = 0; j < Player.NO_CARDS; j++) {
                        CommandCardField field = player.getCardField(j);
                        if(template.players.get(i).cards.get(j).visible){
                            field.setCard(template.players.get(i).cards.get(j).card);
                            field.setVisible(true);
                        } else {
                            field.setVisible(false);
                        }
                    }
                    for (int j = 0; j < Player.NO_REGISTERS; j++) {
                        CommandCardField field = player.getProgramField(j);
                        if(template.players.get(i).program.get(j).visible){
                            field.setCard(template.players.get(i).program.get(j).card);
                            field.setVisible(true);
                        } else {
                            field.setVisible(false);
                        }
                    }
                }
            }
            result.setPhase(template.phase);
            result.setCurrentPlayer(result.getPlayer(template.currentplayer));
            result.setStep(template.step);
            roboRally.createBoardView(gameController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    public Object getGameController() {
        return gameController;
    }

    public void setGameController(Object mockGameController) {
        this.gameController = (GameController) mockGameController;
    }

    /*public void gameOver(Player player) {
        if (player.isGameWon()) {
            stopGame();
        }
    }*/

}

