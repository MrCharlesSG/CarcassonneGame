package hr.algebra.carcassonnegame2.model;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import hr.algebra.carcassonnegame2.model.gameobjects.TileImpl;

import java.io.*;
import java.util.*;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    static final int INIT_NUM_COLS_GAME_BOARD =11;

    static final int INIT_NUM_ROWS_GAME_BOARD =11;
    static final int PENALIZATION_FOR_CHANGING_TILE =-5;
    private static final int POINTS_FOR_CITY = 2;
    private static final int POINTS_FOR_MONASTERY = 9;
    private static final int POINTS_FOR_PATH = 1;
    private Tile[][] gameBoard;
    private List<Tile> remainingTiles;
    private Boolean[][] visitedTilesGrid;
    private int numberOfRemainingTiles;
    private Tile nextTile;
    private boolean hasRemovedNexTileFromRemaining;
    private final Random random;
    private int numColsGameBoard;
    private int numRowsGameBoard;
    private List<Player> playersInfo;
    private int currentPlayer;
    private List<Integer> listOfRemainType;
    private Tile tileWithFollowerInCount =null;

    private boolean hasPathOrCityAnEnd = true;

    public Game(){
        this.random = new Random();
    }

    public Game(List<Tile> listOfTiles, List<Player> playerList, int numberOfRemainingTiles, List<Integer> listOfRemainType) {
        this();
        initializeGame(listOfTiles, playerList, numberOfRemainingTiles, listOfRemainType);
    }


    public void initializeGame(List<Tile> allTiles, List<Player> players, int numberOfRemainingTiles, List<Integer> listOfRemainType){
        this.remainingTiles=allTiles;
        this.listOfRemainType=listOfRemainType;
        this.numberOfRemainingTiles = numberOfRemainingTiles;
        this.numColsGameBoard= INIT_NUM_COLS_GAME_BOARD;
        this.numRowsGameBoard= INIT_NUM_ROWS_GAME_BOARD;
        this.gameBoard = new Tile[this.numColsGameBoard][this.numRowsGameBoard];
        this.playersInfo = players;
        this.currentPlayer = random.nextInt(this.playersInfo.size());
        putFirstTile();
        setNextTile();
    }

    private void putFirstTile() {
        setNextTile();
        this.numberOfRemainingTiles--;
        Tile newTile = new TileImpl(nextTile.getRepresentation(), this);
        newTile.setPositionInGameBoard(new Position(INIT_NUM_COLS_GAME_BOARD /2, INIT_NUM_ROWS_GAME_BOARD /2 ));
        this.gameBoard[INIT_NUM_COLS_GAME_BOARD /2][ INIT_NUM_ROWS_GAME_BOARD /2 ] = newTile;
    }

    public void setNextTile(){
        hasRemovedNexTileFromRemaining=false;
        int indexNextTile = random.nextInt(this.remainingTiles.size());
        this.nextTile = this.remainingTiles.get(indexNextTile).catchTile();
        listOfRemainType.add(indexNextTile, listOfRemainType.get(indexNextTile)-1);
        if(listOfRemainType.get(indexNextTile)==0){
            listOfRemainType.remove(indexNextTile);
            remainingTiles.remove(indexNextTile);
            hasRemovedNexTileFromRemaining=true;
        }
    }

    public void changeNextTile() {
        if(hasRemovedNexTileFromRemaining){
            listOfRemainType.add(1);
            nextTile.removeFollower();
            nextTile.setPositionInGameBoard(null);
            remainingTiles.add(nextTile);
        }
        penalizationForChangingTile();
        setNextTile();
    }

    private void penalizationForChangingTile() {
        playersInfo.get(currentPlayer).addPunctuation(PENALIZATION_FOR_CHANGING_TILE);
        setNextPlayer();
    }

    public boolean isGameIsFinished() { return this.numberOfRemainingTiles<=0; }
    
    public List<Player> getPlayersInfo() {
        return new ArrayList<Player>(playersInfo);
    }

    
    public Tile[][] getGameBoard() {
        return this.gameBoard;
    }

    
    public Tile getNextTile() {
        return this.nextTile;
    }

    
    public int update() {
        //resizeBoard();
        if(isGameIsFinished()){
            makeFinalCount();
            return getWinner();
        }
        if(needGameBoardToIncrease(nextTile.getPositionInGameBoard())) {
            resizeBoard();
        }
        setNextPlayer();
        setNextTile();
        return -1;
    }

    private boolean needGameBoardToIncrease(Position positionInGameBoard) {
        int col = positionInGameBoard.getCol(), row = positionInGameBoard.getRow();
        return col == 1 || col == numColsGameBoard-2 || row==1 || row==numRowsGameBoard-2;
    }

    private void makeFinalCount() {
        for (Player player: playersInfo){
            TreeSet<Position> tilesWithFollower = player.getPositionsOfFollowers();
            for (Position position: tilesWithFollower){
                closeTile(getValueOfPosition(gameBoard, position));
            }
        }
    }

    private int getWinner() {
        int winner=0, bestPoints=playersInfo.get(0).getPoints(), i=0;
        for (Player pl : playersInfo){
            if(pl.getPoints()==bestPoints){
                winner=6;
            }else if(pl.getPoints()>bestPoints){
                winner=i;
                bestPoints=pl.getPoints();
            }
            i++;
        }
        return winner;
    }

    private void resizeBoard() {
        int newNumColumns = numColsGameBoard*2+1, newNumRows = numRowsGameBoard*2+1,
                yOffset = (newNumRows - numRowsGameBoard) / 2,xOffset = (newNumColumns - numColsGameBoard) / 2;
        Tile[][] newGameBoard = new Tile[newNumColumns][newNumRows];
        for (int i = 0; i < numColsGameBoard; i++) {
            for (int j = 0; j < numRowsGameBoard; j++) {
                newGameBoard[i + xOffset][j + yOffset] = gameBoard[i][j];
            }
        }
        numColsGameBoard = newNumColumns;
        numRowsGameBoard = newNumRows;
        gameBoard = newGameBoard;
    }
    
    public boolean putTile(Position position) throws IllegalArgumentException {
        checkIfPositionHasTile(position);
        checkPositionIsCorrect(position);
        checkIfTileMatchWithOtherTiles(position);
        Tile newTile = putFollowerAndCreateNewTile(position);
        setValueInPosition(gameBoard, position, newTile);
        closeTile(newTile);
        numberOfRemainingTiles--;
        return true;
    }

    private void checkIfTileMatchWithOtherTiles(Position position) throws IllegalArgumentException{
        if(!canPutTileInPosition(position)) throw new IllegalArgumentException("Tile doesn't match with the surrounding tiles");
    }

    private void checkIfPositionHasTile(Position position) throws IllegalArgumentException{
        if(getValueOfPosition(gameBoard, position)!=null) throw new IllegalArgumentException("Position all ready has a tile");
    }
    private void checkPositionIsCorrect(Position position) throws IllegalArgumentException{
        if(!checkPositionTileCorrect(position)) throw new IllegalArgumentException("Position is not close to any other tile");
    }

    private Tile putFollowerAndCreateNewTile(Position position) throws IllegalArgumentException{
        Tile newTile = new TileImpl(nextTile.getRepresentation(), position, this);
        nextTile.setPositionInGameBoard(position);
        if(nextTile.hasFollower()) {
            this.visitedTilesGrid = new Boolean[this.numColsGameBoard][this.numRowsGameBoard];
            setValueInPosition(visitedTilesGrid, position, true);
            if (!newTile.canPutFollower(this.nextTile.getFollowerPosition())) {
                this.nextTile.removeFollower();
                throw new IllegalArgumentException("The follower is not correctly collocated");
            }
            newTile.setFollower(currentPlayer, this.nextTile.getFollowerPosition());
            playersInfo.get(currentPlayer).putFollower(position);
            this.nextTile.removeFollower();
        }
        return newTile;
    }

    private void closeTile(Tile newTile) {
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
        visitedTilesGrid= new Boolean[numColsGameBoard][numRowsGameBoard];
        setValueInPosition(visitedTilesGrid, newTile.getPositionInGameBoard(), true);
    }

    private void closeCityAux(int count){
        if(tileWithFollowerInCount !=null && (hasPathOrCityAnEnd || isGameIsFinished())){
            hasBeenClosedTile(tileWithFollowerInCount, count);
        }
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd=true;
    }

    private int countCloseCity(Tile newTile, Position pos) {
        newTile.setIfFollowerInCity(pos);
        return countCitiesForTile(newTile.getNextPositionInGameBoard(pos), Tile.getOtherPosition(pos)) + POINTS_FOR_CITY + newTile.getAddingPointForThisCity(pos);
    }

    public int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile) {
        Tile tile = getValueOfPosition(gameBoard, positionInGameBoardOfOtherTile);
        if(tile!= null && getValueOfPosition(visitedTilesGrid, positionInGameBoardOfOtherTile)==null){
            setValueInPosition(visitedTilesGrid, positionInGameBoardOfOtherTile, true);
            return tile.countCitiesForClosingCities(positionInsideOtherTile) + POINTS_FOR_CITY;
        }
        hasPathOrCityAnEnd=tile!=null && hasPathOrCityAnEnd;
        return 0;
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
            Tile newTile = getValueOfPosition(gameBoard, tile.getNextPositionInGameBoard(position));
            if(newTile != null){
                closeCountPaths(Tile.getOtherPosition(position), newTile);
            }
            hasPathOrCityAnEnd= newTile!=null && hasPathOrCityAnEnd;
        }
    }

    private void closeCountPaths(Position positionInNewTile, Tile newTile) {
        int count = newTile.countPathsForClosingPath(positionInNewTile) + POINTS_FOR_PATH;
        if(tileWithFollowerInCount!=null && (isGameIsFinished() || hasPathOrCityAnEnd))
            hasBeenClosedTile(tileWithFollowerInCount, count);
    }

    public int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile) {
        Tile tile = getValueOfPosition(gameBoard, positionInGameBoard);
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
            }else if(isGameIsFinished()){
                hasBeenClosedTile(newTile, counter);
            }
        }
    }

    private int closeMonasteryAux(Tile tile) {
        int counter=0, colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if((rowPosition!=row || colPosition!=col) &&  getValueOfPosition(gameBoard, new Position(col, row))!=null)
                        counter++;
            }
        }
        return counter;
    }

    private void checkSurroundingMonastery(Tile tile){
        int colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if(rowPosition!=row || colPosition!=col ){
                    Tile tileNextToTile = getValueOfPosition(gameBoard, new Position(col, row));
                    if(tileNextToTile!=null && tileNextToTile.hasMonasteryAndFollower()){
                        int counter = closeMonasteryAux(tileNextToTile);
                        if(counter==8){
                            hasBeenClosedTile(tileNextToTile, POINTS_FOR_MONASTERY);
                        }else if(isGameIsFinished()){
                            hasBeenClosedTile(tileNextToTile, counter);
                        }
                    }
                }
            }
        }
    }

    private void hasBeenClosedTile(Tile tile, int punctuation){
        this.playersInfo.get(tile.getPlayerFollower()).addPunctuation(punctuation);
        this.playersInfo.get(tile.getPlayerFollower()).removeFollower(tile.getPositionInGameBoard());
        tile.removeFollower();
    }

    private boolean canPutTileInPosition(Position position) {
        int colPosition = position.getCol(), rowPosition = position.getRow();
        boolean isCorrectPosition= true;
        //Check Top
        if(this.gameBoard[colPosition][rowPosition-1]!=null){
            isCorrectPosition = this.nextTile.canPutTile(this.gameBoard[colPosition][rowPosition-1],RelativePositionGrid.BOTTOM);
        }

        //Check Bottom
        if(this.gameBoard[colPosition][rowPosition+1]!=null && isCorrectPosition){
            isCorrectPosition = this.nextTile.canPutTile(this.gameBoard[colPosition][rowPosition+1],RelativePositionGrid.TOP);
        }

        //Check Left
        if(this.gameBoard[colPosition-1][rowPosition]!=null && isCorrectPosition){
            isCorrectPosition = this.nextTile.canPutTile(this.gameBoard[colPosition-1][rowPosition],RelativePositionGrid.RIGHT);
        }

        //Check Right
        if(this.gameBoard[colPosition+1][rowPosition]!=null && isCorrectPosition){
            isCorrectPosition = this.nextTile.canPutTile(this.gameBoard[colPosition+1][rowPosition],RelativePositionGrid.LEFT);
        }
        return isCorrectPosition;
    }

    public boolean checkPositionTileCorrect(Position position){
        int col = position.getCol(), row = position.getRow();
        return gameBoard[col-1][row]!= null
                || gameBoard[col+1][row] != null
                || gameBoard[col][row-1] != null
                || gameBoard[col][row+1] != null;
    }

    
    public void rotateNextTile() {
        this.nextTile.rotateTile();
    }

    public void setNextPlayer(){
        if(this.currentPlayer==this.playersInfo.size()-1){
            this.currentPlayer=0;
        }else{
            this.currentPlayer++;
        }
    }

    public boolean canPutFollowerInPosition(Position position) {
        return this.nextTile.isValidPositionForFollower(position);
    }

    public String getNextPlayerInfo() {
        return playersInfo.get(currentPlayer).getName();
    }

    public boolean canPlayerPutAFollower(){
        return playersInfo.get(currentPlayer).getNumberOfFollowers()>=1;
    }

    public int getRemainingTiles() {
        return this.numberOfRemainingTiles;
    }

    public int getRemainingTypes() {
        return this.remainingTiles.size();
    }

    public boolean checkPositionInTileForFollower(Position positionInGameBoard, Position positionInTile) {
        if(getValueOfPosition(visitedTilesGrid,positionInGameBoard)==null ){
            setValueInPosition(visitedTilesGrid, positionInGameBoard, true);
            if(getValueOfPosition(gameBoard, positionInGameBoard)!=null){
                return getValueOfPosition(gameBoard, positionInGameBoard).canPutFollower(positionInTile);
            }
        }
        return true;
    }

    private <T> T getValueOfPosition(T[][] structure, Position position) {
        return structure[position.getCol()][position.getRow()];
    }

    private <T> void setValueInPosition(T[][] structure, Position position, T newValue) {
        structure[position.getCol()][position.getRow()]=newValue;
    }

    public void setTileWithFollowerInCount(Tile tileWithFollowerInCount) {
        this.tileWithFollowerInCount = tileWithFollowerInCount;
    }

    public int finishGame() {
        this.numberOfRemainingTiles=0;
        return update();
    }

    public String getFollowerInTileStyle(Tile tile) {
        return playersInfo.get(tile.getPlayerFollower()).getTextColor();
    }

    public Player getCurrentPlayer() {
        return playersInfo.get(currentPlayer);
    }
}
