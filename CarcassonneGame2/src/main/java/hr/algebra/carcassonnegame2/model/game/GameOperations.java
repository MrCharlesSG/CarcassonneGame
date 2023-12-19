package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.RelativePositionGrid;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileImpl;
import hr.algebra.carcassonnegame2.utils.GridUtils;
import hr.algebra.carcassonnegame2.utils.TileUtils;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.*;
import static hr.algebra.carcassonnegame2.utils.GridUtils.getValueOfPosition;
import static hr.algebra.carcassonnegame2.utils.GridUtils.setValueInPosition;

final class GameOperations {

    private Tile tileWithFollowerInCount =null;
    private boolean hasPathOrCityAnEnd = true;
    private Boolean[][] visitedTilesGrid;
    private GameStatus gameStatus;
    private Game game;
    public GameOperations(GameStatus gameStatus, Game game) {
        this.gameStatus=gameStatus;
        this.game = game;
    }

    public Tile putFollowerAndCreateNewTile(Position position) throws IllegalArgumentException{
        Tile nextTile = gameStatus.getNextTile();
        Tile newTile = new TileImpl(nextTile.getRepresentation(), position);
        nextTile.setPositionInGameBoard(position);
        if(nextTile.hasFollower()) {
            this.visitedTilesGrid = new Boolean[gameStatus.getNumColsGameBoard()][gameStatus.getNumRowsGameBoard()];
            setValueInPosition(visitedTilesGrid, position, true);
            if (!newTile.canPutFollower(nextTile.getFollowerPosition())) {
                nextTile.removeFollower();
                throw new IllegalArgumentException("The follower is not correctly collocated");
            }
            newTile.setFollower(gameStatus.getCurrentPlayer(), nextTile.getFollowerPosition());
            gameStatus.getCurrentPlayer().putFollower(position);
            nextTile.removeFollower();
        }
        return newTile;
    }

    public void closeTile(Tile newTile) {
        closeCity(newTile);
        closePath(newTile);
        closeMonastery(newTile);
        //See if in the surroundings there is a monastery
        checkSurroundingMonastery(newTile);
    }

    private void closeCity(Tile newTile) {
        if(newTile.hasCity()){
            prepareForClose(newTile);
            int count=0;
            if(newTile.areCitiesConnected()){
                count = newTile.countCitiesForClosingCities(newTile.getNextCityValue(Tile.getLeftPosition())) + POINTS_FOR_CITY;
                closeCityAux(count);
            }else{
                Position firstValue = newTile.getNextCityValue(Tile.getLeftPosition());
                Position pos = firstValue;
                do {
                    count= countCloseCity(newTile, pos);
                    closeCityAux(count);
                    pos = newTile.getNextCityValue(pos);
                }while (!pos.equals(firstValue));
            }
        }
    }

    private void prepareForClose(Tile newTile) {
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd =true;
        visitedTilesGrid= new Boolean[gameStatus.getNumColsGameBoard()][gameStatus.getNumRowsGameBoard()];
        setValueInPosition(visitedTilesGrid, newTile.getPositionInGameBoard(), true);
    }

    private void closeCityAux(int count){
        if(tileWithFollowerInCount !=null && (hasPathOrCityAnEnd || gameStatus.isGameFinished())){
            hasBeenClosedTile(tileWithFollowerInCount, count);
        }
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd=true;
    }

    private int countCloseCity(Tile newTile, Position pos) {
        newTile.setIfFollowerInCity(pos);
        return countCitiesForTile(newTile.getNextPositionInGameBoard(pos), TileUtils.getOtherPosition(pos)) + POINTS_FOR_CITY + newTile.getAddingPointForThisCity(pos);
    }

    private void closePath(Tile newTile){
        if(newTile.pathEnd()) {
            closePathInEnd(newTile);
        }
        else if(newTile.hasPath()){
            prepareForClose(newTile);
            closeCountPaths(Tile.getCenterPosition(), newTile);
        }
    }

    private void closePathInEnd(Tile newTile) {
        closePathInEndAux(newTile, Tile.getLeftPosition());
        closePathInEndAux(newTile, Tile.getRightPosition());
        closePathInEndAux(newTile, Tile.getTopPosition());
        closePathInEndAux(newTile, Tile.getBottomPosition());
    }

    private void closePathInEndAux(Tile tile, Position position){
        if(tile.getValuePosition(position).isPath()){
            prepareForClose(tile);
            tile.prepareForClosingPath(position);
            Tile newTile = getPositionInGameBoard( tile.getNextPositionInGameBoard(position));
            if(newTile != null){
                if(tile.getPlayerFollower()!=null)
                    tile.getPlayerFollower().addPunctuation(POINTS_FOR_PATH);
                else
                    gameStatus.getCurrentPlayer().addPunctuation(POINTS_FOR_CITY);
                closeCountPaths(TileUtils.getOtherPosition(position), newTile);
            }
            hasPathOrCityAnEnd= newTile!=null && hasPathOrCityAnEnd;
        }
    }

    private Tile getPositionInGameBoard(Position position) {
        return GridUtils.getValueOfPosition(gameStatus.getGameBoard(), position);
    }

