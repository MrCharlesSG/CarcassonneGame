package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.PlayerImpl;

public enum FactoryPlayer {

    INSTANCE;

    public Player createPlayer(String name, int numberFollowersPerPlayer) {
        return new PlayerImpl(name, numberFollowersPerPlayer);
    }
}
