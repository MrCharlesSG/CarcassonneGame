package hr.algebra.carcassonnegame2.model.player;

public enum PlayerType {
    CLIENT, SERVER;

    public boolean isServer() {
        return this==SERVER;
    }
}
