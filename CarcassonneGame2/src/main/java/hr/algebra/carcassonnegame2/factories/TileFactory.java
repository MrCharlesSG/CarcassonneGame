package hr.algebra.carcassonnegame2.factories;

import com.fasterxml.jackson.databind.JsonNode;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileElementValue;
import hr.algebra.carcassonnegame2.model.tile.TileImpl;
import hr.algebra.carcassonnegame2.utils.GridUtils;

public class TileFactory {

    private TileFactory(){}
    public static Tile createTile(JsonNode dataNode) {
        if(dataNode.isArray()){

            int numRowsInGrid = dataNode.size();
            int numColsInGrid = dataNode.get(0).size();

            TileElementValue[][] tileGrid = new TileElementValue[numColsInGrid][numRowsInGrid];

            for (int col = 0; col < numColsInGrid; col++) {
                JsonNode column = dataNode.get(col);
                for (int row = 0; row < numRowsInGrid; row++) {
                    TileElementValue el = TileElementValue.getElementByValue(column.get(row).asInt());
                    GridUtils.setValueInPosition(tileGrid, new Position(col, row), el);
                }
            }
            return new TileImpl(tileGrid);
        }
        return null;
    }
}
