package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatServiceImpl;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.*;

public final class ViewsManager {
    private final ScoreboardView scoreBoardView;
    private final NextTileView nextTileView;
    private final GameBoardView gameBoardView;

    private final List<GameView> gameViews;
    private final ChatView chatView;
    private boolean viewsEnable =true;
    public ViewsManager(GameWorld game, List<ScoreboardUnit> playersScoreboards, RemoteChatServiceImpl chat, GridPane gpNextTile, GridPane gpGameBoard){
        scoreBoardView = new ScoreboardView(game, playersScoreboards);
        nextTileView = new NextTileView(game, gpNextTile);
        gameBoardView = new GameBoardView(game, gpGameBoard);
        chatView = new ChatView(chat);
        gameViews = List.of(scoreBoardView, nextTileView, gameBoardView);
    }

    public void updateGame(GameWorld game){
        for (GameView gameView: gameViews)
            gameView.updateGame(game);
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

    public void updateChat(TextArea textArea){
        chatView.updateChat(textArea);
    }

    public void updateView() {
        for (GameView gameView: gameViews)
            gameView.updateView();
    }

    public void disableView() {
        if(viewsEnable){
            viewsEnable=false;
            for (GameView gameView: gameViews)
                gameView.disableView();
        }
    }

    public void enableViews() {
        if(!viewsEnable){
            viewsEnable=true;
            for (GameView gameView: gameViews)
                gameView.enableView();
        }
    }
}
