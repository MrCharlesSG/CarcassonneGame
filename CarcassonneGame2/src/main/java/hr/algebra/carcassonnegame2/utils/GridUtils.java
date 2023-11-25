package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.misc.Position;

public class GridUtils {

    private GridUtils(){}

    public static <T> T getValueOfPosition(T[][] grid, Position position) {
        return grid[position.getCol()][position.getRow()];
    }

    public static <T> void setValueInPosition(T[][] grid, Position position, T newValue) {
        grid[position.getCol()][position.getRow()]=newValue;
    }

    public static <T> T[][] rotateGrid(T[][] grid, T[][] newGrid) {
        for (int col = 0; col < grid.length; col++) {
            for (int row = 0; row < grid[col].length; row++) {
                newGrid[col][row] = grid[grid[col].length - 1 - row][col];
            }
        }
        return newGrid;
    }
    public static <T> T[][] resizeGrid(T[][] grid, T[][] newGrid){
        assert(grid.length< newGrid.length
                && grid.length>0 && grid[0].length< newGrid[0].length);
        int numCols = grid.length, numRows = grid[0].length;
        int newNumColumns = newGrid.length, newNumRows = newGrid[0].length,
                yOffset = (newNumRows - numRows) / 2,xOffset = (newNumColumns - numCols) / 2;
        for (int i = 0; i < numCols; i++) {
            System.arraycopy(grid[i], 0, newGrid[i + xOffset], yOffset, numRows);
        }
        return newGrid;
    }
}
