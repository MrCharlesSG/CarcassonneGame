package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.Main;
import hr.algebra.carcassonnegame2.factories.GameFactory;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.network.NetworkManager;
import hr.algebra.carcassonnegame2.utils.ViewUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartViewController implements Initializable {

    public static final int NUM_FOLLOWERS_PER_PLAYER = 7;
    public Label lbMode;
    public Label lbFirstQuestion;
    public Label lbOneName;
    public TextField taOneName;
    public Button btnGreenButton;
    public Button btnRedButton;
    public Label lbError;
    private static Stage stage;

    private static Situation situation;
    private static final String FIRST_QUESTION_TEXT = "What mode you want to play";
    private static final String ONE_NAME_QUESTION_ONLINE = "Your Name";
    private static final String ONE_NAME_QUESTION_OFFLINE = "Player %d Name";
    private static final String RED_BUTTON_QUESTION = "Offline";
    private static final String RED_BUTTON_OTHER = "Cancel";
    private static final String GREEN_BUTTON_QUESTION = "Online";
    private static final String GREEN_BUTTON_OTHER = "Accept";
    private static final String WAITING_TEXT = "Waiting for other player....";

    private static String[] playersName = new String[]{"Juan", "Monica"};
    private static final int NUM_PLAYERS = 2;
    private static int currentNumberOfPlayers;

    public static void setStage(Stage stage) {
        StartViewController.stage = stage;
    }

    public static boolean isNotHover() {
        return situation != Situation.UPDATED;
    }

    public static void startGameView(GameWorld game) {
        situation=Situation.UPDATED;
        stage.close();
        try {
            Stage gameStage = new Stage();
            initializeGame(game);
            Main.setStage(gameStage);
            gameStage.setTitle("Carcassonne ");
            Image icon = new Image(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png").toExternalForm());
            gameStage.getIcons().add(icon);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/hr/algebra/carcassonnegame2/views/gameView.fxml"));
            Parent root = loader.load();
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum Situation {
        ONLINE, OFFLINE, QUESTION, UPDATED
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //playersName = new String[NUM_PLAYERS];
        setupListeners();
        setQuestionView();
    }

    private void setupListeners() {
        taOneName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onGreenButtonClicked();
            }
        });
    }

    public void onGreenButtonClicked() {
        if (situation == Situation.QUESTION) {
            setOnlineView();
        } else if (situation == Situation.ONLINE) {
            startOnlineMode();
        } else {
            startOfflineMode();
        }
    }

    private void startOnlineMode() {
        setWaitingMode();
        startOnline();
    }

    private void startOnline() {
        try {
            NetworkManager.start(taOneName.getText());
        } catch (Exception e) {
            ViewUtils.sendAlert("Connection Error", e.getMessage(), Alert.AlertType.ERROR);
            setQuestionView();
        }
    }

    public void onRedButtonAction(ActionEvent actionEvent) {
        if (situation == Situation.QUESTION) {
            setOfflineView();
        } else {
            currentNumberOfPlayers = 0;
            setQuestionView();
        }
    }

    public void setQuestionView() {
        lbOneName.setVisible(false);
        lbMode.setVisible(false);
        taOneName.setVisible(false);
        lbFirstQuestion.setVisible(true);
        situation = Situation.QUESTION;
        btnRedButton.setText(RED_BUTTON_QUESTION);
        btnGreenButton.setText(GREEN_BUTTON_QUESTION);
        situation = Situation.QUESTION;
        lbError.setVisible(false);
    }

    private void setOnlineView() {
        lbFirstQuestion.setVisible(false);
        lbOneName.setVisible(true);
        taOneName.setVisible(true);
        lbMode.setVisible(true);
        lbMode.setText("Online");
        lbOneName.setText(ONE_NAME_QUESTION_ONLINE);
        btnGreenButton.setText(GREEN_BUTTON_OTHER);
        btnRedButton.setText(RED_BUTTON_OTHER);
        situation = Situation.ONLINE;
        lbError.setVisible(false);
    }

    private void setOfflineView() {
        lbFirstQuestion.setVisible(false);
        taOneName.clear();
        lbOneName.setVisible(true);
        taOneName.setVisible(true);
        lbMode.setVisible(true);
        lbMode.setText("Offline");
        lbOneName.setText(ONE_NAME_QUESTION_OFFLINE.formatted(currentNumberOfPlayers + 1));
        btnGreenButton.setText(GREEN_BUTTON_OTHER);
        btnRedButton.setText(RED_BUTTON_OTHER);
        situation = Situation.OFFLINE;
        lbError.setVisible(false);
    }

    private void setWaitingMode() {
        btnGreenButton.setVisible(false);
        btnRedButton.setVisible(false);
        lbOneName.setVisible(false);
        taOneName.setVisible(false);
        lbFirstQuestion.setText(WAITING_TEXT);
        lbFirstQuestion.setVisible(true);
        lbError.setVisible(false);
    }

    private void startOfflineMode() {
        if (currentNumberOfPlayers != NUM_PLAYERS - 1) {
            if (!taOneName.getText().isBlank()) {
                playersName[currentNumberOfPlayers] = taOneName.getText();
                currentNumberOfPlayers++;
                setOfflineView();
            } else {
                lbError.setVisible(true);
            }
        } else {
            if (!taOneName.getText().isBlank()) {
                playersName[currentNumberOfPlayers] = taOneName.getText();
                initializeOfflineMode();
            } else {
                lbError.setVisible(true);
            }
        }
    }

    private void initializeOfflineMode() {
        stage.close();
        try {
            Stage gameStage = new Stage();
            initializeGame(null);
            Main.setStage(gameStage);
            gameStage.setTitle("Carcassonne Offline");
            Image icon = new Image(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png").toExternalForm());
            gameStage.getIcons().add(icon);
            gameStage.setWidth(620);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/gameView.fxml"));
            Parent root = loader.load();
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeGame(GameWorld game) {
        try {
            game = game ==null ? GameFactory.createGame(playersName, NUM_FOLLOWERS_PER_PLAYER): game;
            GameController.setGame(game, situation != Situation.OFFLINE);
        } catch (IllegalArgumentException ignored) {
            closeThisView();
        }
    }

    private static void closeThisView() {
        stage.close();
    }

    /*
        @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/gameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Carcassonne "+ playerLoggedIn.name().substring(0, 1).toUpperCase() + playerLoggedIn.name().substring(1).toLowerCase());
        Image icon = new Image(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        Main.stage=stage;
        stage.show();
    }

    public static void main(String[] args) {
        playerLoggedIn = NetworkManager.start();
        launch();
    }


     */
}
