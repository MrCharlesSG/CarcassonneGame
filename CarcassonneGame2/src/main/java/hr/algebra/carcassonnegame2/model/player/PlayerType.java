package hr.algebra.carcassonnegame2.model.player;

public enum PlayerType {
    SERVER(0), CLIENT(1), DEFAULT(2);

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
        return DEFAULT;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isDefault() {
        return this ==DEFAULT;
    }
}
