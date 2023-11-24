package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.model.gameobjects.tile.Tile;

public class TileUtils {

    private TileUtils(){}

    public static RelativePositionGrid castPositionInTileToRelative(Position position){
        if(position.equals(Tile.getLeftPosition())) return RelativePositionGrid.LEFT;
        else if(position.equals(Tile.getRightPosition())) return RelativePositionGrid.RIGHT;
        else if(position.equals(Tile.getTopPosition())) return RelativePositionGrid.TOP;
        else return RelativePositionGrid.BOTTOM;
    }

    public static Position castRelativeToPositionInTile(RelativePositionGrid relative){
        if(relative==RelativePositionGrid.TOP) return Tile.getTopPosition();
        else if(relative==RelativePositionGrid.BOTTOM) return Tile.getBottomPosition();
        else if(relative==RelativePositionGrid.RIGHT) return Tile.getRightPosition();
        else return Tile.getLeftPosition();
    }

    public static Position getOtherPosition(Position position){
        if(position.equals(Tile.getTopPosition())) return Tile.getBottomPosition();
        if(position.equals(Tile.getBottomPosition())) return Tile.getTopPosition();
        if(position.equals(Tile.getRightPosition())) return Tile.getLeftPosition();
        if(position.equals(Tile.getLeftPosition())) return Tile.getRightPosition();
        return null;
    }
}
