package hr.algebra.carcassonnegame2.model.player;

public enum PlayerType {
    CLIENT(1), SERVER(0);

    private final int value;

    PlayerType(int value) {
        this.value = value;
    }

    public boolean isServer() {
        return this==SERVER;
    }

    public static PlayerType getElementByValue(int value){
        for (PlayerType element: values()) {
            if(element.value==value){
                return element;
            }
        }
        return null;
    }
}
