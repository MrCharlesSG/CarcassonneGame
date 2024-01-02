package hr.algebra.carcassonnegame2.thread;

import hr.algebra.carcassonnegame2.model.GameMove;
import hr.algebra.carcassonnegame2.utils.GameMoveUtils;

public class SaveMoveThread extends GameMoveThread implements Runnable{
    private GameMove gameMove;
    public SaveMoveThread(GameMove gameMove){
        this.gameMove=gameMove;
    }

    @Override
    public void run() {
        saveMove(gameMove);
    }
}
