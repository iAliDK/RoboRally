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
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.*;
import dk.dtu.compute.se.pisd.roborally.view.boardElements.ConveyorBeltView;
import dk.dtu.compute.se.pisd.roborally.view.boardElements.GearView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

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
     * @param space the space to be shown by this view. The view will be an observer of the space. It will update itself when the space changes. It will also update the space when the view is clicked.
     */
    public SpaceView(@NotNull Space space) {

        Pane pane = new Pane();
        Rectangle rectangle = new Rectangle(50.0, 50.0, SPACE_WIDTH, SPACE_HEIGHT);
        rectangle.setFill(Color.ALICEBLUE);
        pane.getChildren().add(rectangle);
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

        if (space.getFieldAction() != null) {


            if (space.getFieldAction().getClass().equals(Wall.class)) {
                 Wall wall = (Wall) space.getFieldAction();

                if (wall.getHeading()==NORTH) {

                    String north = "-fx-background-image: url('wallNorth.png');" +
                            "-fx-background-size: 60px 20px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: up;";

                    if ((space.x + space.y) % 2 != 0) north = north + "-fx-background-color: black;";
                    this.setStyle(north);


                }
                if (wall.getHeading()==EAST) {

                    String east = "-fx-background-image: url('wallEast.png');" +
                            "-fx-background-size: 20px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: right;";
                    if ((space.x + space.y) % 2 != 0) east = east + "-fx-background-color: black;";
                    this.setStyle(east);

                }
                if (wall.getHeading()==WEST) {
                    String west = "-fx-background-image: url('wallWest.png');" +
                            "-fx-background-size: 20px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: left;";
                    if ((space.x + space.y) % 2 != 0) west = west + "-fx-background-color: black;";
                    this.setStyle(west);

                }
                if (wall.getHeading()==SOUTH) {
                    String south = "-fx-background-image: url('wallSouth.png');" +
                            "-fx-background-size: 60px 20px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: bottom;";
                    if ((space.x + space.y) % 2 != 0) south = south + "-fx-background-color: black;";
                    this.setStyle(south);
                }
            }


            if (space.getFieldAction().getClass().equals(Checkpoint.class)) {
                Checkpoint checkpoint = (Checkpoint) space.getFieldAction();
                if (checkpoint.getCheckpointNumber() == 1) {
                    this.setStyle("-fx-background-color: GREEN");
                }
                if (checkpoint.getCheckpointNumber()== 1) {
                    String cp1 = "-fx-background-image: url('TheFlag1.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: up;";
                    if ((space.x + space.y) % 2 != 0) cp1 = cp1 + "-fx-background-color: black;";
                    this.setStyle(cp1);
                }
                if (checkpoint.getCheckpointNumber() == 2) {
                    String cp2 = "-fx-background-image: url('TheFlag2.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: up;";
                    if ((space.x + space.y) % 2 != 0) cp2 = cp2 + "-fx-background-color: black;";
                    this.setStyle(cp2);
                }
                if (checkpoint.getCheckpointNumber() == 3) {
                    String cp3 = "-fx-background-image: url('TheFlag3.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: up;";
                    if ((space.x + space.y) % 2 != 0) cp3 = cp3 + "-fx-background-color: black;";
                    this.setStyle(cp3);
                }
                if (checkpoint.getCheckpointNumber()== 4) {
                    String cp4 = "-fx-background-image: url('TheFlag4.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: up;";
                    if ((space.x + space.y) % 2 != 0) cp4 = cp4 + "-fx-background-color: black;";
                    this.setStyle(cp4);
                }
            }

///////////////////////////////GEAR//////////////////////////////////////////


            if (space.getFieldAction().getClass().equals(Gear.class)) {
                Gear gear =  (Gear) space.getFieldAction();
                if (gear.isClockwise()) {
                    String gearClockwise = "-fx-background-image: url('gearClockwise.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0) gearClockwise = gearClockwise + "-fx-background-color: black;";
                    this.setStyle(gearClockwise);
                }else {
                    String gearCounterClockwise = "-fx-background-image: url('gearCounterClockwise.png');" +
                            "-fx-background-size: 60px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0)
                        gearCounterClockwise = gearCounterClockwise +
                                "-fx-background-color: black;";
                    this.setStyle(gearCounterClockwise);
                }
            }

            if (space.getFieldAction().getClass().equals(ConveyorBelt.class)) {
                ConveyorBelt conveyorBelt = (ConveyorBelt) space.getFieldAction();
                if (conveyorBelt.getHeading() == WEST) {
                    String eastToWest = "-fx-background-image: url('conveyorBeltWest1.png');" +
                            "-fx-background-size: 60px 30px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0)
                        eastToWest = eastToWest + "-fx-background-color: black;";
                    this.setStyle(eastToWest);
                }

                if (conveyorBelt.getHeading() == EAST) {
                    String westToEast = "-fx-background-image: url('conveyorBeltEast1.png');" +
                            "-fx-background-size: 60px 30px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0)
                        westToEast = westToEast + "-fx-background-color: black;";
                    this.setStyle(westToEast);
                }

                if(conveyorBelt.getHeading() == SOUTH) {
                    String northToSouth = "-fx-background-image: url('conveyorBeltSouth1.png');" +
                            "-fx-background-size: 30px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0)
                        northToSouth = northToSouth + "-fx-background-color: black;";
                    this.setStyle(northToSouth);
                }

                if(conveyorBelt.getHeading() == NORTH) {
                    String southToNorth = "-fx-background-image: url('conveyorBeltNorth1.png');" +
                            "-fx-background-size: 30px 60px;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;";
                    if ((space.x + space.y) % 2 != 0)
                        southToNorth = southToNorth + "-fx-background-color: black;";
                    this.setStyle(southToNorth);
                }
            }
        }

        space.attach(this);
        update(space);
    }

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0, 10.0, 20.0, 20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
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
     * @param subject The space that has changed. In this case the space is the subject.
     */
    @Override
    public void updateView(Subject subject) {
        this.getChildren().clear();
        updateSpace();
        for (FieldAction fiac : space.getActions()) {
            if (fiac instanceof ConveyorBelt) {
                ConveyorBeltView.drawConveyorBeltView(this, fiac);
            } else if (fiac instanceof Gear) {
                GearView.drawGearView(this, fiac);
            }
        }
        updatePlayer();
    }
    public void updateSpace() {
        Image image = new Image("file:src/main/resources/flag.png");
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        this.getChildren().add(canvas);
    }
}
