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
package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import static javax.swing.UIManager.get;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class ConveyorBelt extends FieldAction {
    private final int speed;
    private Heading heading;

    public ConveyorBelt(int speed, Heading heading) {
        this.speed = speed;
        this.heading = heading;
    }

    public int getSpeed() {
        return speed;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Space target = gameController.board.getNeighbour(space, this.heading);
        if (target != null) {
            switch (speed) {
                case 1 -> {


                    Player player = space.getPlayer();

                    space.setPlayer(null, gameController);
                    target.setPlayer(player, gameController);
                    //check if space is wall
                    //If there isn't a player on the new space AND (if the space is not a wall OR the player is not facing the wall
                    /*if (target.getPlayer() == null && (!player.getSpace().getClass().equals(Wall.class)|| player.getHeading() != player.getSpace().getHeading()) && (!target.getClass().equals(Wall.class) || player.getHeading() != target.getHeading().getOpposite())) {

                    } */
                }
                case 2 -> {
                    if (target.getActions().get(0) instanceof ConveyorBelt) {
                        Heading targetHeading = ((ConveyorBelt) target.getActions().get(0)).getHeading();
                        Space target2 = gameController.board.getNeighbour(target, targetHeading);
                        if (target2 != null) {
                            try {
                                gameController.moveToSpace(space.getPlayer(), target2, targetHeading);
                            } catch (GameController.ImpossibleMoveException e) {
                            }
                        }
                    }
                    try {
                        gameController.moveToSpace(space.getPlayer(), target, heading);
                    } catch (GameController.ImpossibleMoveException e) {
                    }
                }
            }
        }
        return true;
    }
}
