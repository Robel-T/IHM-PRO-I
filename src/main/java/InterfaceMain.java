import Controller.ControllerInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InterfaceMain extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResource("main.fxml").openStream());
        primaryStage.setResizable(true);
        primaryStage.setTitle("Image annotation");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();

        ControllerInterface mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);

        primaryStage.setOnCloseRequest((we -> {
            Platform.exit();
            System.exit(0);
        }));
    }
}
