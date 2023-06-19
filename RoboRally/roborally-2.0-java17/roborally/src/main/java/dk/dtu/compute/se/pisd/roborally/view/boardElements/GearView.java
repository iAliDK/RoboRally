package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Gear;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GearView {
    public static void drawGearView(SpaceView spaceView, FieldAction fiac) {
        Gear gear = (Gear) fiac;
        Heading heading = gear.getHeading();
        try {
            Canvas canvas = new Canvas(SpaceView.SPACE_WIDTH, SpaceView.SPACE_HEIGHT);
            GraphicsContext graphic = canvas.getGraphicsContext2D();
            switch (heading) {
                case EAST -> {
                    Image east = new Image("file:src/main/resources/gearClockwise.png", 50, 50, true, true);
                    graphic.drawImage(east, 0, 0);
                }
                case WEST -> {
                    Image west = new Image("file:src/main/resources/gearCounterClockwise.png", 50, 50, true, true);
                    graphic.drawImage(west, 0, 0);
                }
            }
            spaceView.getChildren().add(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
