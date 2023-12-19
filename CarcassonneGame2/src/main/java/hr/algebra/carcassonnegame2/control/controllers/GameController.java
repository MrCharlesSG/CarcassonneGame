package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.network.NetworkManager;
import hr.algebra.carcassonnegame2.utils.DocumentationUtils;
import hr.algebra.carcassonnegame2.views.game.GameViewsManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.IS_GAME_MODE_ONLINE;
import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.SAVE_FILE_NAME;

public class GameController implements Initializable {
    public GridPane gpNextTile;
    public GridPane gpGameBoard;
    public MenuItem miLoadGame;
    public MenuItem miRotateTile;
    public MenuItem miPutTile;
    public Label lbPlayer1Name;
    public Label lbPlayer1Pts;
    public Label lbPlayer1Followers;
    public Label lbPlayer2Name;
    public Label lbPlayer2Followers;
    public Label lbPlayer2Pts;
    public Circle spherePlayer1;
    public Circle spherePlayer2;
    public TextArea taChat;
    public TextField tfMessage;
    public Button btnSendMessage;
    public Label lbPlayerTurn;
    private static GameWorld game;
    private static RemoteChatService chat;
    private static GameViewsManager gameViewsManager;
    private static Player player;

    public static void setGame(GameWorld game) {
        GameController.game = game;
        if(IS_GAME_MODE_ONLINE){
            NetworkManager.sendGame(player.getType(), game);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAux();
    }

    private void initializeAux() {
        if(IS_GAME_MODE_ONLINE){
            NetworkManager.startRmiClient();
            setupTimeline();
            setupListeners();
        }
        initializeManager();
    }

    private void initializeManager() {
        gameViewsManager = new GameViewsManager(game, getPlayersScoreboards(), chat, gpNextTile, gpGameBoard, taChat, lbPlayerTurn);
        if(chat==null){
            disableChat();
        }
        updateView();
    }

    private void disableChat() {
        btnSendMessage.setVisible(false);
        tfMessage.setVisible(false);
        taChat.setVisible(false);
    }

    private void setupListeners() {
        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessageAction();
            }
        });
    }

    private void setupTimeline() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> refreshChatTextArea()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    private void refreshChatTextArea() {
        gameViewsManager.updateChat();
    }

    public List<ScoreboardUnit> getPlayersScoreboards(){
        List<ScoreboardUnit> list = new ArrayList<>();
        list.add(new ScoreboardUnit(lbPlayer1Name, lbPlayer1Pts, lbPlayer1Followers, spherePlayer1));
        list.add(new ScoreboardUnit(lbPlayer2Name, lbPlayer2Pts, lbPlayer2Followers, spherePlayer2));
        return list;
    }

    public void rotateTileAction() {
        if(isPlayerTurns()){
            gameViewsManager.resetFollowerPosition();
            game.rotateNextTile();
            gameViewsManager.updateNextTile();
        }
    }

    public static void closeThisView() {
        gameViewsManager.closeView();
    }

    public void putTileAction() {
        if(gameViewsManager.getSelectedPosition()!=null ){
            try {
                if(gameViewsManager.getFollowerPosition()!=null){
                    game.getNextTile().setFollower(-1, gameViewsManager.getFollowerPosition());
                    gameViewsManager.resetFollowerPosition();
                }
                if(game.putTile(gameViewsManager.getSelectedPosition())){
                    List<Integer> winner = game.update();
                    sendGame(game);
                    if(winner!=null){
                        endActions(winner);
                    }else{
                        updateView();
                    }
                }
            }catch (IllegalArgumentException e){
                GameViewsManager.sendAlert("Something went wrong", e.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            GameViewsManager.sendAlert("Non Position Selected", "Select a position in grid is required", Alert.AlertType.WARNING);
        }
    }

    private void updateView() {
        enableDisableView();
        gameViewsManager.updateView();
    }

    public void getPlayerTurnAction() {
        GameViewsManager.sendAlert("Player Turn", game.getNextPlayerInfo(), Alert.AlertType.INFORMATION);
    }

    public void remainingTilesAction() {
        GameViewsManager.sendAlert("Remaining Tiles", "Left: " + game.getRemainingTiles() + " tiles and " + game.getRemainingTypes() + " different types.", Alert.AlertType.INFORMATION);
    }

    public void removeFollowerAction() {
        gameViewsManager.resetFollowerPosition();
        game.getNextTile().removeFollower();
        gameViewsManager.updateNextTile();
    }

    public void changeTileAction() {
        if(isPlayerTurns()) {
            game.changeNextTile();
            sendGame(game);
            enableDisableView();
        }
    }

    public void finishGameAction() {
        List<Integer> winner = game.finishGame();
        endActions(winner);
    }

    private static void endActions(List<Integer> winner) {
        if(winner.size()!=1){
            GameViewsManager.sendAlert("Tie", "Bad TIE", Alert.AlertType.ERROR);
        }else{
            GameViewsManager.sendAlert("Winner", "The winner is....\n"+"With " + game.getPlayersInfo().get(winner.get(0)).getPoints() + " points...\n" + game.getPlayersInfo().get(winner.get(0)).getName(), Alert.AlertType.INFORMATION);
        }
        closeThisView();
    }

    public void onLoadGame() {
        if(player.isServer()){
            try {
                FileInputStream fileInputStream = new FileInputStream(SAVE_FILE_NAME);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                game = (Game) objectInputStream.readObject();
                gameViewsManager.updateGame(game);
                objectInputStream.close();
                updateView();
                sendGame(game);
                GameViewsManager.sendAlert("Load", "Successfully Loaded Game Status", Alert.AlertType.INFORMATION);
            }catch (IOException | ClassNotFoundException e){
                GameViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            GameViewsManager.sendAlert("Not Access", "As a CLIENT you have no access to this functionality", Alert.AlertType.WARNING);
        }
    }

    private void sendGame(GameWorld game) {
        if(IS_GAME_MODE_ONLINE){
            NetworkManager.sendGame(player.getType(), game);
        }
    }

    public void onSaveGame() {
        if(player.isServer()){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(SAVE_FILE_NAME);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(game);
                objectOutputStream.close();
                GameViewsManager.sendAlert("Save", "Successfully Saved Game Status", Alert.AlertType.INFORMATION);
            }catch (IOException e){
                GameViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            GameViewsManager.sendAlert("Not Access", "As a CLIENT you have no access to this functionality", Alert.AlertType.WARNING);
        }
    }

    public void onGenerateDocumentation() {
        try {
            DocumentationUtils.generateDocumentation();
            GameViewsManager.sendAlert("Success", "Successfully created the documentation", Alert.AlertType.INFORMATION);
        }catch (IllegalArgumentException e){
            GameViewsManager.sendAlert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }
    }

    public void onReadDocumentation() {
            try {
                File htmlFile = new File("project-documentation/index.html"); // Reemplaza con la ruta de tu archivo HTML
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                GameViewsManager.sendAlert("Error", "Something went wrong", Alert.AlertType.ERROR);
            }
    }

    private static boolean isPlayerTurns(){
        return !IS_GAME_MODE_ONLINE || player.getType() == game.getCurrentPlayer().getType();
    }

    public void sendMessageAction() {
        if(!tfMessage.getText().isBlank()) {
            gameViewsManager.sendMessage(new Message(tfMessage.getText(), player));
            tfMessage.clear();
        }
    }

    public static void restoreGame(GameWorld game) {
        System.out.println("Game board received from the client!" + game.getNextPlayerInfo());
        GameController.game=game;
        enableDisableView();
        gameViewsManager.updateGame(GameController.game);
        List<Integer> winner = game.isFinished();
        if(winner!=null){
            endActions(winner);
        }
    }

    public static void enableDisableView(){
        if(isPlayerTurns()){
            gameViewsManager.enableViews();
        }
        else{
            gameViewsManager.disableView();
        }
    }

    public static void setPlayer(Player player){
        GameController.player = player;
    }

    public static void setChat(RemoteChatService chat) {
        GameController.chat=chat;
    }
}
