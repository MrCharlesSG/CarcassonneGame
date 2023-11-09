package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.*;

public class GameViewsManager {
    private static final double MIN_HEIGHT_COL = 10;
    private static final double MIN_WIDTH_COL = 10;


    private static Button selectedPositionButton;
    private static Position followerPosition;
    private static Button buttonWithFollower;
    private static Position selectedPosition;
    private GameViewsManager(){}

    public static void resizeGridPane(GridPane gridPane, int numberColRow, boolean isNextTile){
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

    public static void paintNextTile(GridPane gpNextTile, Game game){
        resizeGridPane(gpNextTile, Tile.NUM_COLS_TILE, true);
        Tile tile = game.getNextTile();
        int numberColRow = Tile.NUM_COLS_TILE;
        for (int row = 0; row < numberColRow; row++) {
            for (int col = 0; col < numberColRow; col++) {
                gpNextTile.add(getTileButton(tile, new Position(col, row), game), col, row);
            }
        }
    }

    private static Button getTileButton(Tile tile, Position point, Game game){
        Button btn = getButton(true);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setStyle(tile.getValuePosition(point).getStyle() + " -fx-background-radius: 0;" );
        btn.setOnAction((actionEvent -> selectPosition(point, btn, game)));
        return btn;
    }

    private static void selectPosition(Position point, Button btn, Game game){
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

    private static Button getButton(boolean isNextTile){
        Button btn = new Button();
        btn.setMinHeight(MIN_HEIGHT_COL*(isNextTile ? 0.20:5));
        btn.setMinWidth(MIN_WIDTH_COL*(isNextTile ? 0.20:5));
        return btn;
    }

    public static void sendAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void completeBoard(Tile[][] gameBoard, GridPane gpGameBoard, Game game) {
        for (int colPos = 0; colPos < gameBoard.length; colPos++) {
            for (int rowPos = 0; rowPos < gameBoard[colPos].length; rowPos++) {
                if(gameBoard[colPos][rowPos] != null){
                    Tile tile = gameBoard[colPos][rowPos];
                    GridPane gpTile = new GridPane();
                    resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, true);
                    representTileInGridPane(tile, gpTile, game);
                    gpGameBoard.add(gpTile, colPos, rowPos);
                }else{
                    Button btnTile = getButton(false);
                    int finalCol = colPos;
                    int finalRow = rowPos;
                    btnTile.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
                    btnTile.setOnAction(actionEvent -> selectTilePosition(new Position(finalCol, finalRow), btnTile,game));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    private static void selectTilePosition(Position point, Button btn , Game game) {
        btn.setStyle("-fx-background-color: "+game.getCurrentPlayer().getTextColor() + "; -fx-background-radius: 0;" );
        if(selectedPositionButton!=null && !selectedPosition.equals(point)){
            selectedPositionButton.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
        }
        selectedPosition=point;
        selectedPositionButton=btn;
    }

    private static void representTileInGridPane(Tile tile, GridPane gridPane, Game game) {
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
    public static Position getFollowerPosition(){
        return followerPosition;
    }
    public static Position getSelectedPosition(){
        return selectedPosition;
    }
    public static void setFollowerPosition(Position position) { followerPosition = position; }

}
