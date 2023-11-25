package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.GameWorld;
import hr.algebra.carcassonnegame2.model.player.Player;

import java.util.List;

public class ScoreboardView extends GameView{
    private final List<ScoreboardUnit> playersScoreboards;

    public ScoreboardView(GameWorld game, List<ScoreboardUnit> playersScoreboards) {
        super(game);
        this.playersScoreboards=playersScoreboards;
        initPlayersInfo();
    }

    private void initPlayersInfo() {
        List<Player> list=game.getPlayersInfo();
        for (int i = 0; i < playersScoreboards.size(); i++) {
            playersScoreboards.get(i).initializePlayerInfo(list.get(i));
        }
    }

    public void updateScoreboard() {
        List<Player> list = game.getPlayersInfo();
        int i=0;
        for(Player player: list){
            playersScoreboards.get(i).updateScoreBoard(player.getPoints(), player.getNumberOfFollowers());
            i++;
        }
    }
}
