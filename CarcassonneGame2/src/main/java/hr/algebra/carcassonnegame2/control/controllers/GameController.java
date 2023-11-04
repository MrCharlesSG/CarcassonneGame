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
    private static final double MIN_HEIGHT_COL = 10;
    private static final double MIN_WIDTH_COL = 10;
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
    private Position selectedPosition;

    private Game game;
    private Button selectedPositionButton;
    private Position followerPosition;
    private Button buttonWithFollower;

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
        resizeGridPane(gpGameBoard, gameBoard.length, false);
        completeBoard(gameBoard);
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

    private void paintNextTile(){
        resizeGridPane(gpNextTile, Tile.NUM_COLS_TILE, true);
        Tile tile = game.getNextTile();
        int numberColRow = Tile.NUM_COLS_TILE;
        for (int row = 0; row < numberColRow; row++) {
            for (int col = 0; col < numberColRow; col++) {
                gpNextTile.add(getTileButton(tile, new Position(col, row)), col, row);
            }
        }
    }

    private  Button getTileButton(Tile tile, Position point){
        Button btn = getButton(true);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setStyle(tile.getValuePosition(point).getStyle() + " -fx-background-radius: 0;" );
        btn.setOnAction((actionEvent -> selectPosition(point, btn)));
        return btn;
    }

    private void selectPosition(Position point, Button btn){
        if(game.canPlayerPutAFollower()) {
            if (followerPosition == null) {
                if (game.canPutFollowerInPosition(point)) {
                    btn.setText("o");
                    followerPosition = point;
                    buttonWithFollower = btn;
                }
            } else if (!point.equals(followerPosition)) {
                if (game.canPutFollowerInPosition(point)) {
                    buttonWithFollower.setText("");
                    buttonWithFollower = btn;
                    buttonWithFollower.setText("o");
                    followerPosition = point;
                }
            } else {
                btn.setText("");
                followerPosition=null;
            }
        }else{
            sendAlert("Put Follower", "You can not put more followers");
        }
    }

    private void resizeGridPane(GridPane gridPane, int numberColRow, boolean isNextTile){
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        if(isNextTile){
            rowConstraints.setVgrow(Priority.ALWAYS);
            columnConstraints.setHgrow(Priority.ALWAYS);
        }else{
            rowConstraints.setMaxHeight(MIN_HEIGHT_COL* 5);
            columnConstraints.setMinWidth(MIN_WIDTH_COL* 5);
            rowConstraints.setMinHeight(MIN_HEIGHT_COL* 5);
            columnConstraints.setMinWidth(MIN_WIDTH_COL* 5);
        }
        for (int i = 0; i < numberColRow; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void completeBoard(Tile[][] gameBoard) {
        for (int colPos = 0; colPos < gameBoard.length; colPos++) {
            for (int rowPos = 0; rowPos < gameBoard[colPos].length; rowPos++) {
                if(gameBoard[colPos][rowPos] != null){
                    Tile tile = gameBoard[colPos][rowPos];
                    GridPane gpTile = new GridPane();
                    resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, true);
                    representTileInGridPane(tile, gpTile);
                    gpGameBoard.add(gpTile, colPos, rowPos);
                }else{
                    Button btnTile = getButton(false);
                    int finalCol = colPos;
                    int finalRow = rowPos;
                    btnTile.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
                    btnTile.setOnAction(actionEvent -> selectTilePosition(new Position(finalCol, finalRow), btnTile));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    private Button getButton(boolean isNextTile){
        Button btn = new Button();
        btn.setMinHeight(MIN_HEIGHT_COL*(isNextTile ? 0.20:5));
        btn.setMinWidth(MIN_WIDTH_COL*(isNextTile ? 0.20:5));
        return btn;
    }

    public void selectTilePosition(Position point, Button btn) {
        btn.setStyle("-fx-background-color: "+game.getCurrentPlayer().getTextColor() + "; -fx-background-radius: 0;" );
        if(selectedPositionButton!=null && !selectedPosition.equals(point)){
            selectedPositionButton.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
        }
        this.selectedPosition=point;
        selectedPositionButton=btn;
    }

    private void representTileInGridPane(Tile tile, GridPane gridPane) {
        gridPane.setGridLinesVisible(true);
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                StackPane pane = new StackPane();

                if(tile.isFollowerInPosition(new Position(col, row))){
                    Circle circle = new Circle(2);
                    circle.setStyle("-fx-fill: " + game.getFollowerInTileStyle(tile));
                    pane.getChildren().add(circle);
                    StackPane.setAlignment(circle, Pos.CENTER);
                }
                pane.setStyle(tile.getValuePosition(new Position(col, row)).getStyle());
                gridPane.add(pane, col, row);
            }
        }
    }

    public void newGameAction() {
        initializeAux();
    }

    public void rotateTileAction() {
        followerPosition=null;
        game.rotateNextTile();
        paintNextTile();
    }

    public void closeThisView() {
        Stage stage = (Stage) gpGameBoard.getScene().getWindow();
        stage.close();
    }

    public void putTileAction() {
        if(selectedPosition!=null){
            try {
                if(followerPosition!=null){
                    game.getNextTile().setFollower(-1, followerPosition);
                    followerPosition=null;
                }
                if(game.putTile(selectedPosition)){
                    int winner = game.update();
                    //Game has finish
                    if(winner!=-1){
                        endActions(winner);
                    }else{
                        updateView();
                    }
                }
            }catch (IllegalArgumentException e){
               sendAlert("Something went wrong", e.getMessage());
            }
        }else{
            sendAlert("Non Position Selected", "Select a position in grid is required");
        }
    }

    private void updateView() {
        paintGameBoard();
        paintNextTile();
        updatePlayersInfo();
    }

    public void getPlayerTurnAction() {
        sendAlert("Player Turn", game.getNextPlayerInfo());
    }

    public void remainingTilesAction() {
        sendAlert("Remaining Tiles", "Left: " + game.getRemainingTiles() + " tiles and " + game.getRemainingTypes() + " different types");
    }

    public void sendAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeFollowerAction() {
        this.followerPosition=null;
        game.getNextTile().removeFollower();
        paintNextTile();
    }

    public void changeTileAction() {
        game.changeNextTile();
        paintNextTile();
        updatePlayersInfo();
    }

    public void finishGameAction() {
        int winner = game.finishGame();
        endActions(winner);
    }

    private void endActions(int winner) {
        if(winner==6){
            sendAlert("Tie", "Bad TIE");
        }else{
            sendAlert("Winner", "The winner is....\n"+"With " + game.getPlayersInfo().get(winner).getPoints() + " points...\n" + game.getPlayersInfo().get(winner).getName());
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
            sendAlert("Load", "Successfully Loaded Game Status");
        }catch (IOException | ClassNotFoundException e){
            sendAlert("Error", e.getMessage());
        }
    }

    public void onSaveGame() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            sendAlert("Save", "Successfully Saved Game Status");
        }catch (IOException e){
            sendAlert("Error", e.getMessage());
        }
    }

    public void onGenerateDocumentation(ActionEvent actionEvent) {
        try {
            DocumentationUtils.generateDocumentation();
            sendAlert("Success", "Successfully created the documentation");
        }catch (IllegalArgumentException e){
            sendAlert("Error", "Something went wrong");
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
