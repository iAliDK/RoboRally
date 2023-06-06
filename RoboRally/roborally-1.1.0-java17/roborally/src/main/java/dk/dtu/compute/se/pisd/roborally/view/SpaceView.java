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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Gear;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;

/**
 * The view of a space on the board.
 * <p>
 * This view is a {@link StackPane} and shows the space as a square with a
 * border and a player on it.
 * The player is shown as a triangle with the color of the player.
 * The space view is an observer of the space and updates itself when the
 * space changes.
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;


    /**
     * The constructor of the space view.
     *
     * @param space the space to be shown by this view.
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);


        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }


        Heading walls = space.getHeading();
        if (space.isWall()) {
            if (walls.equals(NORTH)) {

                String north = "-fx-background-image: url('wallNorth.png');" +
                        "-fx-background-size: 60px 20px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: up;";

                if ((space.x + space.y) % 2 != 0) north = north + "-fx-background-color: black;";
                ;
                this.setStyle(north);


            }
            if (walls.equals(EAST)) {

                String east = "-fx-background-image: url('wallEast.png');" +
                        "-fx-background-size: 20px 60px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: right;";
                if ((space.x + space.y) % 2 != 0) east = east + "-fx-background-color: black;";
                this.setStyle(east);

            }
            if (walls.equals(WEST)) {
                String west = "-fx-background-image: url('wallWest.png');" +
                        "-fx-background-size: 20px 60px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: left;";
                if ((space.x + space.y) % 2 != 0) west = west + "-fx-background-color: black;";
                this.setStyle(west);

            }
            if (walls.equals(SOUTH)) {
                String south = "-fx-background-image: url('wallSouth.png');" +
                        "-fx-background-size: 60px 20px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: bottom;";
                if ((space.x + space.y) % 2 != 0) south = south + "-fx-background-color: black;";
                this.setStyle(south);
            }
        }


        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
        boardElements();
    }

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    private void boardElements() {
        this.getChildren().clear();
        //ConveyorBelt
        if (!space.getActions().isEmpty()) {
            for (FieldAction actions : space.getActions()) {
                if (actions instanceof ConveyorBelt conveyorBelt) {
                    Polygon arrow = new Polygon(0.0, 0.0, 16.0, 30.0, 30.0, 0.0);
                    if (conveyorBelt.command == Command.FAST_FORWARD) {
                        arrow.setFill(Color.GREEN);
                    } else {
                        arrow.setFill(Color.RED);
                    }
                    arrow.setRotate((90 * conveyorBelt.heading.ordinal()) % 360);
                    this.setStyle("-fx-setBackground-color: Black");
                    this.getChildren().add(arrow);
                    //Gear
                } else if (actions instanceof Gear gear) {
                    Image img;
                    ImageView iView;
                    if (gear.isClockwise()) {
                        img = new Image("file:src/main/resources/gearClockwise.png"), SPACE_WIDTH, SPACE_HEIGHT, true, true)
                        ;
                        iView = new ImageView(img);
                    } else {
                        img = new Image("file:src/main/resources/gearCounterClockwise.png"), SPACE_WIDTH, SPACE_HEIGHT, true, true)
                        ;
                        iView = new ImageView(img);
                    }
                    this.getChildren().add(iView);
                }
            }
        }
    }

    /**
     * This method is called, when the space has changed. It updates the
     * view of the space.
     * <p>
     * The view is updated by drawing a border around the space and
     * showing the player on the space.
     * The border is drawn with the color of the player, if the space
     * is a checkpoint.
     * The player is shown as a triangle with the color of the player.
     * If the space is a checkpoint, the player is shown in the color
     * of the player.
     *
     * @param subject the space that has changed.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }
}
