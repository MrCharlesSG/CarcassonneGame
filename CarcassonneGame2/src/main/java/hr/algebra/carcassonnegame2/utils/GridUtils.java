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
}
