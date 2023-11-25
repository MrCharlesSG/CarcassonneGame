package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.util.List;

public class GameViewsManager {
    private final ScoreboardView scoreBoardView;
    private final NextTileView nextTileView;
    private final GameBoardView gameBoardView;
    public GameViewsManager(GameWorld game, List<ScoreboardUnit> playersScoreboards){
        scoreBoardView = new ScoreboardView(game, playersScoreboards);
        nextTileView = new NextTileView(game);
        gameBoardView = new GameBoardView(game);
    }

    public void updateGame(GameWorld game){
        scoreBoardView.updateGame(game);
        nextTileView.updateGame(game);
        gameBoardView.updateGame(game);
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
