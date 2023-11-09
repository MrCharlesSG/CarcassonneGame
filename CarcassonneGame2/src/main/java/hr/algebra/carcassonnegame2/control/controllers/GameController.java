package hr.algebra.carcassonnegame2.control.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.carcassonnegame2.factories.FactoryPlayer;
import hr.algebra.carcassonnegame2.factories.FactoryTile;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import hr.algebra.carcassonnegame2.utils.DocumentationUtils;
import hr.algebra.carcassonnegame2.views.GameViewsManager;
import hr.algebra.carcassonnegame2.utils.ReflectionUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private static final String jsonFileName = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAux();
    }

    private void initializeAux(){
        initializeGame();
        initPlayersInfo();
        updateView();
    }

    private void initializeGame(){
        try{
            //Create players
            List<Player> playerList = new ArrayList<>();
            for(String name: GameController.playersNames){
                playerList.add(FactoryPlayer.createPlayer(name, numberOfFollowersPerPlayer));
            }

            //Create list of tiles from json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFileName));
            int numberOfRemainingTiles = rootNode.get("total").asInt();
            JsonNode typesNode = rootNode.get("types");

            List<Tile> listOfTiles = new ArrayList<Tile>();
            List<Integer> listOfRemainType = new ArrayList<Integer>();

            if(typesNode.isArray()){
                for (JsonNode typeNode: typesNode){
                    listOfRemainType.add( typeNode.get("total").asInt());
                    Tile tile = FactoryTile.createTile(typeNode.get("data"), game);
                    if(tile != null){
                        listOfTiles.add(tile);
                    }
                }
                game = new Game(listOfTiles, playerList, numberOfRemainingTiles, listOfRemainType);
            }
        }catch (Exception ignored) { //Cerrar App
            closeThisView();
        }
    }


    private void paintGameBoard() {
        gpGameBoard.setGridLinesVisible(true);
        Tile[][] gameBoard = game.getGameBoard();
        GameViewsManager.resizeGridPane(gpGameBoard, gameBoard.length, false);
        GameViewsManager.completeBoard(gameBoard,gpGameBoard, game);
    }

    private void initPlayersInfo() {
        List<Player> list=game.getPlayersInfo();
        initPlayersInfoAux(list.get(0));
        initPlayersInfoAux(list.get(1));
        updatePlayersInfo();
    }

    private void initPlayersInfoAux(Player player){
        if(game.getPlayersInfo().getFirst().getName().equals(player.getName())){
            lbPlayer1Name.setText(player.getName());
            spherePlayer1.setStyle("-fx-fill: "+ player.getTextColor());
        }else {
            lbPlayer2Name.setText(player.getName());
            spherePlayer2.setStyle("-fx-fill: "+ player.getTextColor());
        }
    }

    private void updatePlayersInfo(){
        List<Player> list = game.getPlayersInfo();
        for(Player player: list){
            updatePlayerInfo(player);
            updatePlayerInfo(player);
        }
    }

    private void updatePlayerInfo(Player player) {
        if(game.getPlayersInfo().getFirst().getName().equals(player.getName())){
            lbPlayer1Pts.setText(player.getPoints() + "");
            lbPlayer1Followers.setText(player.getNumberOfFollowers() + "");
        }else {
            lbPlayer2Pts.setText(player.getPoints() + "");
            lbPlayer2Followers.setText(player.getNumberOfFollowers() + "");
        }
    }

    public void newGameAction() {
        initializeAux();
    }

    public void rotateTileAction() {
        GameViewsManager.setFollowerPosition(null);
        game.rotateNextTile();
        GameViewsManager.paintNextTile(gpNextTile, game);
    }

    public void closeThisView() {
        Stage stage = (Stage) gpGameBoard.getScene().getWindow();
        stage.close();
    }

    public void putTileAction() {
        if(GameViewsManager.getSelectedPosition()!=null){
            try {
                if(GameViewsManager.getFollowerPosition()!=null){
                    game.getNextTile().setFollower(-1, GameViewsManager.getFollowerPosition());
                    GameViewsManager.setFollowerPosition(null);
                }
                if(game.putTile(GameViewsManager.getSelectedPosition())){
                    int winner = game.update();
                    //Game has finish
                    if(winner!=-1){
                        endActions(winner);
                    }else{
                        updateView();
                    }
                }
            }catch (IllegalArgumentException e){
               GameViewsManager.sendAlert("Something went wrong", e.getMessage());
            }
        }else{
            GameViewsManager.sendAlert("Non Position Selected", "Select a position in grid is required");
        }
    }

    private void updateView() {
        paintGameBoard();
        GameViewsManager.paintNextTile(gpNextTile, game);
        updatePlayersInfo();
    }

    public void getPlayerTurnAction() {
        GameViewsManager.sendAlert("Player Turn", game.getNextPlayerInfo());
    }

    public void remainingTilesAction() {
        GameViewsManager.sendAlert("Remaining Tiles", "Left: " + game.getRemainingTiles() + " tiles and " + game.getRemainingTypes() + " different types");
    }

    public void removeFollowerAction() {
        GameViewsManager.setFollowerPosition(null);
        game.getNextTile().removeFollower();
        GameViewsManager.paintNextTile(gpNextTile, game);
    }

    public void changeTileAction() {
        game.changeNextTile();
        GameViewsManager.paintNextTile(gpNextTile, game);
        updatePlayersInfo();
    }

    public void finishGameAction() {
        int winner = game.finishGame();
        endActions(winner);
    }

    private void endActions(int winner) {
        if(winner==6){
            GameViewsManager.sendAlert("Tie", "Bad TIE");
        }else{
            GameViewsManager.sendAlert("Winner", "The winner is....\n"+"With " + game.getPlayersInfo().get(winner).getPoints() + " points...\n" + game.getPlayersInfo().get(winner).getName());
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
            GameViewsManager.sendAlert("Load", "Successfully Loaded Game Status");
        }catch (IOException | ClassNotFoundException e){
            GameViewsManager.sendAlert("Error", e.getMessage());
        }
    }

    public void onSaveGame() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            GameViewsManager.sendAlert("Save", "Successfully Saved Game Status");
        }catch (IOException e){
            GameViewsManager.sendAlert("Error", e.getMessage());
        }
    }

    public void onGenerateDocumentation(ActionEvent actionEvent) {
        try {
            DocumentationUtils.generateDocumentation();
            GameViewsManager.sendAlert("Success", "Successfully created the documentation");
        }catch (IllegalArgumentException e){
            GameViewsManager.sendAlert("Error", "Something went wrong");
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
