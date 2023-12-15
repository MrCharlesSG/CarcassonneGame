package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerImpl;
import hr.algebra.carcassonnegame2.model.player.PlayerType;

public class PlayerFactory {

    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#3b82f6","#f472b6"};

    public static Player createPlayer(String name, int numberFollowersPerPlayer, PlayerType playerType) {
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle(playerType), playerType);
    }

    private static String getStyle(PlayerType playerType){
        if(playerType.isServer()){
            return colours[0];
        }
        return colours[1];
    }
}