    private void closeCountPaths(Position positionInNewTile, Tile newTile) {
        int count = newTile.countPathsForClosingPath(positionInNewTile) + POINTS_FOR_PATH;
        if(tileWithFollowerInCount!=null && (gameStatus.isGameFinished() || hasPathOrCityAnEnd))
            hasBeenClosedTile(tileWithFollowerInCount, count);
    }

    public int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile) {
        Tile tile = getPositionInGameBoard(positionInGameBoardOfOtherTile);
        if(tile!= null && getValueOfPosition(visitedTilesGrid, positionInGameBoardOfOtherTile)==null){
            setValueInPosition(visitedTilesGrid, positionInGameBoardOfOtherTile, true);
            return tile.countCitiesForClosingCities(positionInsideOtherTile) + POINTS_FOR_CITY;
        }
        hasPathOrCityAnEnd=tile!=null && hasPathOrCityAnEnd;
        return 0;
    }

    private void hasBeenClosedTile(Tile tile, int punctuation){
        tile.getPlayerFollower().addPunctuation(punctuation);
        removeFollowerOfPlayer(tile);
    }

    public void removeFollowerOfPlayer(Tile tile) {
        tile.getPlayerFollower().removeFollower(tile.getPositionInGameBoard());
        tile.removeFollower();
    }

    public int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile) {
        Tile tile = getPositionInGameBoard(positionInGameBoard);
        if(tile!=null && getValueOfPosition(visitedTilesGrid, positionInGameBoard)==null){
            setValueInPosition(visitedTilesGrid, positionInGameBoard, true);
            return tile.countPathsForClosingPath(positionInsideTile) + POINTS_FOR_PATH;
        }
        hasPathOrCityAnEnd = tile!=null && hasPathOrCityAnEnd;
        return 0;
    }

    private void closeMonastery(Tile newTile) {
        if(newTile.hasMonasteryAndFollower()){
            int counter = closeMonasteryAux(newTile);
            if(counter==8){
                hasBeenClosedTile(newTile, POINTS_FOR_MONASTERY);
            }else if(gameStatus.isGameFinished()){
                hasBeenClosedTile(newTile, counter);
            }
        }
    }

    private int closeMonasteryAux(Tile tile) {
        int counter=0, colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if((rowPosition!=row || colPosition!=col) &&  getPositionInGameBoard( new Position(col, row))!=null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private void checkSurroundingMonastery(Tile tile){
        int colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if(rowPosition!=row || colPosition!=col ){
                    Tile tileNextToTile = getPositionInGameBoard( new Position(col, row));
                    if(tileNextToTile!=null && tileNextToTile.hasMonasteryAndFollower()){
                        int counter = closeMonasteryAux(tileNextToTile);
                        if(counter==8){
                            hasBeenClosedTile(tileNextToTile, POINTS_FOR_MONASTERY);
                        }else if(gameStatus.isGameFinished()){
                            hasBeenClosedTile(tileNextToTile, counter);
                        }
                    }
                }
            }
        }
    }

    public boolean checkPositionInTileForFollower(Position positionInGameBoard, Position positionInTile) {
        if(getValueOfPosition(visitedTilesGrid,positionInGameBoard)==null ){
            setValueInPosition(visitedTilesGrid, positionInGameBoard, true);
            Tile tile = getValueOfPosition(gameStatus.getGameBoard(), positionInGameBoard);
            if(tile!=null){
                return tile.canPutFollower(positionInTile);
            }
        }
        return true;
    }

    public void setTileWithFollowerInCount(Tile tileWithFollowerInCount) {
        this.tileWithFollowerInCount = tileWithFollowerInCount;
    }

    public boolean canPutTileInPosition(Position position) {
        if(getPositionInGameBoard(position.getPositionInTop())!=null)
             return gameStatus.getNextTile().canPutTile(getPositionInGameBoard(position.getPositionInTop()), RelativePositionGrid.BOTTOM);
        if(getPositionInGameBoard(position.getPositionInBottom())!=null)
            return gameStatus.getNextTile().canPutTile(getPositionInGameBoard(position.getPositionInBottom()),RelativePositionGrid.TOP);
        if(getPositionInGameBoard(position.getPositionInLeft())!=null)
            return gameStatus.getNextTile().canPutTile(getPositionInGameBoard(position.getPositionInLeft()),RelativePositionGrid.RIGHT);
        return getPositionInGameBoard(position.getPositionInRight())!=null
                && this.gameStatus.getNextTile().canPutTile(
                        getPositionInGameBoard(position.getPositionInRight())
                        , RelativePositionGrid.LEFT);
    }

    public boolean checkIfPositionIsSurrounded(Position position){
        int col = position.getCol(), row = position.getRow();
        Tile[][] gameBoard = gameStatus.getGameBoard();
        return gameBoard[col-1][row] != null
                || gameBoard[col+1][row] != null
                || gameBoard[col][row-1] != null
                || gameBoard[col][row+1] != null;
    }

    public boolean isPositionOccupied(Position position) {
        return getPositionInGameBoard(position)!=null;
    }

}
