package hr.algebra.carcassonnegame2;

import hr.algebra.carcassonnegame2.control.controllers.StartViewController;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static boolean firstClientConnected = false;
    public static PlayerType playerLoggedIn;

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/startView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Initialize Carcassonne Game");
        Image icon = new Image(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        Main.stage=stage;
        StartViewController.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Main.stage= stage;
    }
}