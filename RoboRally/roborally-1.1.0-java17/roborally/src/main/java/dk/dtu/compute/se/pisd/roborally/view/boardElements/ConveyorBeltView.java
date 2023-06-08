package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ConveyorBeltView {
    public static void drawConveyorBeltView(SpaceView spaceView, FieldAction fiac) {
        ConveyorBelt conveyorBelt = (ConveyorBelt) fiac;
        Heading heading = conveyorBelt.getHeading();
        try {
            Canvas canvas = new Canvas(SpaceView.SPACE_WIDTH, SpaceView.SPACE_HEIGHT);
            GraphicsContext graphic = canvas.getGraphicsContext2D();
            switch (heading) {
                case EAST -> {
                    if (conveyorBelt.getSpeed() == 1) {
                        Image east = new Image("conveyorBeltEast1.png", 50, 50, true, true);
                        graphic.drawImage(east, 0, 0);
                    } else {
                        Image east2 = new Image("conveyorBeltEast2.png", 50, 50, true, true);
                        graphic.drawImage(east2, 0, 0);
                    }
                }
                case WEST -> {
                    if (conveyorBelt.getSpeed() == 1) {
                        Image west = new Image("conveyorBeltWest1.png", 50, 50, true, true);
                        graphic.drawImage(west, 0, 0);
                    } else {
                        Image west2 = new Image("conveyorBeltWest2.png", 50, 50, true, true);
                        graphic.drawImage(west2, 0, 0);
                    }
                }
                case NORTH -> {
                    if (conveyorBelt.getSpeed() == 1) {
                        Image north = new Image("conveyorBeltNorth1.png", 50, 50, true, true);
                        graphic.drawImage(north, 0, 0);
                    } else {
                        Image north2 = new Image("conveyorBeltNorth2.png", 50, 50, true, true);
                        graphic.drawImage(north2, 0, 0);
                    }
                }
                case SOUTH -> {
                    if (conveyorBelt.getSpeed() == 1) {
                        Image south = new Image("conveyorBeltSouth1.png", 50, 50, true, true);
                        graphic.drawImage(south, 0, 0);
                    } else {
                        Image south2 = new Image("conveyorBeltSouth2.png", 50, 50, true, true);
                        graphic.drawImage(south2, 0, 0);
                    }
                }
            }
            spaceView.getChildren().add(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
