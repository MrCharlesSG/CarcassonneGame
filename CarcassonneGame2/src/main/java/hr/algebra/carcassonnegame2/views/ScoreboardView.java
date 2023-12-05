package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.misc.ScoreboardUnit;
import hr.algebra.carcassonnegame2.model.player.Player;

import java.util.List;

final class ScoreboardView extends GameView{
    private final List<ScoreboardUnit> playersScoreboards;

    public ScoreboardView(List<ScoreboardUnit> playersScoreboards) {
        super();
        this.playersScoreboards=playersScoreboards;
        initPlayersInfo();
    }

    @Override
    public void updateView() {
        updateScoreboard();
    }

    private void initPlayersInfo() {
        List<Player> list=game.getPlayersInfo();
        for (int i = 0; i < playersScoreboards.size(); i++) {
            playersScoreboards.get(i).initializePlayerInfo(list.get(i));
        }
    }

    private void updateScoreboard() {
        List<Player> list = game.getPlayersInfo();
        int i=0;
        for(Player player: list){
            playersScoreboards.get(i).updateScoreBoard(player.getPoints(), player.getNumberOfFollowers());
            i++;
        }
    }
}
