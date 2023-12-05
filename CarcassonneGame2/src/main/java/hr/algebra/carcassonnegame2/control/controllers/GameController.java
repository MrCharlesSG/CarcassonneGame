package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.factories.GameFactory;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import hr.algebra.carcassonnegame2.network.NetworkManager;
import hr.algebra.carcassonnegame2.utils.DocumentationUtils;
import hr.algebra.carcassonnegame2.views.ViewsManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static hr.algebra.carcassonnegame2.network.NetworkManager.sendGame;

public class GameController implements Initializable {
    private static final String jsonFileNameCity = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-city-test.json";
    private static final String jsonFileNameAll = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    private static final String jsonFileNameMonastery = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-monastery-test.json";
    private static final String jsonFileNamePath = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-path-test.json";
    private static final String jsonFileName = jsonFileNameAll;
    private static final int numberOfFollowersPerPlayer = 7;
    private static final String saveFileName = "data.ser";
    private static final String[] playersNames = new String[]{"CLIENT", "SERVER"};
    public GridPane gpNextTile;
    public MenuItem miNewGame;
    public MenuItem miLoadGame;
    public GridPane gpGameBoard;
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
    private static ViewsManager gameViewsManager;
    private static PlayerType player;

    public static void setChat(RemoteChatService skeleton) {
        chat=skeleton;
        System.out.println("Chat iniciado");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAux();
    }

    private void initializeAux() {
        initializeGame();
        if(!player.isServer()){
            sendGame(player, game );
        }
        NetworkManager.startClientRmi();
        initializeManager();
        setupListeners();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> refreshChatTextArea()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

    }

    private void setupListeners() {
        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessageAction(null);
            }
        });
    }

    private void refreshChatTextArea() {
        gameViewsManager.updateChat();
    }

    private void initializeManager() {
        gameViewsManager = new ViewsManager(game, getPlayersScoreboards(), chat, gpNextTile, gpGameBoard, taChat, lbPlayerTurn);
        updateView();
    }

    public List<ScoreboardUnit> getPlayersScoreboards(){
        List<ScoreboardUnit> list = new ArrayList<>();
        list.add(new ScoreboardUnit(lbPlayer1Name, lbPlayer1Pts, lbPlayer1Followers, spherePlayer1));
        list.add(new ScoreboardUnit(lbPlayer2Name, lbPlayer2Pts, lbPlayer2Followers, spherePlayer2));
        return list;
    }

    private void initializeGame(){
        try{
           game = GameFactory.createGame(playersNames, jsonFileName, numberOfFollowersPerPlayer);
        }catch (IllegalArgumentException ignored) { //Cerrar App
            closeThisView();
        }
    }

    public void newGameAction() {
        initializeAux();
    }

    public void rotateTileAction() {
        if(isPlayerTurns()){
            gameViewsManager.resetFollowerPosition();
            game.rotateNextTile();
            gameViewsManager.updateNextTile();
        }
    }

    public void closeThisView() {
        Stage stage = (Stage) gpGameBoard.getScene().getWindow();
        stage.close();
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
                    sendGame(player, game);
                    if(winner!=null){
                        endActions(winner);
                    }else{
                        updateView();
                    }
                }
            }catch (IllegalArgumentException e){
                ViewsManager.sendAlert("Something went wrong", e.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            ViewsManager.sendAlert("Non Position Selected", "Select a position in grid is required", Alert.AlertType.WARNING);
        }
    }

    private void updateView() {
        enableDisableView();
        gameViewsManager.updateView();
    }

    public void getPlayerTurnAction() {
        ViewsManager.sendAlert("Player Turn", game.getNextPlayerInfo(), Alert.AlertType.INFORMATION);
    }

    public void remainingTilesAction() {
        ViewsManager.sendAlert("Remaining Tiles", "Left: " + game.getRemainingTiles() + " tiles and " + game.getRemainingTypes() + " different types", Alert.AlertType.INFORMATION);
    }

    public void removeFollowerAction() {
        gameViewsManager.resetFollowerPosition();
        game.getNextTile().removeFollower();
        gameViewsManager.updateNextTile();
    }

    public void changeTileAction() {
        if(isPlayerTurns()) {
            game.changeNextTile();
            sendGame(player, game);
            enableDisableView();
        }
    }

    public void finishGameAction() {
        List<Integer> winner = game.finishGame();
        endActions(winner);
    }

    private void endActions(List<Integer> winner) {
        if(winner.size()!=1){
            ViewsManager.sendAlert("Tie", "Bad TIE", Alert.AlertType.ERROR);
        }else{
            ViewsManager.sendAlert("Winner", "The winner is....\n"+"With " + game.getPlayersInfo().get(winner.get(0)).getPoints() + " points...\n" + game.getPlayersInfo().get(winner.get(0)).getName(), Alert.AlertType.INFORMATION);
        }
        closeThisView();
    }

    public void onLoadGame() {
        try {
            FileInputStream fileInputStream = new FileInputStream(saveFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            game = (Game) objectInputStream.readObject();
            gameViewsManager.updateGame(game);
            objectInputStream.close();
            updateView();
            ViewsManager.sendAlert("Load", "Successfully Loaded Game Status", Alert.AlertType.INFORMATION);
        }catch (IOException | ClassNotFoundException e){
            ViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onSaveGame() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            ViewsManager.sendAlert("Save", "Successfully Saved Game Status", Alert.AlertType.INFORMATION);
        }catch (IOException e){
            ViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onGenerateDocumentation(ActionEvent actionEvent) {
        try {
            DocumentationUtils.generateDocumentation();
            ViewsManager.sendAlert("Success", "Successfully created the documentation", Alert.AlertType.INFORMATION);
        }catch (IllegalArgumentException e){
            ViewsManager.sendAlert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }
    }

    public void onReadDocumentation(ActionEvent actionEvent) {
            try {
                File htmlFile = new File("project-documentation/index.html"); // Reemplaza con la ruta de tu archivo HTML
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    private static boolean isPlayerTurns(){
        return player == PlayerType.valueOf(game.getCurrentPlayer().getName());
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        gameViewsManager.sendMessage(new Message(tfMessage.getText(), player));
        tfMessage.clear();
    }

    public static void restoreGame(GameWorld game) {
        System.out.println("Game board received from the client!" + game.getNextPlayerInfo());
        GameController.game=game;
        enableDisableView();
        gameViewsManager.updateGame(GameController.game);
    }

    public static void enableDisableView(){
        if(isPlayerTurns()){
            gameViewsManager.enableViews();
        }
        else{
            gameViewsManager.disableView();
        }
    }

    public static void setPlayer(PlayerType player){
        GameController.player = player;
    }
}
