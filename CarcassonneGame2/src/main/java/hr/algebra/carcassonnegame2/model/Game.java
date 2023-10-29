package hr.algebra.carcassonnegame2.model;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import hr.algebra.carcassonnegame2.model.gameobjects.TileImpl;

import java.util.*;

public enum Game{

    INSTANCE;

    static final int INIT_NUM_COLS_GAME_BOARD =11;

    static final int INIT_NUM_ROWS_GAME_BOARD =11;
    static final int PENALIZATION_FOR_CHANGING_TILE =-5;
    private static final int POINTS_FOR_CITY = 2;
    private static final int POINTS_FOR_MONASTERY = 9;
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

    Game(){
        this.random = new Random();
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
        this.visitedTilesGrid = new Boolean[this.numColsGameBoard][this.numRowsGameBoard];
        putFirstTile();
        setNextTile();
    }

    private void putFirstTile() {
        setNextTile();
        this.numberOfRemainingTiles--;
        Tile newTile = new TileImpl(nextTile.getRepresentation());
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
        /*
        STEPS
        -Ask game board if position is close to any tile
        -Check If tile is compatible with position
        -Ask if follower is ok
        -Assign new tile to player
        -Close cities, paths and monasteries, and count
         */
        if(this.gameBoard[position.getCol()][position.getRow()]!=null){
            throw new IllegalArgumentException("Position all ready has a tile");
        }
        if(!checkPositionTileCorrect(position)){
            throw new IllegalArgumentException("Position is not close to any other tile");
        }
        if(!canPutTileInPosition(position)){
            throw new IllegalArgumentException("Tile doesn't match with the surrounding tiles");
        }

        Tile newTile = new TileImpl(nextTile.getRepresentation(), position);
        nextTile.setPositionInGameBoard(position);
        if(nextTile.hasFollower()) {
            this.visitedTilesGrid = new Boolean[this.numColsGameBoard][this.numRowsGameBoard];
            setValueOfPosition(visitedTilesGrid, position, true);
            if (!newTile.canPutFollower(this.nextTile.getFollowerPosition())) {
                this.nextTile.removeFollower();
                throw new IllegalArgumentException("The follower is not correctly collocated");
            }
            newTile.setFollower(currentPlayer, this.nextTile.getFollowerPosition());
            playersInfo.get(currentPlayer).putFollower(position);
            this.nextTile.removeFollower();
        }
        this.gameBoard[position.getCol()][position.getRow()] = newTile;
        closeTile(newTile);
        numberOfRemainingTiles--;
        return true;
    }

    private void closeTile(Tile newTile) {
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd =false;
        if(newTile.pathEnd() || isGameIsFinished() ){
            visitedTilesGrid= new Boolean[numColsGameBoard][numRowsGameBoard];
            setValueOfPosition(visitedTilesGrid, newTile.getPositionInGameBoard(), true);
            if(newTile.getValuePosition(Tile.getCenterPosition()).isIntersection()){
                closePathInIntersection(newTile, Tile.getLeftPosition(), Tile.getRightPosition(), newTile.getPositionInGameBoard().getPositionInLeft());
                tileWithFollowerInCount=null;
                hasPathOrCityAnEnd =true;
                closePathInIntersection(newTile, Tile.getRightPosition(), Tile.getLeftPosition(), newTile.getPositionInGameBoard().getPositionInRight());
                tileWithFollowerInCount=null;
                hasPathOrCityAnEnd =true;
                closePathInIntersection(newTile, Tile.getBottomPosition(), Tile.getTopPosition(), newTile.getPositionInGameBoard().getPositionInBottom());
                tileWithFollowerInCount=null;
                hasPathOrCityAnEnd =true;
                closePathInIntersection(newTile, Tile.getTopPosition(), Tile.getBottomPosition(), newTile.getPositionInGameBoard().getPositionInTop());
            }else{
                newTile.prepareForClosingPath(null);
                if(isGameIsFinished() && newTile.getValuePosition(newTile.getFollowerPosition()).isPath())
                    tileWithFollowerInCount = newTile;
                int count = newTile.countPathsForClosingPath(Tile.getCenterPosition())+1;
                if(tileWithFollowerInCount != null && (hasPathOrCityAnEnd || isGameIsFinished())){
                    hasBeenClosedTile(tileWithFollowerInCount, count);
                }
            }
        }
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd =true;
        if(newTile.hasCity()){
            visitedTilesGrid= new Boolean[numColsGameBoard][numRowsGameBoard];
            setValueOfPosition(visitedTilesGrid, newTile.getPositionInGameBoard(), true);
            int count=0;
            if(newTile.areCitiesConnected()){
                count = newTile.countCitiesForClosingCities(newTile.getNextCityValue(Tile.getLeftPosition()));
                closeCity(count);
            }else{
                int i=0;
                Position pos = Tile.getLeftPosition();
                do{
                    pos = newTile.getNextCityValue(pos);
                    count= countCloseCity(newTile, pos);
                    closeCity(count);
                    i++;
                }
                while(i<2 && pos !=  nextTile.getNextCityValue(pos));
            }
        }
        if(newTile.hasMonasteryAndFollower()){
            int counter = closeMonastery(newTile);
            if(counter==8){
                hasBeenClosedTile(newTile, POINTS_FOR_MONASTERY);
            }else if(isGameIsFinished()){
                hasBeenClosedTile(newTile, counter);
            }
        }
        //See if in the surroundings there is a monastery
        checkSurroundingMonastery(newTile);
    }

