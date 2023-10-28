package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PutFollowerController implements Initializable {
    public GridPane gpTile;

    private Position followerPosition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTileGrid();
    }

    private void initTileGrid() {
        gpTile.setGridLinesVisible(true);
        Tile tile = Game.INSTANCE.getNextTile();
        int numberColRow = Tile.NUM_COLS_TILE;
        resizeGridPane(gpTile,numberColRow);
        for (int row = 0; row < numberColRow; row++) {
            for (int col = 0; col < numberColRow; col++) {

                gpTile.add(getTileButton(tile, new Position(col, row)), col, row);
            }
        }
    }

    private boolean canPutFollowerInPosition(Position point) {
        return Game.INSTANCE.canPutFollowerInPosition(point);
    }

    private  Button getTileButton(Tile tile, Position point){
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setStyle(tile.getValuePosition(point).getStyle());
        btn.setOnAction((actionEvent -> selectPosition(point, btn)));
        return btn;
    }

    private void selectPosition(Position point, Button btn){
        if(canPutFollowerInPosition(point)) {
            btn.setText("O");
            if (followerPosition != null) {
                gpTile.add(getTileButton(Game.INSTANCE.getNextTile(), followerPosition), followerPosition.getCol(), followerPosition.getRow());
            }
            followerPosition = point;
        }
    }

    private void resizeGridPane(GridPane gridPane, int numberColRow){//Make an utils script
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        for (int i = 0; i < numberColRow; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    public void handleSubmit(ActionEvent actionEvent) {
        Game.INSTANCE.getNextTile().setFollower(-1,followerPosition);
        closeThisView();
    }

    public void handleCancel(ActionEvent actionEvent) {
        closeThisView();
    }

    public void closeThisView(){
        Stage stage = (Stage) gpTile.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/gameView.fxml"));
            Parent root = loader.load();

            Stage gameStage = new Stage();
            gameStage.setTitle("Game");
            gameStage.setScene(new Scene(root));
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
