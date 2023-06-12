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
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Gear;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Class "Player" represents a player in the game.
 * It is a subject in the observer pattern, because the player is displayed in the GUI and the GUI needs to be updated, when the player changes.
 *
 * @author
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;
    public int playerCounter = 1;
    private String name;
    private String color;
    private Space space;
    private Heading heading = SOUTH;
    private CommandCardField[] program;
    private CommandCardField[] cards;
    private boolean gameWon;

    private GameController gc;


    /**
     * Constructor method "Player" creates a new player with the specified board, color, and name
     *
     * @param board The board where the player is going to play
     * @param color The color of the player
     * @param name  The name of the player
     */
    public Player(@NotNull Board board, String color, @NotNull String name, GameController gc) {
        this.board = board;
        this.name = name;
        this.color = color;
        this.space = null;
        this.gc = gc;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * Method "getProgram" returns the program of the player
     *
     * @return The program of the player as an array of CommandCardField objects.
     */
    public CommandCardField[] getProgram() {
        return program;
    }
//    public CommandCardFieldTemplate createCardTemplate(){
//        return new CommandCardFieldTemplate( player, visible, cards, program);
//    }

    /**
     * Method "createTemplate" creates a template of the player
     *
     * @return The template of the player as a PlayerTemplate object.
     */
    public PlayerTemplate createTemplate() {
        List<CommandCardFieldTemplate> programTemp = new ArrayList<>();
        List<CommandCardFieldTemplate> cardsTemp = new ArrayList<>();

        for (int i = 0; i < NO_CARDS; i++) {
            cardsTemp.add(new CommandCardFieldTemplate(cards[i].isVisible(), cards[i].getCard()));
        }

        for (int i = 0; i < NO_REGISTERS; i++) {
            programTemp.add(new CommandCardFieldTemplate(program[i].isVisible(), program[i].getCard()));
        }

        return new PlayerTemplate(space.x, space.y, heading, board.getGameId(), name, color, cardsTemp, programTemp);
    }

    /**
     * Method "isGameWon" returns the gameWon of the player object.
     * @return The gameWon of the player object as a boolean.
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Method "setGameWon" returns the gameWon of the player object.
     * @param gameWon The gameWon of the player object as a boolean.
     */
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    /**
     * Method "playerCounterMethod" returns the player counter of the player object.
     * @param player The player object.
     */
    public void playerCounterMethod(Player player) {
        int playerCounter = player.getPlayerCounter();
    }

    /**
     * Method "getPlayerCounter" returns the player counter of the player object.
     *
     * @return The player counter of the player object as an integer.
     */
    public int getPlayerCounter() {
        return playerCounter;
    }

    /**
     * Method "setPlayerCounter" sets the player counter of the player object.
     * @param playerCounter The player counter of the player object as an integer.
     */
    public void setPlayerCounter(int playerCounter) {
        this.playerCounter = playerCounter;
    }

  /*  private boolean complete1;

    public void setComplete1(boolean completion1){
        complete1 = completion1;
    }
    public boolean getComplete1(){
        return complete1;
    } */

    /**
     * Method "getName" returns the name of the player object.
     * @return The name of the player object as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether the name is a null or if it's different from the current field
     * if not null and name not already in field, it sets the field to the new name value passed as a parameter
     * it then calls the method notifyChange
     * Checks whether space is null
     * if not null, it calls playerChanged
     * <p>
     * Overall, the setName method updates the name field of the object,
     * notifies observers of the change,
     * and updates the game board if necessary.
     *
     * @param name The new name to set for the player.
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }


    public String getColor() {
        return color;
    }

    /**
     * @param color The color chosen for the player to set.
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Method "getSpace" returns the space where the player is located on the board
     * @return The space where the player is located on the board as a Space object.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Space where the player is located on the board is set.
     *
     * @param space The new space to set for the player.
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace && (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null, gc, false);
            }
            if (space != null) {
                space.setPlayer(this, gc, false);
            }
            notifyChange();
        }
    }

    /**
     * Method "getHeading" returns the heading direction of the player
     *
     * @return The heading direction of the player as a Heading object.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading direction for the player
     *
     * @param heading The new heading direction
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Method "getProgramField" returns the program field of the player at the specified index
     *
     * @param i The index of the program field to return
     * @return The program field of the player at the specified index as a CommandCardField object.
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Method "getCardField" returns the card field of the player at the specified index
     *
     * @param i The index of the card field to return
     * @return The card field of the player at the specified index as a CommandCardField object.
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }
}

