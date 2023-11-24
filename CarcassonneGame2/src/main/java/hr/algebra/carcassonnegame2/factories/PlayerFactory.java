package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.gameobjects.player.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.player.PlayerImpl;

public class PlayerFactory {

    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#4F46E5","#0891B2"};

    private static int number = -1;

    public static Player createPlayer(String name, int numberFollowersPerPlayer) {
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle());
    }

    private static String getStyle() {
        number++;
        return colours[number] +";";
    }
}
