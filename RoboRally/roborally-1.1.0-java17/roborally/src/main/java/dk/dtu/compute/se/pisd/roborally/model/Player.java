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
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.EAST;
import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject implements Comparable<Player> {

    final public static int NO_REGISTERS = 5;
    final public Board board;
    private final List<CommandCardField> cards = new ArrayList<>(8);
    private int numberOfCards = 8;
    private String name;
    private String color;
    private Space finalDestination;
    private Space space;
    private Heading heading = SOUTH;
    private final CommandCardField[] program;
    private int lastCheckpointVisited = 0;
    private boolean playerWin = false;
    private Space rebootSpace;
    public int antennaDistance;
    private int energyBank = 0;
    private List<Upgrade> upgrades = new ArrayList<>();
    private List<Command> damageCards = new ArrayList<>();

    /**
     * Constructor method "Player" creates a new player with the specified board, color, and name
     *
     * @param board The board where the player is going to play
     * @param color The color of the player
     * @param name  The name of the player
     */
    public Player(@NotNull Board board, @NotNull String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        //cards = new CommandCardField[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            cards.add(new CommandCardField(this));
        }
    }

    public PlayerTemplate createTemplate(){
        return new PlayerTemplate(space.x, space.y, heading, board.getGameId(), name, color);
    }

    public void setFinalDestination(Space finalDestination) {
        this.finalDestination = finalDestination;
    }
    public Space getFinalDestination() {
        return finalDestination;
    }

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
     * @param name The new name to set for the player. Must not be null. Must be different from the current name. Must not be already used by another player.
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

    public Space getSpace() {
        return space;
    }

    /**
     * Space where the player is located on the board
     *
     * @param space The new space to set for the player.
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace && (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

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


    public void setPlayerWin(boolean playerWin) {
        this.playerWin = playerWin;
    }

    public void setLastCheckpointVisited(int lastCheckpointVisited) {
        this.lastCheckpointVisited = lastCheckpointVisited;
    }

    public int getLastCheckpointVisited() {
        return lastCheckpointVisited;
    }

    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public void setRebootSpace(Space rebootSpace) {
        this.rebootSpace = rebootSpace;
    }

    public int getAntennaDistance() {
        return antennaDistance;
    }

    public void addEnergy() {
        energyBank++;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setExtraHandCard() {
        cards.add(new CommandCardField(this));
        numberOfCards++;
    }
    public List<CommandCardField> getCards() {
        return cards;
    }
    public List<Command> getDamageCards () {
        return damageCards;
    }
    public void setDamageCards(List<Command> damageCards) {
        this.damageCards = damageCards;
    }

    @Override
    public int compareTo(@NotNull Player o) {
        if (o.getAntennaDistance() > antennaDistance) return -1;
        else if (o.getAntennaDistance() == antennaDistance) return o.getSpace().y < space.y ? -1 : 1;
        else return 1;
    }
}
