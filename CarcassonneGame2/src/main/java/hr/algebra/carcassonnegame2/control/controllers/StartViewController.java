package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.views.start.StartViewsManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartViewController implements Initializable {

    public Label lbMode;
    public Label lbFirstQuestion;
    public Label lbOneName;
    public TextField taOneName;
    public static TextField taOneNameStatic;
    public Button btnGreenButton;
    public Button btnRedButton;
    public Label lbError;
    private static Stage stage;
    private static StartViewsManager startViewsManager;
    public Button btnReplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnReplay.setVisible(false);
        taOneNameStatic = taOneName;
        startViewsManager = new StartViewsManager(
                lbMode,
                lbOneName,
                lbError,
                lbFirstQuestion,
                taOneName,
                btnGreenButton,
                btnRedButton,
                btnReplay
        );
        startViewsManager.setQuestionView();
    }

    public void onGreenButtonClicked() {
        startViewsManager.onGreenButtonClicked();
    }

    public void onRedButtonAction() {
        startViewsManager.onRedButtonAction();
    }

    public static void startGameView(GameWorld game) {
        startViewsManager.startGameView(game);
    }

    public static void closeView() {
        stage.close();
    }

    public static void setStage(Stage stage) {
        StartViewController.stage = stage;
    }

    public static boolean isViewUpdated() {
        return startViewsManager.isViewUpdated();
    }

    public void onReplayAction(ActionEvent actionEvent) {
        startViewsManager.startReplayView();
    }
}
