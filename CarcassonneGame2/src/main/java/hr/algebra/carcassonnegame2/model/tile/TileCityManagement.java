package hr.algebra.carcassonnegame2.model.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.utils.TileUtils;

import static hr.algebra.carcassonnegame2.utils.TileUtils.getOtherPosition;

class TileCityManagement extends TileManagement {
    public TileCityManagement(Tile tile) {
        super(tile);
        tile.setCitiesConnected(initializeCitiesAreConnected());
    }

    @Override
    public boolean checkPutFollower(Position position) throws IllegalArgumentException {
        if(!getValuePosition(position).isCity()){
            throw new IllegalArgumentException("Wrong Manager");
        }
        if(getValuePosition(CENTER).isCity()){
            return (tile.getFollowerPosition() == null || !getValuePosition(tile.getFollowerPosition()).isCity())
                    && callGameToCheckOtherTile(TileElementValue.CITY);
        }else{
            if (tile.areCitiesConnected()) {
                return (tile.getFollowerPosition() == null || !getValuePosition(tile.getFollowerPosition()).isCity())
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
    public int countPathsForClosingPath(Position position) throws IllegalArgumentException {
        throw new IllegalArgumentException("Wrong Manager");
    }

    @Override
    public void setTileWithFollowerInPath(Position position) throws IllegalArgumentException {throw new IllegalArgumentException("Wrong Manager");}

    @Override
    public void prepareForClosingPath(Position position) throws IllegalArgumentException {throw new IllegalArgumentException("Wrong Manager");}

    @Override
    public void setIfFollowerInCity(Position position) throws IllegalArgumentException {
        if(isFollowerInCity(position)){
            setTileWithFollowerInCity();
        }
    }

    @Override
    public int countCitiesForClosingCities(Position position) throws IllegalArgumentException {
        if (tile.getFollowerPosition() != null && getValuePosition(tile.getFollowerPosition()).isCity()) {
            setTileWithFollowerInCity();
        }
        return getAddingPointForThisCity(position) + (tile.areCitiesConnected() ? getCountCityFromOtherTiles():0);
    }

    @Override
    public int getAddingPointForThisCity(Position position) throws IllegalArgumentException {
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
    public Position getNextCityValue(Position position) throws IllegalArgumentException {
        Position pos = position;
        do {
            pos= TileUtils.castRelativeToPositionInTile(TileUtils.castPositionInTileToRelative(pos).getNext());
        }
        while(!getValuePosition(pos).isCity());
        return pos;
    }

    @Override
    public boolean hasPathAnEnd() throws IllegalArgumentException {throw new IllegalArgumentException("Wrong Manager");}

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
