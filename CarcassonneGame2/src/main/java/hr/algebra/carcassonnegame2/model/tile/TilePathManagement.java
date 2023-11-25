package hr.algebra.carcassonnegame2.model.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;

final class TilePathManagement extends TileManagement {
    public TilePathManagement(Tile tile) {
        super(tile);
    }

    @Override
    public boolean checkPutFollowerInPath(Position position) {
        if(getValuePosition(CENTER).isIntersection()){
            if( !isFollowerInPathSection(position)){
                if(position.getRow()==NUM_ROWS_TILE/2){
                    if(position.getCol()<NUM_COLS_TILE/2)
                        return game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.LEFT), RIGHT);
                    else
                        return game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.RIGHT), LEFT);
                }else {
                    if (position.getRow() < NUM_ROWS_TILE / 2)
                        return game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.TOP), BOTTOM);
                    else
                        return game.checkPositionInTileForFollower(getPositionInGameBoardForRelative(RelativePositionGrid.BOTTOM), TOP);
                }
            }
        }else{
            if(!tile.isFollowerInPosition(CENTER) && isFollowerInPath()) {
                return callGameToCheckOtherTile(TileElementValue.PATH);
            }
        }
        return false;
    }

    @Override
    public int countPathsForClosingPath(Position position) {
        if(tile.pathEnd()) {
            setTileWithFollowerInPath(position);
            return 0;
        }else{
            int counter = 0;
            setTileWithFollowerInPath(position);
            if(!position.equals(LEFT) || position.equals(CENTER)) counter += countPathForClosingPathAux(LEFT.getPositionInRight(), tile.getPositionInGameBoard().getPositionInLeft(), RIGHT);
            if(!position.equals(RIGHT) || position.equals(CENTER)) counter += countPathForClosingPathAux(RIGHT.getPositionInLeft(), tile.getPositionInGameBoard().getPositionInRight(), LEFT);
            if(!position.equals(TOP) || position.equals(CENTER)) counter += countPathForClosingPathAux(TOP.getPositionInBottom(), tile.getPositionInGameBoard().getPositionInTop(), BOTTOM);
            if (!position.equals(BOTTOM) || position.equals(CENTER)) counter += countPathForClosingPathAux(BOTTOM.getPositionInTop(), tile.getPositionInGameBoard().getPositionInBottom(), TOP);
            return counter;
        }
    }

    @Override
    public void setTileWithFollowerInPath(Position position) {
        if(isFollowerInPathSection(position)){
            game.setTileWithFollowerInCount(tile);
        }
    }

    @Override
    public boolean checkPutFollowerInCity(Position position) {return false;}

    @Override
    public void prepareForClosingPath(Position position) {
        if(isFollowerInPathSection(position)){
            game.setTileWithFollowerInCount(tile);
        }
    }

    @Override
    public void setIfFollowerInCity(Position position) {}

    @Override
    public int countCitiesForClosingCities(Position position) {return 0;}

    @Override
    public int getAddingPointForThisCity(Position position) {
        return 0;
    }

    @Override
    public Position getNextCityValue(Position position) {
        return null;
    }

    @Override
    public boolean hasPathAnEnd() {
        int counter = 0;
        counter+= getValuePosition(RIGHT).isPath()?1:0;
        counter+= getValuePosition(LEFT).isPath()?1:0;
        counter+= getValuePosition(TOP).isPath()?1:0;
        counter+= getValuePosition(BOTTOM).isPath()?1:0;
        return getValuePosition(CENTER).isIntersection()
                || counter == 1;
    }

    private int countPathForClosingPathAux(Position position, Position positionOtherTileGB, Position positionInOtherTile){
        if(getValuePosition(position).isPath()){
            setTileWithFollowerInPath(position);
            return game.countPathForClosingPath(positionOtherTileGB, positionInOtherTile);
        }
        return 0;
    }

    private boolean isFollowerInPath() {
        return (!getValuePosition(TOP).isPath() || !isFollowerInPathSection(TOP))
                && (!getValuePosition(RIGHT).isPath() || !isFollowerInPathSection(RIGHT))
                && (!getValuePosition(LEFT).isPath() || !isFollowerInPathSection(LEFT))
                && (!getValuePosition(BOTTOM).isPath() || !isFollowerInPathSection(BOTTOM));
    }

    private boolean isFollowerInPathSection(Position position){
        boolean returnValue=tile.isFollowerInPosition(position) || tile.isFollowerInPosition(CENTER);
        if(isPositionInsideTile(position.getPositionInTop()) && getValuePosition(position.getPositionInTop()).isPath() && !returnValue){
            returnValue = tile.isFollowerInPosition(position.getPositionInTop());
        }
        if(isPositionInsideTile(position.getPositionInBottom()) && getValuePosition(position.getPositionInBottom()).isPath() && !returnValue){
            returnValue = tile.isFollowerInPosition(position.getPositionInBottom());
        }
        if(isPositionInsideTile(position.getPositionInLeft()) && getValuePosition(position.getPositionInLeft()).isPath()&& !returnValue){
            returnValue = tile.isFollowerInPosition(position.getPositionInLeft());
        }
        if(isPositionInsideTile(position.getPositionInRight()) && getValuePosition(position.getPositionInRight()).isPath()&& !returnValue) {
            returnValue = tile.isFollowerInPosition(position.getPositionInRight());
        }
        return returnValue;
    }
}
