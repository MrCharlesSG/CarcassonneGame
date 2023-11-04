package hr.algebra.carcassonnegame2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/gameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Carcassonne Game");
        Image icon = new Image(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}