package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.PlayerImpl;

public enum FactoryPlayer {

    INSTANCE;

    private final String headOfStyle = "-fx-fill: ";
    private final String[] colours = new String[]{"#ffd400","#1b00db"};

    private int number = -1;

    public Player createPlayer(String name, int numberFollowersPerPlayer) {
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle());
    }

    private String getStyle() {
        number++;
        return headOfStyle + colours[number] +";";
    }
}
