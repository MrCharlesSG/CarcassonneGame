package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.Serializable;
import java.util.List;

public interface GameWorld extends Serializable {
    int INIT_NUM_COLS_GAME_BOARD =11;
    int INIT_NUM_ROWS_GAME_BOARD =11;
    void initializeGame(List<Player> players, int numberOfRemainingTiles, List<Tile> allTiles, List<Integer> listOfRemainType);
    List<Integer> update();

    boolean putTile(Position position) throws IllegalArgumentException;

    int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile);

    int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile);

    boolean canPutFollowerInPosition(Position position);

    boolean canPlayerPutAFollower();

    int getRemainingTiles();

    int getRemainingTypes();

    boolean checkPositionInTileForFollower(Position positionInGameBoard, Position positionInTile);

    void setTileWithFollowerInCount(Tile tileWithFollowerInCount);

    List<Integer> finishGame();

    Player getCurrentPlayer();

    Tile getNextTile();

    List<Integer> isFinished();

    void addPlayer(Player player);
    void rotateNextTile();

    void changeNextTile();
    List<Player> getPlayersInfo();
    public Tile[][] getGameBoard();
}
