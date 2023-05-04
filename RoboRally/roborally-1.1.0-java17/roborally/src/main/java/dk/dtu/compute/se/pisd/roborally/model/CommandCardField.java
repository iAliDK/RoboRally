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

/**
 * This class is used to represent a command card field in the game.
 * The command card field is used to hold a command card of a player.
 * <p>
 * A command card can be visible or not. This is used to hide the
 * command card in the GUI, when it is not used.
 */
public class CommandCardField extends Subject {

    final public Player player;

    private CommandCard card;

    private boolean visible;

    /**
     * Creates a command card field for the given player.
     * @param player
     */
    public CommandCardField(Player player) {
        this.player = player;
        this.card = null;
        this.visible = true;
    }

    /**
     * Returns the command card of this field.
     * @return
     */
    public CommandCard getCard() {
        return card;
    }

    /**
     * Sets the command card of this field.
     * @param card
     */
    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * Returns the visibility of the command card field.
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the command card field.
     * @param visible
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
