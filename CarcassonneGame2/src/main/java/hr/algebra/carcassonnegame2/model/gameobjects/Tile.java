package hr.algebra.carcassonnegame2.model.gameobjects;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;

public abstract class Tile {

    public static final int NUM_ROWS_TILE = 5;
    public static final int NUM_COLS_TILE = 5;

    public abstract boolean canPutTile(Tile otherTile, RelativePositionGrid positionOfOtherTile);

    public abstract void removeFollower();

    public abstract Tile catchTile();

    public abstract void rotateTile();

    public abstract boolean canPutFollower(Position point);

    public abstract TileElementValue[][] getRepresentation();

    public abstract boolean isFollowerInPosition(Position point);

    public abstract boolean isValidPositionForFollower(Position point);

    public abstract boolean hasFollower();

    public abstract Position getFollowerPosition();

    public abstract void setPositionInGameBoard(Position point);

    public abstract Position getPositionInGameBoard();

    public abstract void setFollower(int player, Position position);

    public static Position getTopPosition() {
        return new Position(NUM_COLS_TILE / 2, 0);
    }

    public static Position getBottomPosition() {
        return new Position(NUM_COLS_TILE / 2, NUM_ROWS_TILE - 1);
    }

    public static Position getRightPosition() {
        return new Position(NUM_COLS_TILE - 1, NUM_ROWS_TILE / 2);
    }

    public static Position getLeftPosition() {
        return new Position(0, NUM_ROWS_TILE / 2);
    }

    public static Position getCenterPosition() {
        return new Position(NUM_COLS_TILE / 2, NUM_ROWS_TILE / 2);
    }

    public static RelativePositionGrid castPositionInTileToRelative(Position position){
        if(position.equals(getLeftPosition())) return RelativePositionGrid.LEFT;
        else if(position.equals(getRightPosition())) return RelativePositionGrid.RIGHT;
        else if(position.equals(getTopPosition())) return RelativePositionGrid.TOP;
        else return RelativePositionGrid.BOTTOM;
    }

    public static Position castRelativeToPositionInTile(RelativePositionGrid relative){
        if(relative==RelativePositionGrid.TOP) return getTopPosition();
        else if(relative==RelativePositionGrid.BOTTOM) return getBottomPosition();
        else if(relative==RelativePositionGrid.RIGHT) return getRightPosition();
        else return getLeftPosition();
    }

    public static Position getOtherPosition(Position position){
        if(position.equals(getTopPosition())) return getBottomPosition();
        if(position.equals(getBottomPosition())) return getTopPosition();
        if(position.equals(getRightPosition())) return getLeftPosition();
        if(position.equals(getLeftPosition())) return getRightPosition();
        return null;
    }

    public abstract TileElementValue getValuePosition(Position point);

    public abstract boolean hasIntersection();

    public abstract boolean hasCity();

    public abstract boolean hasMonasteryAndFollower();

    public abstract int getPlayerFollower();

    public abstract int countPathsForClosingPath(Position position);

    public abstract boolean pathEnd();

    public abstract void setTileWithFollowerInPath(Position position);

    public abstract int countCitiesForClosingCities(Position position);

    public abstract Position getNextCityValue(Position start);

    public abstract void prepareForClosingPath(Position position);

    public abstract boolean areCitiesConnected();

    public abstract int getAddingPointForThisCity(Position position);

    public abstract void setIfFollowerInCity(Position position);

    public abstract Position getNextPositionInGameBoard(Position positionInTile);

    public abstract boolean hasPath();
}
