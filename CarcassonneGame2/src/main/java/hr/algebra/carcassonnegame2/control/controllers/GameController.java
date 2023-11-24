package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.factories.GameFactory;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.player.Player;
import hr.algebra.carcassonnegame2.utils.DocumentationUtils;
import hr.algebra.carcassonnegame2.views.GameViewsManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.awt.Desktop;

public class GameController implements Initializable {
    private static final String jsonFileNameCity = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-city-test.json";
    private static final String jsonFileNameAll = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    private static final String jsonFileNameMonastery = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-monastery-test.json";
    private static final String jsonFileNamePath = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-path-test.json";
    private static final String jsonFileName = jsonFileNamePath;
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
    private Game game;
    private GameViewsManager gameViewsManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAux();
    }

    private void initializeAux() {
        initializeGame();
        gameViewsManager = new GameViewsManager(game, getPlayersScoreboards());
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

    private void updatePlayersInfo() {
        gameViewsManager.updateScoreboard();
    }

    public void newGameAction() {
        initializeAux();
    }

    public void rotateTileAction() {
        gameViewsManager.resetFollowerPosition();
        game.rotateNextTile();
        gameViewsManager.paintNextTile(gpNextTile);
    }

    public void closeThisView() {
        Stage stage = (Stage) gpGameBoard.getScene().getWindow();
        stage.close();
    }

    public void putTileAction() {
        if(gameViewsManager.getSelectedPosition()!=null){
            try {
                if(gameViewsManager.getFollowerPosition()!=null){
                    game.getNextTile().setFollower(-1, gameViewsManager.getFollowerPosition());
                    gameViewsManager.resetFollowerPosition();
                }
                if(game.putTile(gameViewsManager.getSelectedPosition())){
                    int winner = game.update();
                    if(winner!=-1){
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
        gameViewsManager.paintGameBoard(gpGameBoard);
        gameViewsManager.paintNextTile(gpNextTile);
        updatePlayersInfo();
    }

    public void getPlayerTurnAction() {
        GameViewsManager.sendAlert("Player Turn", game.getNextPlayerInfo(), Alert.AlertType.INFORMATION);
    }

    public void remainingTilesAction() {
        GameViewsManager.sendAlert("Remaining Tiles", "Left: " + game.getRemainingTiles() + " tiles and " + game.getRemainingTypes() + " different types", Alert.AlertType.INFORMATION);
    }

    public void removeFollowerAction() {
        gameViewsManager.resetFollowerPosition();
        game.getNextTile().removeFollower();
        gameViewsManager.paintNextTile(gpNextTile);
    }

    public void changeTileAction() {
        game.changeNextTile();
        gameViewsManager.paintNextTile(gpNextTile);
        updatePlayersInfo();
    }

    public void finishGameAction() {
        int winner = game.finishGame();
        endActions(winner);
    }

    private void endActions(int winner) {
        if(winner==6){
            GameViewsManager.sendAlert("Tie", "Bad TIE", Alert.AlertType.ERROR);
        }else{
            GameViewsManager.sendAlert("Winner", "The winner is....\n"+"With " + game.getPlayersInfo().get(winner).getPoints() + " points...\n" + game.getPlayersInfo().get(winner).getName(), Alert.AlertType.INFORMATION);
        }
        closeThisView();
    }

    public void onLoadGame() {
        try {
            FileInputStream fileInputStream = new FileInputStream(saveFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            game = (Game) objectInputStream.readObject();
            objectInputStream.close();
            updateView();
            GameViewsManager.sendAlert("Load", "Successfully Loaded Game Status", Alert.AlertType.INFORMATION);
        }catch (IOException | ClassNotFoundException e){
            GameViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onSaveGame() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            GameViewsManager.sendAlert("Save", "Successfully Saved Game Status", Alert.AlertType.INFORMATION);
        }catch (IOException e){
            GameViewsManager.sendAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onGenerateDocumentation(ActionEvent actionEvent) {
        try {
            DocumentationUtils.generateDocumentation();
            GameViewsManager.sendAlert("Success", "Successfully created the documentation", Alert.AlertType.INFORMATION);
        }catch (IllegalArgumentException e){
            GameViewsManager.sendAlert("Error", "Something went wrong", Alert.AlertType.ERROR);
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
}