    private int countCloseCity(Tile newTile, Position pos) {
        newTile.setIfFollowerInCity(pos);
        RelativePositionGrid relative = Tile.castPositionInTileToRelative(pos);
        Position positionForNewTile = newTile.getPositionInGameBoard().castRelativePositionToPoint(relative);
        return countCitiesForTile(positionForNewTile, Tile.getOtherPosition(pos)) + POINTS_FOR_CITY + newTile.getAddingPointForThisCity(pos);
    }

    private void closeCity(int count){
        if(tileWithFollowerInCount !=null && (hasPathOrCityAnEnd || isGameIsFinished())){
            hasBeenClosedTile(tileWithFollowerInCount, count);
        }
        tileWithFollowerInCount=null;
        hasPathOrCityAnEnd=true;
    }

    private void closePathInIntersection(Tile tile, Position positionInTile, Position positionInNewTile, Position positionOfNewTile){
        int count =0;
        if(tile.getValuePosition(positionInTile).isPath()){
            tile.prepareForClosingPath(positionInTile);
            Tile newTile = getValueOfPosition(gameBoard, positionOfNewTile);
            if(newTile != null){
                count = newTile.countPathsForClosingPath(positionInNewTile);
                if(tileWithFollowerInCount!=null)
                    hasBeenClosedTile(tileWithFollowerInCount, count);
            }
            hasPathOrCityAnEnd=true;
        }
    }

    public int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile) {
        Tile tile = getValueOfPosition(gameBoard, positionInGameBoard);
        if(tile!=null && getValueOfPosition(visitedTilesGrid, positionInGameBoard)==null){
            setValueOfPosition(visitedTilesGrid, positionInGameBoard, true);
            return tile.countPathsForClosingPath(positionInsideTile);
        }
        hasPathOrCityAnEnd = false;
        return 0;
    }

    private void checkSurroundingMonastery(Tile tile){
        int colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if(rowPosition!=row || colPosition!=col ){
                    Tile tileNextToTile = getValueOfPosition(gameBoard, new Position(col, row));
                    if(tileNextToTile!=null && tileNextToTile.hasMonasteryAndFollower()){
                        int counter = closeMonastery(tileNextToTile);
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

    private int closeMonastery(Tile tile) {

        int counter=0, colPosition = tile.getPositionInGameBoard().getCol(), rowPosition = tile.getPositionInGameBoard().getRow();
        for (int col = colPosition-1; col <= colPosition+1; col++) {
            for (int row = rowPosition-1; row <= rowPosition+1; row++) {
                if(rowPosition!=row || colPosition!=col ){
                    if(getValueOfPosition(gameBoard, new Position(col, row))!=null){
                        counter++;
                    }
                }
            }
        }
        return counter;
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
            setValueOfPosition(visitedTilesGrid, positionInGameBoard, true);
            if(getValueOfPosition(gameBoard, positionInGameBoard)!=null){
                return getValueOfPosition(gameBoard, positionInGameBoard).canPutFollower(positionInTile);
            }
        }
        return true;
    }

    private <T> T getValueOfPosition(T[][] structure, Position position) {
        return structure[position.getCol()][position.getRow()];
    }

    private <T> void setValueOfPosition(T[][] structure, Position position, T newValue) {
        structure[position.getCol()][position.getRow()]=newValue;
    }

    public void setTileWithFollowerInCount(Tile tileWithFollowerInCount) {
        this.tileWithFollowerInCount = tileWithFollowerInCount;
    }

    public void setHasPathOrCityAnEnd(boolean b) {
        hasPathOrCityAnEnd =b;
    }

    public int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile) {
        Tile tile = getValueOfPosition(gameBoard, positionInGameBoardOfOtherTile);
        if(tile!= null && getValueOfPosition(visitedTilesGrid, positionInGameBoardOfOtherTile)==null){
            setValueOfPosition(visitedTilesGrid, positionInGameBoardOfOtherTile, true);
            return tile.countCitiesForClosingCities(positionInsideOtherTile) + POINTS_FOR_CITY;
        }
        hasPathOrCityAnEnd=tile!=null && hasPathOrCityAnEnd;
        return getValueOfPosition(visitedTilesGrid, positionInGameBoardOfOtherTile)!=null && isGameIsFinished() ? POINTS_FOR_CITY:0;
    }

    public int finishGame() {
        this.numberOfRemainingTiles=0;
        return update();
    }

    public String getFollowerInTileStyle(Tile tile) {
        return playersInfo.get(tile.getPlayerFollower()).getTextColor();
    }
}
