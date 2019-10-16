package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Box;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ControllerInterface implements Initializable {

    private List<Box> boxes;
    private Box currentBox;
    private int nbBoxes;

    @FXML
    private Pane drawingPane;
    @FXML
    private VBox labelList = new VBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boxes = new LinkedList<>();
        nbBoxes = 0;

        Image image = new Image("File:img/img.jpg");
        drawingPane.getChildren().add(new ImageView(image));

        drawingPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            boxes.add(currentBox = new Box(drawingPane, event.getX(), event.getY()));
            nbBoxes++;
        });

        drawingPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            currentBox.render(event.getX(), event.getY());
        });

        // Give options (annotate, cancel,...)
        drawingPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            // TODO
            currentBox.setLabel("Label" + nbBoxes);
            repaintLabels();
        });
    }

    public void repaintLabels() {
        labelList.getChildren().clear();
        for(Box box : boxes) {
            Button button = new Button();
            button.setText(box.getLabel());
            Circle circle = new Circle(10);
            circle.setFill(box.getColor());
            button.setGraphic(circle);
            button.setStyle("-fx-border-width: 0");
            button.setStyle("-fx-fill-width: true");

            labelList.getChildren().add(button);
        }
    }
}

