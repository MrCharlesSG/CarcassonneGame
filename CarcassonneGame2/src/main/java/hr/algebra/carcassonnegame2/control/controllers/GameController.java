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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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
    public MenuItem miPutFollower;
    public MenuItem miPutTile;
    private ObservableList<Player> players;
    public TableColumn<Player, String> tcPlayersName;
    public TableColumn<Player, Integer> tcPlayersPoints;
    private Position selectedPosition;

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
        representTileInGridPane(Game.INSTANCE.getNextTile(), gpNextTile);
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
                    btnTile.setOnAction((actionEvent -> selectTilePosition(actionEvent, new Position(finalCol, finalRow))));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    public void selectTilePosition(ActionEvent actionEvent, Position point) {
        this.selectedPosition=point;
    }

    private void representTileInGridPane(Tile tile, GridPane gridPane) {
        gridPane.setGridLinesVisible(true);
        TileElementValue[][] tileRepresentation = tile.getRepresentation();

        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                Pane pane = new Pane();
                if(tile.isFollowerInPosition(new Position(col, row))){
                    Label text = new Label("o");
                    text.layoutXProperty().bind(pane.widthProperty().subtract(text.widthProperty()).divide(2));
                    text.layoutYProperty().bind(pane.heightProperty().subtract(text.heightProperty()).divide(2));
                    pane.getChildren().add(text);
                }
                pane.setStyle(tile.getValuePosition(new Position(col, row)).getStyle());
                gridPane.add(pane, col, row);
            }
        }
    }

    public void newGameAction(ActionEvent actionEvent) {
        closeThisView();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/startView.fxml"));
            Parent root = loader.load();

            Stage gameStage = new Stage();
            gameStage.setTitle("New Carcassonne Game");
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putFollowerAction(ActionEvent actionEvent) {
        if(Game.INSTANCE.canPlayerPutAFollower()) {
            closeThisView();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/putFollowerView.fxml"));
                Parent root = loader.load();

                Stage gameStage = new Stage();
                gameStage.setTitle("Put Follower");
                gameStage.setScene(new Scene(root));
                gameStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            sendAlert("Put Follower", "You can not put more followers");
        }
    }

    public void rotateTileAction(ActionEvent actionEvent) {
        Game.INSTANCE.rotateNextTile();
        paintNextTile();
    }

    public void closeThisView() {
        Stage stage = (Stage) gpGameBoard.getScene().getWindow();
        stage.close();
    }

    public void putTileAction(ActionEvent actionEvent) {
        if(selectedPosition!=null){
            try {
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

    public void getPlayerTurnAction(ActionEvent actionEvent) {
        sendAlert("Player Turn", Game.INSTANCE.getNextPlayerInfo());
    }

    public void remainingTilesAction(ActionEvent actionEvent) {
        sendAlert("Remaining Tiles", "Left: " + Game.INSTANCE.getRemainingTiles() + " tiles and " + Game.INSTANCE.getRemainingTypes() + " different types");
    }

    public void sendAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeFollowerAction(ActionEvent actionEvent) {
        Game.INSTANCE.getNextTile().removeFollower();
        paintNextTile();
    }

    public void changeTileAction(ActionEvent actionEvent) {
        Game.INSTANCE.changeNextTile();
        paintNextTile();
        tvPlayers.refresh();
    }

    public void finishGameAction(ActionEvent actionEvent) {
        int winner = Game.INSTANCE.finishGame();
        endActions(winner);
    }

    private void endActions(int winner) {
        if(winner==6){
            sendAlert("Winner", "Bad TIE");
        }else{
            sendAlert("Winner", "The winner is....\n"+"With " + Game.INSTANCE.getPlayersInfo().get(winner).getPoints() + " points...\n" + Game.INSTANCE.getPlayersInfo().get(winner).getName());
        }
        closeThisView();
    }
}
