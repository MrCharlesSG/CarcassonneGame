package hr.algebra.carcassonnegame2.views.start;

import hr.algebra.carcassonnegame2.Main;
import hr.algebra.carcassonnegame2.control.controllers.GameController;
import hr.algebra.carcassonnegame2.control.controllers.StartViewController;
import hr.algebra.carcassonnegame2.factories.GameFactory;
import hr.algebra.carcassonnegame2.factories.PlayerFactory;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.network.NetworkManager;
import hr.algebra.carcassonnegame2.utils.ViewUtils;
import javafx.fxml.FXMLLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.*;

public class StartViewsManager {
    private final Label lbMode;
    private final Label lbFirstQuestion;
    private final Label lbOneName;
    private final TextField taOneName;
    private final Button btnGreenButton;
    private final Button btnRedButton;
    private final Label lbError;
    private static final String ONE_NAME_QUESTION_ONLINE = "Your Name";
    private static final String ONE_NAME_QUESTION_OFFLINE = "Player %d Name";
    private static final String RED_BUTTON_QUESTION = "Offline";
    private static final String RED_BUTTON_OTHER = "Cancel";
    private static final String GREEN_BUTTON_QUESTION = "Online";
    private static final String GREEN_BUTTON_OTHER = "Accept";
    private static final String WAITING_TEXT = "Waiting for other player....";
    private static final double OFFLINE_SCREEN_SIZE = 620;
    private static final double ONLINE_SCREEN_SIZE = 800;
    private static List<Player> players;

    private enum Situation {
        ONLINE, OFFLINE, QUESTION, UPDATED
    }
    private static Situation situation;
    private static int currentNumberOfPlayers;
    public StartViewsManager(Label lbMode,Label lbOneName, Label lbError, Label lbFirstQuestion, TextField taOneName, Button btnGreenButton, Button btnRedButton) {
        this.lbMode=lbMode;
        this.lbError=lbError;
        this.lbOneName=lbOneName;
        this.lbFirstQuestion=lbFirstQuestion;
        this.taOneName=taOneName;
        this.btnGreenButton=btnGreenButton;
        this.btnRedButton=btnRedButton;
        setupListeners();
        players = new ArrayList<>();
    }

    private void setupListeners() {
        taOneName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onGreenButtonClicked();
            }
        });
    }

    public void onRedButtonAction() {
        if (situation == Situation.QUESTION) {
            setOfflineView();
        } else {
            currentNumberOfPlayers = 0;
            setQuestionView();
        }
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
        try {
            addPlayerName();
            setWaitingMode();
            NetworkManager.start();
        } catch (IllegalArgumentException e){
            setOnlineView();
            lbError.setVisible(true);
        }
        catch (Exception e) {
            ViewUtils.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            setQuestionView();
        }
    }

    private void addPlayerName() throws IllegalArgumentException {
        String playerName = taOneName.getText();
        if (!playerName.isBlank()) {
            players.add(PlayerFactory.createPlayer(playerName, NUM_FOLLOWERS_PER_PLAYER));
            currentNumberOfPlayers++;
        } else {
            throw new IllegalArgumentException("Name is Blank");
        }
    }

    private void startOfflineMode() {
        try{
            addPlayerName();
            if (currentNumberOfPlayers != NUM_PLAYERS) {
                setOfflineView();
            } else {
                initializeOfflineMode();
            }
        }catch (IllegalArgumentException e){
            lbError.setVisible(true);
        }
    }

    private void initializeOfflineMode() {
        initializeGame(null);
        startGameView(OFFLINE_SCREEN_SIZE);
    }

    private void startGameView(double size){
        StartViewController.closeView();
        try {
            Stage gameStage = new Stage();
            Main.setStage(gameStage);
            gameStage.setTitle("Carcassonne "+ (players.isEmpty()?"":players.get(0).getName()));
            gameStage.setWidth(size);
            Image icon = new Image(Objects.requireNonNull(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png")).toExternalForm());
            gameStage.getIcons().add(icon);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/hr/algebra/carcassonnegame2/views/gameView.fxml"));
            Parent root = loader.load();
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGameView(GameWorld game) {
        situation=Situation.UPDATED;
        GameController.setPlayer(players.get(0));
        if(game == null){
            players.add(PlayerFactory.createDefaultPlayer());
        }
        initializeGame(game);
        startGameView(ONLINE_SCREEN_SIZE);
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
        IS_GAME_MODE_ONLINE=true;
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
        IS_GAME_MODE_ONLINE=false;
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


    private static void initializeGame(GameWorld game) {
        try {
            game = game ==null ? GameFactory.createGame(): game;
            for (Player player: players) {
                game.addPlayer(player);
            }
            GameController.setGame(game);
        } catch (IllegalArgumentException ignored) {
            StartViewController.closeView();
        }
    }

    public boolean isViewUpdated() {
        return situation != Situation.UPDATED;
    }
}
