package hr.algebra.carcassonnegame2.thread;

import hr.algebra.carcassonnegame2.configuration.GameConfiguration;
import hr.algebra.carcassonnegame2.control.controllers.GameController;
import hr.algebra.carcassonnegame2.model.GameMove;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.utils.GameMoveUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.IS_GAME_MODE_ONLINE;

public class LatestMoveThread extends GameMoveThread implements Runnable{
    private final Label lbLastMove;

    public LatestMoveThread(Label lbLastMove) {
        this.lbLastMove=lbLastMove;
    }

    @Override
    public void run() {
        while (!IS_GAME_MODE_ONLINE){
            Platform.runLater(() -> {
                lbLastMove.setText(getTheLastMove().toString());
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
