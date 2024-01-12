package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileElementValue;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class ViewUtils {
    private ViewUtils(){}

    public static void sendAlert(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void representTileInGridPane(Tile tile, GridPane gridPane) {
        gridPane.setGridLinesVisible(true);
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                StackPane pane = new StackPane();

                if(tile.isFollowerInPosition(new Position(col, row))){
                    Circle circle = new Circle(2);
                    circle.setStyle("-fx-fill: " + tile.getPlayerFollower().getStyle());
                    pane.getChildren().add(circle);
                    StackPane.setAlignment(circle, Pos.CENTER);
                }
                pane.setStyle(tile.getValuePosition(new Position(col, row)).getStyle());
                gridPane.add(pane, col, row);
            }
        }
    }

    public static void resizeGridPane(GridPane gridPane, int numberColRow, boolean isNextTile, double heightCol, double widthCol){
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        if(isNextTile){
            rowConstraints.setVgrow(Priority.ALWAYS);
            columnConstraints.setHgrow(Priority.ALWAYS);
        }else{
            rowConstraints.setMaxHeight(heightCol* 5);
            columnConstraints.setMinWidth(widthCol* 5);
            rowConstraints.setMinHeight(heightCol* 5);
            columnConstraints.setMinWidth(widthCol* 5);
        }
        for (int i = 0; i < numberColRow; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }
}
