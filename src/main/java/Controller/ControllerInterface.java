package Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Box;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerInterface implements Initializable {

    private List<Box> boxes;
    private List<Stage> toolBoxes; // Associated to boxes

    private Box currentBox;
    private Stage currentToolBox;

    private boolean shouldDrawNewBox = true;

    private int nbBoxes;
    private GridPane gridPane;
    private Stage primaryStage;
    private String imageFile;

    @FXML
    private Pane drawingPane;

    @FXML
    private  ListView<Button> listView;
    private final  ObservableList<Button> listItems = FXCollections.observableArrayList();

    @FXML
    private ImageView imageView;
    @FXML
    private Button btnAdd;
    @FXML
    private Button save;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            boxes = new LinkedList<>();
            toolBoxes = new LinkedList<>();
            save.setVisible(false);

            nbBoxes = 0;
            imageView.setVisible(false);
            listView.setItems(listItems);
            listView.setStyle("-fx-control-inner-background: #2B2B2B");
            listView.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void repaintLabels() {

        listView.getItems().clear();

        for (Box box : boxes) {

            Button button = new Button();
            button.setText(box.getLabel());
            button.setTextFill(Color.WHITE);
            Circle circle = new Circle(7);
            circle.setFill(box.getColor());
            ImageView imageView = new ImageView();

            button.setMinWidth(180);
            button.setMaxWidth(180);
            button.setGraphic(circle);
            button.setStyle("-fx-background-color: transparent");
            button.setAlignment(Pos.CENTER_LEFT);

            listView.setVisible(true);
            listItems.add(button);

            /* Allows to reopen toolbox when clicking on label */
            button.setOnMouseClicked(event -> {
                Stage toolBox = toolBoxes.get(boxes.indexOf(box));

                toolBox.setX(box.getRectangle().getX() + primaryStage.getX());
                toolBox.setY(box.getRectangle().getY() + primaryStage.getY() - 25);
                toolBox.show();

                /* Hide all others toolboxes (just edit one at time) */
                for(Stage t : toolBoxes) {
                    if(!t.equals(toolBox)) {
                        t.hide();
                    }
                }
            });
        }
    }

    /**
     * Save current file
     *
     */
    public void btnSaveFile() {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                String textFile = "";
                textFile += imageFile;
                for (Box box : boxes) {
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

    /**
     * Open a file, parse and render it
     *
     */
    public void btnOpenFile() throws IOException {

            for(Box box : boxes) {
                drawingPane.getChildren().remove(box.getRectangle());
            }
            toolBoxes.clear();
            boxes.clear();
            repaintLabels();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Files",
                            "*.txt", "*.json")); // limit fileChooser options to image files
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {

                /* parsing */
                String line = "";
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));

                /* first line : image path */
                imageFile = br.readLine();
                System.out.println(imageFile);
                while ((line = br.readLine()) != null) {
                    double x, y, height, width;
                    String label;
                    x = Float.valueOf(line);
                    y = Float.valueOf(br.readLine());
                    width = Float.valueOf(br.readLine());
                    height = Float.valueOf(br.readLine());
                    label = br.readLine();

                    currentBox = new Box(drawingPane, x, y, width, height);
                    currentBox.setLabel(label);

                    /* create toolboxes related to imported boxes */
                    toolbox(currentBox);
                    for(Stage stage : toolBoxes) {
                        stage.close();
                    }
                }

                /* place image */
                btnAdd.setVisible(false);
                imageView.setVisible(true);

                enableDrawing();
                shouldDrawNewBox = true;
                repaintLabels();

            } else {
                System.out.println("Image file selection cancelled.");
            }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void btnAddImage() throws MalformedURLException {
        for(Box box : boxes) {
            drawingPane.getChildren().remove(box.getRectangle());
        }
        toolBoxes.clear();
        boxes.clear();
        repaintLabels();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files",
                            "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {

                btnAdd.setVisible(false);
                imageView.setVisible(true);
                imageFile = selectedFile.toURI().toURL().toString();

                enableDrawing();

            } else {
                System.out.println("Image file selection cancelled.");
            }
    }

    private void enableDrawing() {

        Image image = new Image(imageFile);
        imageView.setImage(image);

        double aspectRatio = image.getWidth() / image.getHeight();
        double realWidth = Math.min(imageView.getFitWidth(), imageView.getFitHeight() * aspectRatio);
        double realHeight = Math.min(imageView.getFitHeight(), imageView.getFitWidth() / aspectRatio);

        imageView.setOnMousePressed(event -> {
            if(shouldDrawNewBox) {
                currentBox = new Box(drawingPane, event.getSceneX(), event.getSceneY() - 25);
                if(!toolBoxes.isEmpty())
                    currentToolBox.hide();
            }
        });

        System.out.println(realWidth + " " + realHeight);
        imageView.setOnMouseDragged(event -> {
            if(shouldDrawNewBox) {
                if (event.getSceneX() < realWidth + 10 && (event.getSceneY()-25) < realHeight+15 && event.getSceneX() > 10 && (event.getSceneY()-25) > 15) {
                    currentBox.render(event.getSceneX(), event.getSceneY()-25);
                }
            }
        });

        imageView.setOnMouseReleased(event -> {
            if(shouldDrawNewBox) {
                if (currentBox.getRectangle().getHeight() > 0)
                    toolbox(currentBox);
            }
            save.setVisible(true);
        });
    }

    private void toolbox(Box currentBox) {

        shouldDrawNewBox = false;

        for(Stage toolBox : toolBoxes) {
            if(!toolBox.equals(currentToolBox)) {
                toolBox.hide();
            }
        }
        gridPane = new GridPane();

        Stage fenetre = new Stage();

        fenetre.setTitle("ToolBox");
        fenetre.initStyle(StageStyle.UNDECORATED);
        fenetre.setResizable(false);

        // Set toolbox above rectangle
        fenetre.setX(currentBox.getRectangle().getX() + primaryStage.getX());
        fenetre.setY(currentBox.getRectangle().getY() + primaryStage.getY() - 25);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5, 25, 25, 25));

        final TextField label = new TextField();
        label.setPromptText("Enter label name.");
        label.setPrefColumnCount(10);

        gridPane.add(label, 0, 1);

        final Label notif = new Label("Robel");
        fenetre.setAlwaysOnTop(true);

        ImageView delete = new ImageView("poubelle.png");
        delete.setFitHeight(20);
        delete.setFitWidth(20);
        delete.setOnMouseClicked((MouseEvent Mevent) -> {
            fenetre.close();
            drawingPane.getChildren().remove(currentBox.getRectangle());
            boxes.remove(boxes.indexOf(currentBox));
            toolBoxes.remove(toolBoxes.indexOf(fenetre));
            repaintLabels();
            shouldDrawNewBox = true;
        });

        ImageView ok = new ImageView("ok.png");
        ok.setFitHeight(20);
        ok.setFitWidth(20);
        ok.setOnMouseClicked((MouseEvent Mevent) -> {
            currentBox.setLabel(label.getText());
            nbBoxes++;
            repaintLabels();
            fenetre.close();
            shouldDrawNewBox = true;
        });

        gridPane.add(delete, 1, 1);
        gridPane.add(ok,2,1);
        gridPane.setBackground(new Background(new BackgroundFill(Color.valueOf("#2B2B2B"), CornerRadii.EMPTY, Insets.EMPTY)));

        boxes.add(currentBox);
        toolBoxes.add(fenetre);
        currentToolBox = fenetre;

        final Scene scene = new Scene(gridPane);
        fenetre.setScene(scene);
        fenetre.initOwner(primaryStage);
        fenetre.show();
    }

    @FXML
    private void fileQuit() {
        System.exit(0);
        Platform.exit();
    }

    @FXML
    private void goToGithub() throws IOException, URISyntaxException {
        String urlString = "https://github.com/Robel-T/IHM-PRO-I";
        Desktop.getDesktop().browse(new URI(urlString));
    }

    @FXML
    private void addImage(){
        try {
            btnAddImage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exportImage(){
            btnSaveFile();
    }

}

