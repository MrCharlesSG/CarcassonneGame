package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerImpl;
import hr.algebra.carcassonnegame2.model.player.PlayerType;

public class PlayerFactory {

    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#4F46E5","#0891B2"};

    public static Player createPlayer(String name, int numberFollowersPerPlayer) {
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle(name));
    }

    private static String getStyle(String name){
        if(PlayerType.valueOf(name).isServer()){
            return colours[0];
        }
        return colours[1];
    }
}
