package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Box;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerInterface implements Initializable {

    private List<Box> boxes;
    private Box currentBox;
    private int nbBoxes;

    @FXML
    private Pane drawingPane;
    private Pane imgPane;
    @FXML
    private VBox labelList = new VBox();

    @FXML private ImageView imageView;
    @FXML private Button btnAdd;




    private String imageFile;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boxes = new LinkedList<>();
        nbBoxes = 0;
        imageView.setVisible(false);


/*

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
        */
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


    public void btnAddImage(MouseEvent mouseEvent) throws MalformedURLException {
        if (mouseEvent.getEventType().getName().equals("MOUSE_CLICKED")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files",
                            "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
            File selectedFile = fileChooser.showOpenDialog( Stage.class.cast(Control.class.cast(mouseEvent.getSource()).getScene().getWindow()));


            if (selectedFile != null) {

                btnAdd.setVisible(false);
                imageView.setVisible(true);
                imageFile = selectedFile.toURI().toURL().toString();

                Image image = new Image(imageFile);
                imageView.setImage(image);
                imageView.setStyle("align: CENTER;");








                imageView.setOnMousePressed(event -> {
                    boxes.add(currentBox = new Box(drawingPane, event.getX(), event.getY()));
                    nbBoxes++;
                });

                imageView.setOnMouseDragged(event -> {
                    currentBox.render(event.getX(), event.getY());
                });

                imageView.setOnMouseReleased(event -> {
                    // TODO
                    currentBox.setLabel("Label" + nbBoxes);
                    repaintLabels();
                });





            } else {
                System.out.println("Image file selection cancelled.");
            }
        }
    }
}

