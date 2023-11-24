package hr.algebra.carcassonnegame2.factories;

import com.fasterxml.jackson.databind.JsonNode;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.tile.Tile;
import hr.algebra.carcassonnegame2.model.gameobjects.tile.TileElementValue;
import hr.algebra.carcassonnegame2.model.gameobjects.tile.TileImpl;

public class TileFactory {

    private TileFactory(){}
    public static Tile createTile(JsonNode dataNode, Game game) {
        if(dataNode.isArray()){

            int numRowsInGrid = dataNode.size();
            int numColsInGrid = dataNode.get(0).size();

            TileElementValue[][] tileGrid = new TileElementValue[numRowsInGrid][numColsInGrid];

            for (int col = 0; col < numColsInGrid; col++) {
                JsonNode column = dataNode.get(col);
                for (int row = 0; row < numRowsInGrid; row++) {
                    TileElementValue el = TileElementValue.getElementByValue(column.get(row).asInt());
                    tileGrid[col][row] = el;

                }
            }
            return new TileImpl(tileGrid, game);
        }
        return null;
    }
}
