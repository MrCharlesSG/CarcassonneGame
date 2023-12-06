package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.*;
import java.util.List;

import static hr.algebra.carcassonnegame2.utils.GridUtils.*;

public class Game implements GameWorld, Externalizable {

    @Serial
    private static final long serialVersionUID = 1L;
    private GameStatus gameStatus;
    private GameOperations gameOperations;

    public Game(){}
    @Override
    public void initializeGame(List<Player> players, int numberOfRemainingTiles, List<Tile> allTiles, List<Integer> listOfRemainType) {
        gameStatus= new GameStatus(players, numberOfRemainingTiles);
        gameOperations = new GameOperations(gameStatus, this);
        gameStatus.initializeGameStatus(allTiles, listOfRemainType);
    }

    @Override
    public void changeNextTile() {
        gameStatus.changeNextTile();
    }

    private boolean isGameFinished() { return gameStatus.isGameFinished(); }
    
    @Override
    public List<Player> getPlayersInfo() {
        return gameStatus.getPlayersInfo();
    }

    
    @Override
    public Tile[][] getGameBoard() {
        return gameStatus.getGameBoard();
    }

    
    @Override
    public List<Integer> update() {
        //resizeBoard();
        if(isGameFinished()){
            makeFinalCount();
            return getWinner();
        }
        gameStatus.update();
        return null;
    }

    private void makeFinalCount() {
        for (Player player: gameStatus.getPlayersInfo()){
            List<Position> tilesWithFollower = player.getPositionsOfFollowers();
            for (int i = 0; i < tilesWithFollower.size(); i++) {
                closeTile(getValueOfPosition(gameStatus.getGameBoard(), tilesWithFollower.get(i)));
            }
        }
    }

    private List<Integer> getWinner() {
        return gameStatus.getWinner();
    }
    
    @Override
    public boolean putTile(Position position) throws IllegalArgumentException {
        checkPositionIsCorrect(position);
        checkIfPositionHasTile(position);
        checkIfTileMatchWithOtherTiles(position);
        Tile newTile = gameOperations.putFollowerAndCreateNewTile(position);
        gameStatus.putTile(position, newTile);
        closeTile(newTile);
        return true;
    }

    private void checkIfTileMatchWithOtherTiles(Position position) throws IllegalArgumentException{
        if(!gameOperations.canPutTileInPosition(position)) throw new IllegalArgumentException("Tile doesn't match with the surrounding tiles");
    }

    private void checkIfPositionHasTile(Position position) throws IllegalArgumentException{
        if(gameStatus.isPositionOccupied(position)) throw new IllegalArgumentException("Position all ready has a tile");
    }
    private void checkPositionIsCorrect(Position position) throws IllegalArgumentException{
        if(!gameStatus.checkIfPositionIsSurrounded(position)) throw new IllegalArgumentException("Position is not close to any other tile");
    }

    private void closeTile(Tile newTile) {
        gameOperations.closeTile(newTile);
    }

    @Override
    public int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile) {
        return gameOperations.countCitiesForTile(positionInGameBoardOfOtherTile, positionInsideOtherTile);
    }

    @Override
    public int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile) {
        return gameOperations.countPathForClosingPath(positionInGameBoard, positionInsideTile);
    }

    
    @Override
    public void rotateNextTile() {
        gameStatus.rotateNextTile();
    }

    @Override
    public boolean canPutFollowerInPosition(Position position) {
        return gameStatus.getNextTile().isValidPositionForFollower(position);
    }

    @Override
    public String getNextPlayerInfo() {
        return gameStatus.getNextPlayerInfo();
    }

    @Override
    public boolean canPlayerPutAFollower(){
        return gameStatus.canPlayerPutAFollower();
    }

    @Override
    public int getRemainingTiles() {
        return gameStatus.getRemainingTiles();
    }

    @Override
    public int getRemainingTypes() {
        return gameStatus.getRemainingTypes();
    }

    @Override
    public boolean checkPositionInTileForFollower(Position positionInGameBoard, Position positionInTile) {
        return gameOperations.checkPositionInTileForFollower(positionInGameBoard,positionInTile);
    }

    @Override
    public void setTileWithFollowerInCount(Tile tileWithFollowerInCount) {
        gameOperations.setTileWithFollowerInCount(tileWithFollowerInCount);
    }

    @Override
    public List<Integer> finishGame() {
        gameStatus.finishGame();
        return update();
    }

    @Override
    public String getFollowerInTileStyle(Tile tile) {
        return gameStatus.getFollowerStyleInTile(tile);
    }

    @Override
    public Player getCurrentPlayer() {
        return gameStatus.getCurrentPlayer();
    }

    @Override
    public Tile getNextTile() {
        return gameStatus.getNextTile();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(gameStatus);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        gameStatus = (GameStatus) in.readObject();
        gameOperations = new GameOperations(gameStatus, this);
        Tile.initializeTiles(this);
    }
}
