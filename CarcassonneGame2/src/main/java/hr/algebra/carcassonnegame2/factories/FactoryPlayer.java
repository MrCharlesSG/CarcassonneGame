package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.PlayerImpl;

public enum FactoryPlayer {

    INSTANCE;

    private final String headOfStyle = "-fx-fill: ";
    private final String[] colours = new String[]{"#4F46E5","#0891B2"};

    private int number = -1;

    public Player createPlayer(String name, int numberFollowersPerPlayer) {
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle());
    }

    private String getStyle() {
        number++;
        return colours[number] +";";
    }
}
