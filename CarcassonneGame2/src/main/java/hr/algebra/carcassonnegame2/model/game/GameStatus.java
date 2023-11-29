package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileImpl;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hr.algebra.carcassonnegame2.utils.GridUtils.*;

final class GameStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Tile[][] gameBoard;
    private List<Tile> remainingTiles;
    private int numberOfRemainingTiles;
    private Tile nextTile;
    private List<Integer> listOfRemainType;
    private int numColsGameBoard;
    private int numRowsGameBoard;
    private final List<Player> playersInfo;
    private int currentPlayer;
    private final Random random;
    private boolean hasRemovedNextTileFromRemaining;
    private final Game game;


    public GameStatus(List<Player> players, int numberOfRemainingTiles, Game game) {
        random = new Random();
        this.numberOfRemainingTiles = numberOfRemainingTiles;
        this.playersInfo = players;
        this.game=game;
    }

    public void initializeGameStatus(List<Tile> allTiles, List<Integer> listOfRemainType) {
        this.remainingTiles=allTiles;
        this.listOfRemainType=listOfRemainType;
        this.numColsGameBoard= GameWorld.INIT_NUM_COLS_GAME_BOARD;
        this.numRowsGameBoard= GameWorld.INIT_NUM_ROWS_GAME_BOARD;
        this.gameBoard = new Tile[this.numColsGameBoard][this.numRowsGameBoard];
        this.currentPlayer = random.nextInt(this.playersInfo.size());
        putFirstTile();
        setNextTile();
    }

    public void setNextTile() {
        hasRemovedNextTileFromRemaining =false;
        int indexNextTile = random.nextInt(this.remainingTiles.size());
        this.nextTile = this.remainingTiles.get(indexNextTile).catchTile();
        listOfRemainType.add(indexNextTile, listOfRemainType.get(indexNextTile)-1);
        if(listOfRemainType.get(indexNextTile)==0){
            listOfRemainType.remove(indexNextTile);
            remainingTiles.remove(indexNextTile);
            hasRemovedNextTileFromRemaining =true;
        }
    }

    public void putFirstTile() {
        setNextTile();
        this.numberOfRemainingTiles--;
        Tile newTile = new TileImpl(nextTile.getRepresentation());
        newTile.setPositionInGameBoard(new Position(numColsGameBoard /2, numRowsGameBoard /2 ));
        this.gameBoard[numColsGameBoard /2][ numRowsGameBoard /2 ] = newTile;

    }

    public void changeNextTile() {
        if(hasRemovedNextTileFromRemaining){
            listOfRemainType.add(1);
            nextTile.removeFollower();
            nextTile.setPositionInGameBoard(null);
            remainingTiles.add(nextTile);
        }
        penalizationForChangingTile();
        setNextTile();
    }

    private void penalizationForChangingTile() {
        playersInfo.get(currentPlayer).addPunctuation(Game.PENALIZATION_FOR_CHANGING_TILE);
        setNextPlayer();
    }

    public boolean isGameFinished() {
        return this.numberOfRemainingTiles<=0;
    }

    public List<Player> getPlayersInfo() {
        return new ArrayList<Player>(playersInfo);
    }

    public Tile[][] getGameBoard() {
        return this.gameBoard;
    }

    public Tile getNextTile() {
        return this.nextTile;
    }

    public void update() {
        if(needGameBoardToResize(nextTile.getPositionInGameBoard())){
            resizeBoard();
        }
        setNextPlayer();
        setNextTile();
    }
    private boolean needGameBoardToResize(Position lastPositionTile) {
        int col = lastPositionTile.getCol(), row = lastPositionTile.getRow();
        return col == 1 || col == numColsGameBoard-2 || row==1 || row==numRowsGameBoard-2;
    }

    private void resizeBoard() {
        numColsGameBoard = numColsGameBoard*2+1;
        numRowsGameBoard = numRowsGameBoard*2+1;
        Tile[][] newGameBoard = new Tile[numColsGameBoard][numRowsGameBoard];
        gameBoard = resizeGrid(gameBoard, newGameBoard);
    }

    public List<Integer> getWinner() {
        List<Integer> winners = new ArrayList<>(1);
        winners.add(0);
        int maxPoints = playersInfo.get(0).getPoints();
        for (int i = 1; i < playersInfo.size(); i++) {
            if(playersInfo.get(i).getPoints()> maxPoints){
                winners.clear();
                winners.add(i);
            }else if(playersInfo.get(i).getPoints() == maxPoints){
                winners.add(i);
            }
        }
        return winners;
    }

    public void putTile(Position position, Tile newTile) {
        setValueInPosition(gameBoard, position, newTile);
        numberOfRemainingTiles--;
    }

    public boolean isPositionOccupied(Position position) {
        return getValueOfPosition(gameBoard, position)!=null;
    }

    public void finishGame() {
        this.numberOfRemainingTiles=0;
    }

    public Player getCurrentPlayer() {
        return playersInfo.get(currentPlayer);
    }

    public int getRemainingTypes() {
        return this.remainingTiles.size();
    }

    public int getRemainingTiles() {
        return this.numberOfRemainingTiles;
    }

    public boolean canPlayerPutAFollower() {
        return playersInfo.get(currentPlayer).getNumberOfFollowers()>=1;
    }

    public String getFollowerStyleInTile(Tile tile) {
        return playersInfo.get(tile.getPlayerFollower()).getTextColor();
    }

    public void setNextPlayer() {
        if(this.currentPlayer==this.playersInfo.size()-1){
            this.currentPlayer=0;
        }else{
            this.currentPlayer++;
        }
    }

    public String getNextPlayerInfo() {
        return playersInfo.get(currentPlayer).getName();
    }

    public void rotateNextTile() {
        this.nextTile.rotateTile();
    }

    public boolean checkIfPositionIsSurrounded(Position position){
        int col = position.getCol(), row = position.getRow();
        return gameBoard[col-1][row]!= null
                || gameBoard[col+1][row] != null
                || gameBoard[col][row-1] != null
                || gameBoard[col][row+1] != null;
    }

    public int getNumColsGameBoard() {
        return numColsGameBoard;
    }
    public int getNumRowsGameBoard() {
        return numRowsGameBoard;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayer;
    }

    public void addPunctuationToPlayer(int points, int player) {
        playersInfo.get(player).addPunctuation(points);
    }

    public void addPunctuationToPlayer(int points) {
        playersInfo.get(currentPlayer).addPunctuation(points);
    }

    public void removeFollowerOfPlayer(Tile tile) {
        this.playersInfo.get(tile.getPlayerFollower()).removeFollower(tile.getPositionInGameBoard());
        tile.removeFollower();
    }

    public Tile getPositionInGameBoard(Position position) {
        Tile  tl  = getValueOfPosition(gameBoard, position);
        return tl;
    }
}
