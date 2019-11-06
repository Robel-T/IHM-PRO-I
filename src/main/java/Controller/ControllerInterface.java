package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Box;
import model.JsonObjectMapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerInterface implements Initializable {

    private List<Box> boxes;
    private Box currentBox;
    private int nbBoxes;

    @FXML
    private Pane drawingPane;
    @FXML
    private VBox labelList = new VBox();

    @FXML private ImageView imageView;
    @FXML private Button btnAdd;
    @FXML private Button save;

    private String imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boxes = new LinkedList<>();
        nbBoxes = 0;
        imageView.setVisible(false);

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

    /**
     * Save current file
     * @param mouseEvent
     */
    public void btnSaveFile(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().getName().equals("MOUSE_CLICKED")) {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog( Stage.class.cast(Control.class.cast(mouseEvent.getSource()).getScene().getWindow()));

            if (file != null) {
                String textFile = "";
                textFile += imageFile;
                for(Box box : boxes) {
                    textFile += "\n" +
                        box.getRectangle().getX() + "\n" +
                        box.getRectangle().getY() + "\n" +
                        box.getRectangle().getWidth() + "\n" +
                        box.getRectangle().getHeight() + "\n" +
                        box.getLabel();
                }
                saveTextToFile(textFile, file);
            }
        }
    }

    /**
     * Open a file, parse and render it
     * @param mouseEvent
     */
    public void btnOpenFile(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getEventType().getName().equals("MOUSE_CLICKED")) {

            /* clear the current boxes */
            boxes.clear();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Files",
                            "*.txt", "*.json")); // limit fileChooser options to image files
            File selectedFile = fileChooser.showOpenDialog( Stage.class.cast(Control.class.cast(mouseEvent.getSource()).getScene().getWindow()));

            if (selectedFile != null) {

                /* parsing */
                String line = "";
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));

                /* first line : image path */
                imageFile = br.readLine();
                while((line = br.readLine()) != null) {
                    double x, y, height, width;
                    String label;
                    x = Float.valueOf(line);
                    y =  Float.valueOf(br.readLine());
                    width = Float.valueOf(br.readLine());
                    height = Float.valueOf(br.readLine());
                    label = br.readLine();

                    Box box = new Box(drawingPane, x, y, width, height);
                    box.setLabel(label);
                    boxes.add(currentBox = box);
                }

                /* place image */
                Image image = new Image(imageFile);
                imageView.setImage(image);
                imageView.setStyle("align: CENTER;");

            } else {
                System.out.println("Image file selection cancelled.");
            }
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {}
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

            } else {
                System.out.println("Image file selection cancelled.");
            }
        }
    }
}

