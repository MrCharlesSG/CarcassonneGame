package hr.algebra.carcassonnegame2.factories;

import com.fasterxml.jackson.databind.JsonNode;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import hr.algebra.carcassonnegame2.model.gameobjects.TileElementValue;
import hr.algebra.carcassonnegame2.model.gameobjects.TileImpl;

public enum FactoryTile {

    INSTANCE;

    public Tile createTile(JsonNode dataNode) {
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
            return new TileImpl(tileGrid);
        }
        return null;
    }
}
