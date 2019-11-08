package Interface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Box;

public class ToolboxInterface extends Application {
    private final GridPane gridPane = new GridPane();
    private String mNotification = "";
    private int idNotif = 0;
    private Box box;

    public ToolboxInterface(Box box){
        this.box = box;
    }

    @Override
    public void start(Stage fenetre) {
        fenetre.setTitle("ToolBox");
        fenetre.initStyle(StageStyle.UNDECORATED);
        fenetre.setResizable(false);

        fenetre.setX(box.getStartX());
        System.out.println(box.getStartX() + " " + fenetre.getX());

        fenetre.setY(box.getStartY());


        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5, 25, 25, 25));
        

        // Chemin de la sonnerie
        Label message = new Label("Message :");
        gridPane.add(message, 0, 1);

        final Label notif = new Label(mNotification);

        ImageView delete = new ImageView("./images/poubelle.png");
        delete.setFitHeight(20);
        delete.setFitWidth(20);
        delete.setOnMouseClicked((MouseEvent event) -> {
                fenetre.close();

        });

        ImageView ok = new ImageView("./images/ok.png");
        ok.setFitHeight(20);
        ok.setFitWidth(20);
        ok.setOnMouseClicked((MouseEvent event) -> {

            fenetre.close();
        });

        gridPane.add(notif, 1, 1);
        gridPane.add(delete, 2, 1);
        gridPane.add(ok,3,1);
        gridPane.setBackground(new Background(new BackgroundFill(Color.valueOf("#2B2B2B"), CornerRadii.EMPTY, Insets.EMPTY)));

        final Scene scene = new Scene(gridPane);
        fenetre.setScene(scene);
        fenetre.show();
    }

}
