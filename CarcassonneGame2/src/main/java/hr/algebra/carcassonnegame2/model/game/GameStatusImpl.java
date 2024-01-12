package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.configuration.GameConfiguration;
import hr.algebra.carcassonnegame2.factories.PlayerFactory;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.GameMove;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileImpl;
import hr.algebra.carcassonnegame2.utils.XmlUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.PENALIZATION_FOR_CHANGING_TILE;
import static hr.algebra.carcassonnegame2.utils.GridUtils.*;

final class GameStatusImpl implements GameStatus {
    @Serial
    private static final long serialVersionUID = 1L;
    private Tile[][] gameBoard;
    private List<Tile> remainingTiles;
    private int numberOfRemainingTiles;
    private Tile nextTile;
    private List<Integer> listOfRemainType;
    private int numColsGameBoard;
    private int numRowsGameBoard;
    private final List<Player> players;
    private int currentPlayer;
    private final Random random;
    private boolean hasRemovedNextTileFromRemaining;


    public GameStatusImpl(List<Player> players, int numberOfRemainingTiles, List<Tile> allTiles, List<Integer> listOfRemainType) {
        random = new Random();
        this.numberOfRemainingTiles = numberOfRemainingTiles;
        this.players = players;
        this.remainingTiles=allTiles;
        this.listOfRemainType=listOfRemainType;
        this.numColsGameBoard= GameWorld.INIT_NUM_COLS_GAME_BOARD;
        this.numRowsGameBoard= GameWorld.INIT_NUM_ROWS_GAME_BOARD;
        this.gameBoard = new Tile[this.numColsGameBoard][this.numRowsGameBoard];
        this.currentPlayer = random.nextInt(!this.players.isEmpty() ? this.players.size() : 2);
        putFirstTile();
        setNextTile();
    }

    private void setNextTile() {
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

    private void putFirstTile() {
        setNextTile();
        this.numberOfRemainingTiles--;
        Tile newTile = new TileImpl(nextTile.getRepresentation(), true);
        newTile.setPositionInGameBoard(new Position(numColsGameBoard /2, numRowsGameBoard /2 ));
        this.gameBoard[numColsGameBoard /2][ numRowsGameBoard /2 ] = newTile;

    }

    @Override
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
        players.get(currentPlayer).addPunctuation(PENALIZATION_FOR_CHANGING_TILE);
        setNextPlayer();
    }

    @Override
    public boolean isGameFinished() {
        return this.numberOfRemainingTiles<=0;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Tile[][] getGameBoard() {
        return this.gameBoard;
    }

    @Override
    public Tile getNextTile() {
        return this.nextTile;
    }

    @Override
    public void update() {
        if (needGridToResize(nextTile.getPositionInGameBoard(), numColsGameBoard)) {
            resizeBoard();
        }
        setNextPlayer();
        setNextTile();
    }

    private void resizeBoard() {
        numColsGameBoard = numColsGameBoard*2+1;
        numRowsGameBoard = numRowsGameBoard*2+1;
        Tile[][] newGameBoard = new Tile[numColsGameBoard][numRowsGameBoard];
        gameBoard = resizeGrid(gameBoard, newGameBoard);
    }

    @Override
    public List<Integer> getWinner() {
        List<Integer> winners = new ArrayList<>(1);
        winners.add(0);
        int maxPoints = players.get(0).getPoints();
        for (int i = 1; i < players.size(); i++) {
            if(players.get(i).getPoints()> maxPoints){
                winners.clear();
                winners.add(i);
            }else if(players.get(i).getPoints() == maxPoints){
                winners.add(i);
            }
        }
        return winners;
    }

    @Override
    public void putTile(Position position, Tile newTile) {
        setValueInPosition(gameBoard, position, newTile);
        numberOfRemainingTiles--;
    }

    @Override
    public void finishGame() {
        this.numberOfRemainingTiles=0;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    @Override
    public int getRemainingTypes() {
        return this.remainingTiles.size();
    }

    @Override
    public int getRemainingTiles() {
        return this.numberOfRemainingTiles;
    }

    private void setNextPlayer() {
        if(this.currentPlayer==this.players.size()-1){
            this.currentPlayer=0;
        }else{
            this.currentPlayer++;
        }
    }

    @Override
    public void rotateNextTile() {
        this.nextTile.rotateTile();
    }

    @Override
    public int getNumColsGameBoard() {
        return numColsGameBoard;
    }
    @Override
    public int getNumRowsGameBoard() {
        return numRowsGameBoard;
    }

    @Override
    public void addPlayer(Player player) {
        removeDefaultPlayer();
        players.add(player);
    }

    private void removeDefaultPlayer() {
        if(GameConfiguration.IS_GAME_MODE_ONLINE) {
            players.removeIf(Player::isDefault);
        }
    }
}
