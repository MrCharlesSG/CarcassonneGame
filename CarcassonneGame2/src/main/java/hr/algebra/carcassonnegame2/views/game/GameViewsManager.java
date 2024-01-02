package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.Main;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.utils.ViewUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;

public final class GameViewsManager {

    private final HashMap<String, GameView> gameViews;
    private final ChatView chatView;

    public GameViewsManager(GameWorld game,
                            List<ScoreboardUnit> playersScoreboards,
                            RemoteChatService chat,
                            GridPane gpNextTile,
                            GridPane gpGameBoard,
                            TextArea taChat,
                            Label lbLastMove,
                            Label lbPlayerTurns){
        GameView.updateGame(game);
        chatView = new ChatView(chat, taChat);
        gameViews = new HashMap<>() {{
            put(NextTileView.getKeyName(), new NextTileView(gpNextTile));
            put(ScoreboardView.getKeyName(), new ScoreboardView(playersScoreboards));
            put(GameBoardView.getKeyName(), new GameBoardView(gpGameBoard));
            put(PlayerTurnView.getKeyName(), new PlayerTurnView(lbPlayerTurns));
        }};
    }

    public static void sendAlert(String title, String message, Alert.AlertType alertType) {
        ViewUtils.sendAlert(title, message, alertType);
    }

    public void updateGame(GameWorld game){
        GameView.updateGame(game);
        updateView();
    }

    public void updateNextTile(){
        gameViews.get(NextTileView.getKeyName()).updateView();
    }
    public Position getFollowerPosition(){
        return ((NextTileView) gameViews.get(NextTileView.getKeyName())).getFollowerPosition();
    }
    public Position getSelectedPosition(){
        return ((GameBoardView) gameViews.get(GameBoardView.getKeyName())).getSelecetedPosition();
    }
    public void resetFollowerPosition() {
        ((NextTileView) gameViews.get(NextTileView.getKeyName())).resetFollowerPosition();
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

    public void closeView() {
        Main.getStage().close();
    }

    public void onPlayersChanged() {
        gameViews.get(ScoreboardView.getKeyName()).initialize();
    }
}
