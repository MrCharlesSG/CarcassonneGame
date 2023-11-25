package hr.algebra.carcassonnegame2.model.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.GameWorld;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;

import java.io.Serial;
import java.io.Serializable;

public abstract class Tile implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;
    public static final int NUM_ROWS_TILE = 5;
    public static final int NUM_COLS_TILE = 5;
    protected GameWorld game;
    protected TileElementValue[][] tileGrid;


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

    public abstract TileElementValue getValuePosition(Position point);

    public abstract boolean hasCity();

    public abstract boolean hasMonasteryAndFollower();

    public abstract int getPlayerFollower();

    public abstract int countPathsForClosingPath(Position position);

    public abstract boolean pathEnd();

    public abstract int countCitiesForClosingCities(Position position);

    public abstract Position getNextCityValue(Position start);

    public abstract void prepareForClosingPath(Position position);

    public abstract boolean areCitiesConnected();

    public abstract int getAddingPointForThisCity(Position position);

    public abstract void setIfFollowerInCity(Position position);

    public abstract Position getNextPositionInGameBoard(Position positionInTile);

    public abstract boolean hasPath();
    protected abstract void setCitiesConnected(boolean citiesConnected);
}
