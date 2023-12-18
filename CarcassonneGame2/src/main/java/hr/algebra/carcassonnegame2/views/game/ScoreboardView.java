package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.player.Player;

import java.util.List;

final class ScoreboardView extends GameView{
    private final List<ScoreboardUnit> playersScoreboards;
    private final static String KEY_NAME="ScoreBoard";


    public ScoreboardView(List<ScoreboardUnit> playersScoreboards) {
        super();
        this.playersScoreboards=playersScoreboards;
        initialize();
    }

    @Override
    public void updateView() {
        updateScoreboard();
    }

    @Override
    public void initialize() {
        List<Player> list=game.getPlayersInfo();
        for (int i = 0; i < playersScoreboards.size(); i++) {
            playersScoreboards.get(i).initializePlayerInfo(list.get(i));
        }
    }

    private void updateScoreboard() {
        initialize();
        /*
        for(ScoreboardUnit scoreboardUnit: playersScoreboards){
            scoreboardUnit.updateScoreBoard();
        }

         */
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

}
