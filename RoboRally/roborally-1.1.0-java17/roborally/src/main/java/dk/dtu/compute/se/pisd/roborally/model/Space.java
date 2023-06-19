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
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a space on the board.
 * It also keeps track of the player that is on the space.
 * <p>
 * The class itself extends the {@link Subject} class, which means that
 * it is observable and can notify its observers about changes.
 * <p>
 * A space can have a player on it.
 */
public class Space extends Subject {

    public final Board board;
    public final int x;
    public final int y;

    private Player player;


    private List<Heading> walls = new ArrayList<>();
    private FieldAction fieldAction = null;
    private Heading heading;

    private List<FieldAction> actions = new ArrayList();



    /**
     * Constructor for a space on the board.
     *
     * @param board the board the space belongs to
     * @param x     the x-coordinate of the space
     * @param y     the y-coordinate of the space
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }


    /**
     * @param action the action to be added to this space
     * @return the field actions on this space
     */

    public void setFieldAction(FieldAction action) {
        this.fieldAction = action;
    }

    /**
     * @return the field action on this space
     * @return the field action on this space
     */
    public FieldAction getFieldAction() {
        return fieldAction;
    }

    /**
     * @author Zainab
     * @return the heading of this space.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading of this space.
     *
     * @param heading the heading to be set on this space
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * @return the player on this space
     * @return the player on this space
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player on this space.
     * DO NOT CALL THIS TO MOVE PLAYER. USE SET SPACE.
     * @param player the player to be set on this space
     */
    public void setPlayer(Player player, GameController gc, boolean runFieldAction) {
        this.player = player;

        if(runFieldAction){
            runFieldAction(gc);
        }
        notifyChange();
    }



    private void runFieldAction(GameController gc) {
        if (fieldAction != null) {
            fieldAction.doAction(gc, this);
        }
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public List<Heading> getWalls() {
        return walls;
    }

    public void setWalls(List<Heading> walls) {
        this.walls = walls;
    }

    public void setActions(List<FieldAction> actions) {
        this.actions = actions;
    }

    public List<FieldAction> getActions() {
        return actions;
    }

}
