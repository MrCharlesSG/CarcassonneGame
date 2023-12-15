package hr.algebra.carcassonnegame2.model.tile;

import java.io.Serial;
import java.io.Serializable;

public enum TileElementValue implements Serializable {
    PATH(0, "-fx-background-color: #d6d3d1;"), INTERSECTION(1, "-fx-background-color: #a8a29e;"), MONASTERY(2, "-fx-background-color: #b91c1c;"), FARM(3, "-fx-background-color: #15803d;"), CITY(4, "-fx-background-color: #92400e;"), CITY_WITH_SHIELD(5, "-fx-background-color: #92400e; -fx-border-color: black;");

    private final int value;

    @Serial
    private static final long serialVersionUID = 5L;

    private final String style;
    TileElementValue(int value, String style) {
        this.value= value;
        this.style = style;
    }

    public String getStyle(){
        return this.style;
    }

    public static TileElementValue getElementByValue(int value){
        for (TileElementValue element: values()) {
            if(element.value==value){
                return element;
            }
        }
        return null;
    }

    public boolean areTileElementsCompatible(TileElementValue tileElement){
        if ((this==INTERSECTION || this==PATH) && (tileElement==INTERSECTION || tileElement==PATH)){
            return true;
        }
        if((this==CITY || this == CITY_WITH_SHIELD) && (tileElement==CITY || tileElement == CITY_WITH_SHIELD)){
            return true;
        }
        if(this == FARM && tileElement==FARM){
            return true;
        }
        return false;
    }

    public boolean isCity(){
        return this==CITY || this == CITY_WITH_SHIELD;
    }

    public boolean isPath() {return this==PATH;}

    public  boolean isFarm() {return this==FARM;}

    public boolean isMonastery() {return this==MONASTERY;}

    public boolean isIntersection() {return this==INTERSECTION;}

    public boolean hasShield() {return this==CITY_WITH_SHIELD;}
}
