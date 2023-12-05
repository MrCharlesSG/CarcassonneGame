package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.List;

public final class ViewsManager {
    private final ScoreboardView scoreBoardView;
    private final NextTileView nextTileView;
    private final GameBoardView gameBoardView;

    private final List<GameView> gameViews;
    private final ChatView chatView;
    private final PlayerTurnView playerTurnsView;
    private boolean viewsEnable =true;
    public ViewsManager(GameWorld game, List<ScoreboardUnit> playersScoreboards, RemoteChatService chat, GridPane gpNextTile, GridPane gpGameBoard, TextArea taChat, Label lbPlayerTurns){
        GameView.updateGame(game);
        scoreBoardView = new ScoreboardView(playersScoreboards);
        nextTileView = new NextTileView(gpNextTile);
        gameBoardView = new GameBoardView(gpGameBoard);
        chatView = new ChatView(chat, taChat);
        playerTurnsView = new PlayerTurnView(lbPlayerTurns);
        gameViews = List.of(scoreBoardView, nextTileView, gameBoardView, playerTurnsView);
    }

    public void updateGame(GameWorld game){
        GameView.updateGame(game);
        updateView();
    }

    public void updateNextTile(){
        nextTileView.updateView();
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

    public void paintGameBoard() {
        gameBoardView.updateView();
    }

    public void updateScoreboard() {
        scoreBoardView.updateView();
    }

    public void updateChat(){
        chatView.updateChat();
    }

    public void updateView() {
        for (GameView gameView: gameViews)
            gameView.updateView();
    }

    public void disableView() {
        GameView.disableView();
        updateView();
    }

    public void enableViews() {
        GameView.enableView();
        updateView();
    }

    public void sendMessage(Message message) {
        chatView.sendMessage(message);
    }
}
