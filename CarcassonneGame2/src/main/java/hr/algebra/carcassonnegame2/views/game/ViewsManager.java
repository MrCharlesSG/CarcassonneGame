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

import java.util.HashMap;
import java.util.List;

public final class ViewsManager {

    private final HashMap<String, GameView> gameViews;
    private final ChatView chatView;
    public ViewsManager(GameWorld game, List<ScoreboardUnit> playersScoreboards, RemoteChatService chat, GridPane gpNextTile, GridPane gpGameBoard, TextArea taChat, Label lbPlayerTurns){
        GameView.updateGame(game);
        chatView = new ChatView(chat, taChat);
        gameViews = new HashMap<>() {{
            put("ScoreBoard", new ScoreboardView(playersScoreboards));
            put("NextTile", new NextTileView(gpNextTile));
            put("GameBoard", new GameBoardView(gpGameBoard));
            put("PlayerTurns", new PlayerTurnView(lbPlayerTurns));
        }};
    }

    public void updateGame(GameWorld game){
        GameView.updateGame(game);
        updateView();
    }

    public void updateNextTile(){
        gameViews.get("NextTile").updateView();
    }

    public static void sendAlert(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public Position getFollowerPosition(){
        return ((NextTileView) gameViews.get("NextTile")).getFollowerPosition();
    }
    public Position getSelectedPosition(){
        return ((GameBoardView) gameViews.get("GameBoard")).getSelecetedPosition();
    }
    public void resetFollowerPosition() {
        ((NextTileView) gameViews.get("NextTile")).resetFollowerPosition();
    }

    public void updateChat(){
        chatView.updateChat();
    }

    public void updateView() {
        for (String keys: gameViews.keySet())
            gameViews.get(keys).updateView();
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
