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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.model.filelist;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.filelist.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.saveBoard;

/**
 * This class is the controller for the whole application. It is the
 * observer of the {@link RoboRally} class, and it is the controller
 * for the {@link GameController}.
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> GAMEBOARD_OPTIONS = Arrays.asList("testboard", "defaultboard", "javaboard");
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private RoboRally roboRally;

   // private ArrayList<GameWalls> wall;
    private GameController gameController;
    public String[] saves = {"test.json"};

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;


    }
    private static String removeExtension(final String s)
    {
        return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
    }
    private String[] loadSaveFiles() throws Exception{
        String dirName;
        dirName = System.getProperty("user.dir");
        dirName += "\\RoboRally\\roborally-1.1.0-java17\\roborally\\target\\classes\\boards";
//        dirName = "C:\\Users\\s205412\\IdeaProjects\\RoboRally\\RoboRally\\roborally-1.1.0-java17\\roborally\\target\\classes\\boards";
        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            System.out.println("*** Not a directory! ***");
            throw new Exception();
        } else {
            String [] nameOfFiles = dir.list();
            for (int i = 0; i < nameOfFiles.length; i++) {
                nameOfFiles[i]=removeExtension(nameOfFiles[i]);
            }
            System.out.println(nameOfFiles);
            return nameOfFiles;
        }
    }


/**
     * This method is called by the {@link RoboRally} class when the
     * application is started. It sets up the application and shows
     * the main window.
     */
    /**
     * Show dialog and makes players choose number of players
     * creates a new game, and sets up the board
     * If a game is already in progress, prompts the user to
     * save the game or abort before starting a new game.
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
            ChoiceDialog<String> boardSelection = new ChoiceDialog<>(GAMEBOARD_OPTIONS.get(0), GAMEBOARD_OPTIONS);
            dialog.setTitle("Board Select");
            dialog.setHeaderText("Start board");
            Optional<String> startBoard = boardSelection.showAndWait();

            if (startBoard.isPresent()) {
                if (gameController == null) {
                    Board board = loadBoard(startBoard.get(), gameController);
                    gameController = new GameController(board);
                    int noPlayers = result.get();
                    for (int i = 0; i < noPlayers; i++) {
                        Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1), gameController);
                        board.addPlayer(player);
                        player.setSpace(board.getSpace(i % board.width, i));
                    }
                    gameController.startProgrammingPhase();
                    roboRally.createBoardView(gameController);

                }
            }



            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
//            Board board = loadBoard("defaultboard");
//            //GameWalls gameWalls = new GameWalls( board);
//            //gameWalls.addAWall(new Walls(4,4));
//            gameController = new GameController(board);
//            int noPlayers = result.get();
//            for (int i = 0; i < noPlayers; i++) {
//                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
//                board.addPlayer(player);
//                player.setSpace(board.getSpace(i % board.width, i));
//            }
//
//            // XXX: V2
//            // board.setCurrentPlayer(board.getPlayer(0));
//            gameController.startProgrammingPhase();
//            roboRally.createBoardView(gameController);
        }
    }

    public void saveGame() {
        // XXX needs to be implemented eventually
        saveBoard(this.gameController.board,  this.gameController.board.boardName+"save"+ this.gameController.board.gameId);
    }

    public void loadGame() {
        // XXX needs to be implemented eventually
        // for now, we just create a new game
        try {
            saves = loadSaveFiles();
        } catch (Exception e){
            System.out.println("No directories found");
            saves = null;
        }
        List<String> GAME_SAVES  = Arrays.asList(saves);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(GAME_SAVES.get(0), GAME_SAVES);
        dialog.setTitle("Save");
        dialog.setHeaderText("Select save you wish to load");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController == null) {
                Board board = loadBoard(result.get(), gameController);
                gameController = new GameController(board);
                board.setPhase(Phase.PROGRAMMING);
                board.setCurrentPlayer(board.getPlayer(0));
                board.setStep(0);
                roboRally.createBoardView(gameController);

            }
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Method that is used when you want to close the game.
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

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    /*public void gameOver(Player player) {
        if (player.isGameWon()) {
            stopGame();
        }
    }*/

}
