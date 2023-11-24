package hr.algebra.carcassonnegame2.model.gameobjects.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.utils.GridUtils;

import java.io.Serial;
import java.io.Serializable;

public abstract class TileTypeManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    protected Tile tile;
    protected static Game game;
    protected static int NUM_ROWS_TILE;
    protected static int NUM_COLS_TILE;
    protected static Position CENTER;
    protected static Position RIGHT;
    protected static Position LEFT;
    protected static Position TOP;
    protected static Position BOTTOM;
    
    protected TileTypeManager(Tile tile){this.tile=tile;}

    public static void initializeTileManager(Game game){
        TileTypeManager.game=game;
        NUM_COLS_TILE= Tile.NUM_COLS_TILE;
        NUM_ROWS_TILE=Tile.NUM_ROWS_TILE;
        CENTER = Tile.getCenterPosition();
        RIGHT = Tile.getRightPosition();
        LEFT = Tile.getLeftPosition();
        TOP = Tile.getTopPosition();
        BOTTOM = Tile.getBottomPosition();
    }

    public static TileTypeManager getInstance(TileElementValue type, Tile tile){
        if(game!=null){
            switch (type){
                case PATH -> {
                    return new TilePathManager(tile);
                }
                case CITY -> {
                    return new TileCityManager(tile);
                }
            }
        }
        throw new IllegalArgumentException("Can not manage that type");
    }

    public abstract boolean checkPutFollowerInPath(Position position);

    protected boolean callGameToCheckOtherTile(TileElementValue value){
        boolean returnValue = true;
        if(getValuePosition( TOP).areTileElementsCompatible(value)){
            returnValue = game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.TOP), BOTTOM);
        }
        if(getValuePosition( BOTTOM).areTileElementsCompatible(value) && returnValue){
            returnValue = game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.BOTTOM), TOP);
        }
        if(getValuePosition(RIGHT).areTileElementsCompatible(value) && returnValue){
            returnValue = game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.RIGHT), LEFT);
        }
        if(getValuePosition(LEFT).areTileElementsCompatible(value) && returnValue){
            returnValue = game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.LEFT), RIGHT);
        }
        return returnValue;
    }

    protected TileElementValue getValuePosition(Position position){
        return GridUtils.getValueOfPosition(tile.getRepresentation(), position);
    }

    protected Position getPositionInGameBoardForRelative(RelativePositionGrid relativePositionGrid){
        return tile.getPositionInGameBoard().castRelativePositionToPoint(relativePositionGrid);
    }

    protected boolean isPositionInsideTile(Position position){
        int col = position.getCol(), row = position.getRow();
        return col>=0 && col<NUM_COLS_TILE && row>=0 && row<NUM_ROWS_TILE;
    }

    public abstract int countPathsForClosingPath(Position position);

    public abstract void setTileWithFollowerInPath(Position position);

    public abstract boolean checkPutFollowerInCity(Position position);

    public abstract void prepareForClosingPath(Position position);

    public abstract void setIfFollowerInCity(Position position);

    public abstract int countCitiesForClosingCities(Position position);

    public abstract int getAddingPointForThisCity(Position position);

    public abstract Position getNextCityValue(Position position);

    public abstract boolean hasPathAnEnd();
}
