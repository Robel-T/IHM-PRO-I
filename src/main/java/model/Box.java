package model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Box {

    private static int RECTANGLE_NB_POINTS = 4;

    private Pane drawingPane;
    private Rectangle rectangle;
    private double startX, startY;
    private Random random;
    private Color color;
    private String label;

    public Box(Pane drawingPane, double startX, double startY) {
        this.drawingPane = drawingPane;
        this.startX = startX;
        this.startY = startY;
        this.random = new Random();

        this.rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 1);
        rectangle.setStroke(color);
        this.drawingPane.getChildren().add(rectangle);

    }

    public void render(double endX, double endY) {

        if(endX < startX) {
            rectangle.setX(endX);
            if(endY < startY)
                rectangle.setY(endY);
        } else if(endY < startY) {
            rectangle.setY(endY);
        } else {
            rectangle.setX(startX);
            rectangle.setY(startY);
        }

        rectangle.setWidth(Math.abs(endX - startX));
        rectangle.setHeight(Math.abs(endY - startY));
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public double getStartX(){
        return getStartX();
    }

    public double getStartY(){
        return getStartY();
    }
}
