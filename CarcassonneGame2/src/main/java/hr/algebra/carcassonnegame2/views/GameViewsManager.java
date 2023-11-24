package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.Game;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Objects;

public class GameViewsManager {
    private final ScoreboardView scoreBoardView;
    private final NextTileView nextTileView;
    private final GameBoardView gameBoardView;
    public GameViewsManager(Game game, List<ScoreboardUnit> playersScoreboards){
        scoreBoardView = new ScoreboardView(game, playersScoreboards);
        nextTileView = new NextTileView(game);
        gameBoardView = new GameBoardView(game);
    }

    public void paintNextTile(GridPane gpNextTile){
        nextTileView.paintNextTile(gpNextTile);
    }

    public static void sendAlert(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public Position getFollowerPosition(){
        return nextTileView.getFollowerPosition();
    }
    public Position getSelectedPosition(){
        return gameBoardView.getSelecetedPosition();
    }
    public void resetFollowerPosition() {
        nextTileView.resetFollowerPosition();
    }

    public void paintGameBoard(GridPane gpGameBoard) {
        gameBoardView.paintGameBoard(gpGameBoard);
    }

    public void updateScoreboard() {
        scoreBoardView.updateScoreboard();
    }
}
