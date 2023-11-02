package hr.algebra.carcassonnegame2.control.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.carcassonnegame2.factories.FactoryPlayer;
import hr.algebra.carcassonnegame2.factories.FactoryTile;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import hr.algebra.carcassonnegame2.model.gameobjects.TileElementValue;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    public static int MAX_NUM_PLAYERS=5;
    private static final String jsonFileName = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    private static final int numberOfFollowersPerPlayer = 7;
    private static final double MIN_HEIGHT_COL = 10;
    private static final double MIN_WIDTH_COL = 10;
    public TableView<Player> tvPlayers;
    public TableColumn<Player, Integer> tcFollowers;
    public GridPane gpNextTile;
    public MenuItem miNewGame;
    public MenuItem miLoadGame;
    public GridPane gpGameBoard;
    public MenuItem miRotateTile;
    public MenuItem miPutTile;
    private ObservableList<Player> players;
    public TableColumn<Player, String> tcPlayersName;
    public TableColumn<Player, Integer> tcPlayersPoints;
    private Position selectedPosition;

    private Button selectedPositionButton;
    private Position followerPosition;
    private Button buttonWithFollower;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPlayersInfo();
        updateView();
    }

    private void initPlayersInfo() {
        initTableCells();
        players = FXCollections.observableArrayList(Game.INSTANCE.getPlayersInfo());
        showPlayers();
    }

    private void initTableCells() {
        tcPlayersName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPlayersPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        tcFollowers.setCellValueFactory(new PropertyValueFactory<>("numberOfFollowers"));
    }

    private void showPlayers() {
        tvPlayers.setItems(players);
    }

    private void paintGameBoard() {
        gpGameBoard.setGridLinesVisible(true);
        Tile[][] gameBoard = Game.INSTANCE.getGameBoard();
        resizeGridPane(gpGameBoard, gameBoard.length, false);
        completeBoard(gameBoard);
    }

    private void paintNextTile(){
        resizeGridPane(gpNextTile, Tile.NUM_COLS_TILE, true);
        Tile tile = Game.INSTANCE.getNextTile();
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
        if(Game.INSTANCE.canPlayerPutAFollower()) {
            if (followerPosition == null) {
                if (Game.INSTANCE.canPutFollowerInPosition(point)) {
                    btn.setText("o");
                    followerPosition = point;
                    buttonWithFollower = btn;
                }
            } else if (!point.equals(followerPosition)) {
                if (Game.INSTANCE.canPutFollowerInPosition(point)) {
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
        btn.setStyle("-fx-background-color: "+Game.INSTANCE.getCurrentPlayerStyle() + "; -fx-background-radius: 0;" );
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
                    circle.setStyle("-fx-fill: " + Game.INSTANCE.getFollowerInTileStyle(tile));
                    pane.getChildren().add(circle);
                    StackPane.setAlignment(circle, Pos.CENTER);
                }
                pane.setStyle(tile.getValuePosition(new Position(col, row)).getStyle());
                gridPane.add(pane, col, row);
            }
        }
    }

    public void newGameAction() {
        closeThisView();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/startView.fxml"));
            Parent root = loader.load();

            Stage gameStage = new Stage();
            gameStage.setTitle("New Carcassonne Game");
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            sendAlert("Something Went wrong", e.getMessage());
        }
    }

    public void rotateTileAction() {
        followerPosition=null;
        Game.INSTANCE.rotateNextTile();
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
                    Game.INSTANCE.getNextTile().setFollower(-1, followerPosition);
                    followerPosition=null;
                }
                if(Game.INSTANCE.putTile(selectedPosition)){
                    int winner = Game.INSTANCE.update();
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
        tvPlayers.refresh();
    }

    public void getPlayerTurnAction() {
        sendAlert("Player Turn", Game.INSTANCE.getNextPlayerInfo());
    }

    public void remainingTilesAction() {
        sendAlert("Remaining Tiles", "Left: " + Game.INSTANCE.getRemainingTiles() + " tiles and " + Game.INSTANCE.getRemainingTypes() + " different types");
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
        Game.INSTANCE.getNextTile().removeFollower();
        paintNextTile();
    }

    public void changeTileAction() {
        Game.INSTANCE.changeNextTile();
        paintNextTile();
        tvPlayers.refresh();
    }

    public void finishGameAction() {
        int winner = Game.INSTANCE.finishGame();
        endActions(winner);
    }

    private void endActions(int winner) {
        if(winner==6){
            sendAlert("Tie", "Bad TIE");
        }else{
            sendAlert("Winner", "The winner is....\n"+"With " + Game.INSTANCE.getPlayersInfo().get(winner).getPoints() + " points...\n" + Game.INSTANCE.getPlayersInfo().get(winner).getName());
        }
        closeThisView();
    }
}
