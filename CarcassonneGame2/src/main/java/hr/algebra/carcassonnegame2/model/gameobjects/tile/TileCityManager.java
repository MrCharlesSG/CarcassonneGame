package hr.algebra.carcassonnegame2.model.gameobjects.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.utils.TileUtils;

import static hr.algebra.carcassonnegame2.utils.TileUtils.getOtherPosition;

public class TileCityManager extends TileTypeManager {
    private final Position followerPosition;
    public TileCityManager(Tile tile) {
        super(tile);
        followerPosition = tile.getFollowerPosition();
        tile.setCitiesConnected(initializeCitiesAreConnected());
    }

    @Override
    public boolean checkPutFollowerInPath(Position position) { return false;}

    @Override
    public int countPathsForClosingPath(Position position) {return 0;}

    @Override
    public void setTileWithFollowerInPath(Position position) {}

    @Override
    public boolean checkPutFollowerInCity(Position position) {
        if(getValuePosition(CENTER).isCity()){
            return (followerPosition == null || !getValuePosition(followerPosition).isCity())
                    && callGameToCheckOtherTile(TileElementValue.CITY);
        }else{
            if (tile.areCitiesConnected()) {
                return (followerPosition == null || !getValuePosition(followerPosition).isCity())
                        && callGameToCheckOtherTile(TileElementValue.CITY);
            } else {
                if(isFollowerInCity(position)){
                    return false;
                }
                if (position.getCol() == 0) {
                    return game.checkPositionInTileForFollower(tile.getPositionInGameBoard().castRelativePositionToPoint(RelativePositionGrid.LEFT), RIGHT);
                } else if (position.getCol() == NUM_COLS_TILE - 1) {
                    return game.checkPositionInTileForFollower(tile.getPositionInGameBoard().castRelativePositionToPoint(RelativePositionGrid.RIGHT), LEFT);
                } else if (position.getRow() == 0) {
                    return game.checkPositionInTileForFollower(tile.getPositionInGameBoard().castRelativePositionToPoint(RelativePositionGrid.TOP), BOTTOM);
                }
                return game.checkPositionInTileForFollower(tile.getPositionInGameBoard().castRelativePositionToPoint(RelativePositionGrid.BOTTOM),TOP);
            }

        }
    }

    @Override
    public void prepareForClosingPath(Position position) {}

    @Override
    public void setIfFollowerInCity(Position position) {
        if(isFollowerInCity(position)){
            setTileWithFollowerInCity();
        }
    }

    @Override
    public int countCitiesForClosingCities(Position position) {
        if (followerPosition != null && getValuePosition(followerPosition).isCity()) {
            setTileWithFollowerInCity();
        }
        return getAddingPointForThisCity(position) + (tile.areCitiesConnected() ? getCountCityFromOtherTiles():0);
    }

    @Override
    public int getAddingPointForThisCity(Position position) {
        if(tile.areCitiesConnected()){
            if(getValuePosition(LEFT).hasShield() ||
                    getValuePosition(RIGHT).hasShield() ||
                    getValuePosition(TOP).hasShield() ||
                    getValuePosition(BOTTOM).hasShield())
                return 2;
        }
        if(getValuePosition(position).hasShield()){
            return 2;
        }
        return 0;
    }

    @Override
    public Position getNextCityValue(Position position) {
        Position pos = position;
        do {
            pos= TileUtils.castRelativeToPositionInTile(TileUtils.castPositionInTileToRelative(pos).getNext());
        }
        while(!getValuePosition(pos).isCity());
        return pos;
    }

    @Override
    public boolean hasPathAnEnd() {return false;}

    private int getCountCityFromOtherTiles() {
        int count = getCountCityFromOtherTilesAux(LEFT);
        count+= getCountCityFromOtherTilesAux(RIGHT);
        count+= getCountCityFromOtherTilesAux(BOTTOM);
        return count + getCountCityFromOtherTilesAux(TOP);
    }

    private int getCountCityFromOtherTilesAux(Position position){ //Ultimo toque ha sido cambiar getCountOfCitiesFromOneTile(getNextPositionInGameBoard(position), position)
        return getValuePosition(position).isCity() ? getCountOfCitiesFromOneTile(tile.getNextPositionInGameBoard(position), getOtherPosition(position)) : 0;
    }

    private int getCountOfCitiesFromOneTile(Position positionInGameBoardForOtherTile, Position positionInsideOtherTile) {
        return game.countCitiesForTile(positionInGameBoardForOtherTile, positionInsideOtherTile);
    }

    private void setTileWithFollowerInCity() {
        game.setTileWithFollowerInCount(tile);
    }

    private boolean isFollowerInCity(Position position) {
        if(isPositionInsideTile(position.getPositionInTop())
                && getValuePosition(position.getPositionInTop()).isCity()
                && tile.isFollowerInPosition(position.getPositionInTop()))
            return true;
        if(isPositionInsideTile(position.getPositionInBottom())
                && getValuePosition(position.getPositionInBottom()).isCity()
                && tile.isFollowerInPosition(position.getPositionInBottom()))
            return true;
        if(isPositionInsideTile(position.getPositionInLeft())
                && getValuePosition(position.getPositionInLeft()).isCity()
                && tile.isFollowerInPosition(position.getPositionInLeft()))
            return true;
        if(isPositionInsideTile(position.getPositionInRight())
                && getValuePosition(position.getPositionInRight()).isCity()
                && tile.isFollowerInPosition(position.getPositionInRight()))
            return true;

        return tile.isFollowerInPosition(position);
    }

    private boolean initializeCitiesAreConnected(){
        if(getValuePosition(CENTER).isCity()){
            return true;
        }else if((getValuePosition(LEFT).isCity() || getValuePosition(RIGHT).isCity())
                && (getValuePosition(TOP).isCity() || getValuePosition(BOTTOM).isCity())){
            return isOneCornerACity();
        }
        return false;
    }

    private boolean isOneCornerACity() {
        return getValuePosition(new Position(0, 0)).isCity() ||
                getValuePosition(new Position(NUM_COLS_TILE-1, 0)).isCity() ||
                getValuePosition(new Position(0, NUM_ROWS_TILE-1)).isCity() ||
                getValuePosition(new Position(NUM_COLS_TILE-1, NUM_ROWS_TILE-1)).isCity();
    }
}
