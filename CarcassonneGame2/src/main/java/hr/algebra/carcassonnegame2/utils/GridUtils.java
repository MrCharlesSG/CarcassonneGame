package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.misc.Position;

public class GridUtils {

    private GridUtils(){}

    public static <T> T getValueOfPosition(T[][] grid, Position position) {
        return grid[position.getCol()][position.getRow()];
    }
    public static <T> T getLeft(T[][] grid) {
        return getValueOfPosition(grid, getLeftPosition(grid));
    }
    public static <T> T getRight(T[][] grid) {
        return getValueOfPosition(grid, getRightPosition(grid));
    }
    public static <T> T getTop(T[][] grid) {
        return getValueOfPosition(grid, getTopPosition(grid));
    }
    public static <T> T getBottom(T[][] grid) {
        return getValueOfPosition(grid, getBottomPosition(grid));
    }
    public static <T> T getCenter(T[][] grid) {
        return getValueOfPosition(grid, getCenterPosition(grid));
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
        assert(grid.length < newGrid.length
                && grid.length>0 && grid[0].length< newGrid[0].length);
        int numCols = grid.length, numRows = grid[0].length;
        int newNumColumns = newGrid.length, newNumRows = newGrid[0].length,
                yOffset = (newNumRows - numRows) / 2,xOffset = (newNumColumns - numCols) / 2;
        for (int i = 0; i < numCols; i++) {
            System.arraycopy(grid[i], 0, newGrid[i + xOffset], yOffset, numRows);
        }
        return newGrid;
    }

    public static <T> Position getTopPosition(T[][] grid) {
        return new Position(grid.length / 2, 0);
    }

    public static <T> Position getBottomPosition(T[][] grid) {
        return new Position(grid.length / 2, grid.length - 1);
    }

    public static <T> Position getRightPosition(T[][] grid) {
        return new Position(grid.length - 1, grid.length / 2);
    }

    public static <T> Position getLeftPosition(T[][] grid) {
        return new Position(0, grid.length / 2);
    }

    public static <T> Position getCenterPosition(T[][] grid) {
        return new Position(grid.length / 2, grid.length / 2);
    }

    public static <T> void copyGrid(T[][] gridDestination, T[][] gridOrigin){
        for (int i = 0; i < gridOrigin.length; i++) {
            for (int j = 0; j < gridOrigin[i].length; j++) {
                setValueInPosition(gridDestination, new Position(i, j), getValueOfPosition(gridOrigin, new Position(i, j)));
            }
        }
    }

    public static boolean needGridToResize(Position lastPositionTile, int numColsRowsGrid) {
        int col = lastPositionTile.getCol(), row = lastPositionTile.getRow();
        return col == 1 || col == numColsRowsGrid-2 || row==1 || row==numColsRowsGrid-2;
    }
}
