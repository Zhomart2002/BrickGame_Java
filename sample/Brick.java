package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {

    public Brick(double x, double y, double width, double height){
        super(x, y, width, height);
        super.setFill(Color.YELLOW);
        super.setArcWidth(10.0);
        super.setArcHeight(10.0);
    }


}
