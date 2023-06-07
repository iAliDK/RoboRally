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
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

/**
 * This class represents a space on the board.
 * It also keeps track of the player that is on the space.
 *
 * The class itself extends the {@link Subject} class, which means that
 * it is observable and can notify its observers about changes.
 *
 * A space can have a player on it.
 *
 */
public class Space extends Subject {

    private Player player;
    public int checkpointNumber = 0;
    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    public final Board board;

    public final int x;
    public final int y;

    private Heading heading;

    private Space[][] spaces;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * @author Daniel.
     */
    private boolean isWall; //Getter og Setter

    private boolean isCheckpoint; //Getter og Setter

    public boolean isCheckpoint() {
        return isCheckpoint;
    }

    public void setCheckpoint(boolean checkpoint) {
        isCheckpoint = checkpoint;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean getIsWall() {
        return isWall;
    }

    public void setIsWall(boolean wall) {
        isWall = wall;
    }



    /**
     * Constructor for a space on the board.
     *
     * @param board the board the space belongs to
     * @param x the x-coordinate of the space
     * @param y the y-coordinate of the space
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }
    public Space(Board board, int x, int y, int checkpointNumber) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.checkpointNumber = checkpointNumber;
        this.isCheckpoint = true;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player on this space.
     *
     * @param player the player to be set on this space
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public void setWalls(List<Heading> walls) {
        this.walls = walls;
    }

    public List<Heading> getWalls() {
        return walls;
    }


    public List<FieldAction> getActions() {
        return actions;
    }

    public void setSpaceProperties(int x, int y, Heading heading, boolean isWall) {
        Space space = spaces[x][y];
        space.setHeading(heading);
        space.setIsWall(isWall);

        // Perform any additional operations or logic if needed
    }

    public String getImagePath(boolean isWallHeading) {
        if (isWallHeading) {
            if (this.equals(SOUTH)) {
                return "wallSouth.png";

            } else if (this.equals(WEST)) {
                return "wallWest.png";

            } else if (this.equals(NORTH)) {
                return "wallNorth.png";

            } else if (this.equals(EAST)) {
                return "wallEast.png";
            }
        }
        return ""; // Return an empty string if it is not a wall heading
    }



}
