package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class GameScene {

    private final double windowWidth = 500;
    private final double windowHeight = 350;

    private final double brickWidth;
    private final double brickHeight;
    private final int numOfBricks;

    private Pane pane;

    private final Ball ball;
    private final Bat bat;
    private Set<Brick> brickList;
    private final double padding = 2;
    private double x = 50, y = 30;
    Scene scene;
    public GameScene() {

        numOfBricks = 15;
        brickWidth = (windowWidth - (2 * x)) / numOfBricks - padding;
        brickHeight = 17;

        ball = new Ball();
        double batWidth = 65;
        double batHeight = 5;
        bat = new Bat(windowWidth / 2, windowHeight - 12, batWidth, batHeight);

        setBricks();
        ball.go();
    }

    public void setBricks() {
        brickList = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            for (int p = 0; p < numOfBricks; p++) {
                brickList.add(new Brick(x, y, brickWidth, brickHeight));
                x += brickWidth + padding;
            }
            x = 50;
            y += brickHeight + padding;
        }
    }

    public Scene initScene() {
        pane = new Pane();
        pane.setStyle("-fx-background-color: black");

        scene = new Scene(pane, windowWidth, windowHeight);

//        scene.setOnMouseMoved(bat.getEvent());
//        scene.setOnMouseDragged(bat.getEvent());

        pane.getChildren().addAll(brickList);
        pane.getChildren().addAll(ball, bat);
        brickList = null;
        return scene;
    }

    class Ball extends Circle {
        private final double radius = 5;
        private double deltaX = 2;
        private double deltaY = -2;
        private Timeline loop;

        public Ball() {
            super.setRadius(radius);
            super.setFill(Color.LIGHTGREEN);
            super.relocate(355, 200);
        }

        public void go() {

            loop = new Timeline(new KeyFrame(Duration.millis(10), e -> {

                if (!intersectsWithBrick())
                    if (!intersectsWithPane())
                        intersectsWithBat();

                ball.setLayoutX(ball.getLayoutX() + deltaX);
                ball.setLayoutY(ball.getLayoutY() + deltaY);

            }));

            loop.setCycleCount(Timeline.INDEFINITE);
            loop.play();
        }


        public boolean intersectsWithBrick() {
            Bounds bounds = ball.getBoundsInParent();
            for (Node br : pane.getChildren()) {
                if (!(br instanceof Brick)) continue;

                Bounds bound = br.getBoundsInLocal();

                if (bounds.intersects(bound)) {
                    if (ball.getLayoutX() + radius <= bound.getMinX() ||
                            ball.getLayoutX() - radius >= bound.getMaxX()) {
                        deltaX *= -1;
                    } else {
                        deltaY *= -1;
                    }
                    pane.getChildren().remove(br);
                    return true;
                }
            }
            return false;
        }

        public boolean intersectsWithPane() {
            final Bounds paneBounds = pane.getBoundsInLocal();
            double ballX = ball.getLayoutX();
            double ballY = ball.getLayoutY();

            if (ballX >= (paneBounds.getMaxX() - radius)) {
                deltaX *= -1;
                return true;
            } else if (ballX <= (paneBounds.getMinX() + radius)) {
                deltaX *= -1;
                return true;
            }
            if (ballY >= (paneBounds.getMaxY() - radius)) {
                deltaY *= -1;
//                loop.stop();
                return true;
            } else if (ballY <= (paneBounds.getMinY() + radius)) {
                deltaY *= -1;
                return true;
            }
            return false;
        }

        public void intersectsWithBat() {
            final Bounds bound = bat.getBoundsInParent();
            Bounds bounds = ball.getBoundsInParent();

            if (bound.intersects(bounds)){
                if (ball.getLayoutX() + radius <= bound.getMinX() ||
                ball.getLayoutX() - radius >= bound.getMaxX() ){
                    deltaX *= -1;
                } else {
                    deltaY *= -1;
                }
            }
        }
    }

    class Bat extends Rectangle {
        private final double speed = 15;
        public Bat(double x, double y, double width, double height) {
            super(x, y, width, height);
            super.setFill(Color.YELLOW);
            super.setArcWidth(5.0);
            super.setArcHeight(5.0);
        }

        public EventHandler<MouseEvent> getEvent() {
            return event -> {
                setX(Math.min(event.getSceneX(), windowWidth - getWidth()));
            };
        }
    }

}


