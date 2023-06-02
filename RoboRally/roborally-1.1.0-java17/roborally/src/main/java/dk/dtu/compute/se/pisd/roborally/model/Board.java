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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 *  The board class extends subject to use the method notifyChange from Subject.
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    public Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    /**
     * The method is used by the next method. It Sets the current objects boardname to a String and the width and height to integers.
     * It gives each space on the board a coordinate using a foor loop.
     * @param width The width of the current objects board.
     * @param height The height of the current objects board.
     * @param boardName The name of the current objects board.
     */
private Walls walls;

    public List<Player> getPlayers() {
        return players;
    }

    public Board(int width, int height, @NotNull String boardName, int gameId) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        this.gameId = gameId;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
        //walls = new Walls();

    }
    /*
        public void addAWall(int wallx, int wally){
        GameWalls gameWalls = new GameWalls(wallx, wally);
        walls.addAWall(gameWalls);
        }*/


    /**
     * This method uses the width and height from the above method and sets the boardname to "defaultboard"
     * This is the method we call in the App and Game controllers.
     * @param width Width from previous method.
     * @param height Height from previous method.
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard", 1);
    }

    /**
     * The method returns the gameId.
     * Not used.
     * @return gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * This method sets the gameId of the current object and throws an exception if another object has the same gameId.
     * Not used.
     * @param gameId
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * This method lets you get a space using the coordinates for the space.
     * @param x The x position.
     * @param y The y position.
     * @return Returns the space on the coordinate.
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * This method lets you get the amount of players in the game.
     * @return The number of players.
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * This method is used to check if a player is already added and if not it adds it to an array list.
     * After that it calls the notifyChange method from the Subject class.
     * @param player
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * This methods gets the player from the array list equal to the index you give your parameter.
     * @param i Index of player.
     * @return A player.
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sets the current player. Then notifies the observer.
     * @param player The player you want to be the current player.
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Gets the current phase.
     * @return Enum corresponding to the phase.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sets the current phase and notifies the observer. Does nothing if you try to set the phase to the same phase.
     * @param phase The parameter determines what phase you want to set it to.
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * Gets which step you are on.
     * @return The number of the step you are on.
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the current step to the step you want and notifies the observer.
     * @param step The step you want to change to.
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Checks your stepMode.
     * @return If stepMode is true or false.
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Sets the stepMode to the one you give it. If it's the same as current stepMode, it does nothing.
     * If it is different, it changes it and updates the observer.
     * @param stepMode
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Gets the index of the layer you provide in the parameter.
     * @param player a player.
     * @return The index of the player.
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        //if (space.getWalls().contains(heading)) {
        //  return null;
        if (space.getIsWall() == true) {
            return null;
        }
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;

        }
        Heading reverse = Heading.values()[(heading.ordinal() + 2) % Heading.values().length];
        Space result = getSpace(x, y);
        if (result != null) {
            if (result.getWalls().contains(reverse)) {
                return null;
            }

    }
        if(result.getIsWall()==true){
            return null;
        }
        return result;
    }

    /**
     * @author Zainab.
     * This method finds the space behind the player using the space coordinates and the heading as parameters.
     * It is used for moving the robot backwards.
     * @param space Coordinate of robot.
     * @param heading Heading of robot.
     * @return Coordinate behind robot.
     */
    public Space getSpaceBehind(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + height - 1) % height;
                break;
            case WEST:
                x = (x + 1) % width;
                break;
            case NORTH:
                y = (y + 1) % height;
                break;
            case EAST:
                x = (x + width - 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    /**
     * This method shows the status of the game, including the phase, current player and current step.
     * @return A string showing all the information.
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }


}
