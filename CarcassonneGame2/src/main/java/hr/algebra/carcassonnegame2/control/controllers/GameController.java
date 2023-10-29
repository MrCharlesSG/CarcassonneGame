package hr.algebra.carcassonnegame2.control.controllers;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

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
    private Position followerPosition;
    private Button buttonWithFollower;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPlayersInfo();
        paintGameBoard();
        paintNextTile();
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
        resizeGridPane(gpGameBoard, gameBoard.length, 20);
        completeBoard(gameBoard);
    }

    private void paintNextTile(){
        resizeGridPane(gpNextTile, Tile.NUM_COLS_TILE, -1);
        Tile tile = Game.INSTANCE.getNextTile();
        int numberColRow = Tile.NUM_COLS_TILE;
        for (int row = 0; row < numberColRow; row++) {
            for (int col = 0; col < numberColRow; col++) {
                gpNextTile.add(getTileButton(tile, new Position(col, row)), col, row);
            }
        }
    }

    private  Button getTileButton(Tile tile, Position point){
        Button btn = new Button();
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
            }
        }else{
            sendAlert("Put Follower", "You can not put more followers");
        }
    }

    private void resizeGridPane(GridPane gridPane, int numberColRow, double size){
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        if(size>0){
            rowConstraints.setPercentHeight(size);
            columnConstraints.setPercentWidth(size);
        }else{
            rowConstraints.setVgrow(Priority.ALWAYS);
            columnConstraints.setHgrow(Priority.ALWAYS);
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
                    resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, -1);
                    representTileInGridPane(tile, gpTile);
                    gpGameBoard.add(gpTile, colPos, rowPos);
                }else{
                    Button btnTile = new Button();
                    btnTile.setPrefWidth(Double.MAX_VALUE);
                    btnTile.setPrefHeight(Double.MAX_VALUE);
                    int finalCol = colPos;
                    int finalRow = rowPos;
                    btnTile.setOnAction((actionEvent -> selectTilePosition(new Position(finalCol, finalRow))));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    public void selectTilePosition(Position point) {
        this.selectedPosition=point;
    }

    private void representTileInGridPane(Tile tile, GridPane gridPane) {
        gridPane.setGridLinesVisible(true);
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                StackPane pane = new StackPane();

                if(tile.isFollowerInPosition(new Position(col, row))){
                    Circle circle = new Circle(2);
                    circle.setStyle(Game.INSTANCE.getFollowerInTileStyle(tile));
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
                        paintGameBoard();
                        paintNextTile();
                        tvPlayers.refresh();
                    }
                }
            }catch (IllegalArgumentException e){
               sendAlert("Something went wrong", e.getMessage());
            }
        }else{
            sendAlert("Non Position Selected", "Select a position in grid is required");
        }
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
