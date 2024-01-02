package hr.algebra.carcassonnegame2.model.tile;

import java.io.Serial;
import java.io.Serializable;

public class TileDescription implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
    private TileElementValue top;
    private TileElementValue bottom;
    private TileElementValue right;
    private TileElementValue left;
    private TileElementValue center;

    public TileDescription(Tile tile){
        top = tile.getValuePosition(Tile.getTopPosition());
        bottom = tile.getValuePosition(Tile.getBottomPosition());
        right = tile.getValuePosition(Tile.getRightPosition());
        left = tile.getValuePosition(Tile.getLeftPosition());
        center = tile.getValuePosition(Tile.getCenterPosition());
    }

    public String getDescription(){
        return
                "Top:" + top.name() + ", " +
                "Right:" + right.name() + ", " +
                "Left:" + left.name() + ", " +
                "Bottom:" + bottom.name() + ", " +
                "Center:" + center.name();
    }
}
