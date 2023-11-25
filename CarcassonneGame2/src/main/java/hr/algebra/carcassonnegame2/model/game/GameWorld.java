package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.List;

public interface GameWorld extends Serializable {
    int INIT_NUM_COLS_GAME_BOARD =11;
    int INIT_NUM_ROWS_GAME_BOARD =11;
    int PENALIZATION_FOR_CHANGING_TILE =-5;
    int POINTS_FOR_CITY = 2;
    int POINTS_FOR_MONASTERY = 9;
    int POINTS_FOR_PATH = 1;
    void changeNextTile();
    void initializeGame(List<Player> players, int numberOfRemainingTiles, List<Tile> allTiles, List<Integer> listOfRemainType);

    List<Player> getPlayersInfo();

    Tile[][] getGameBoard();

    List<Integer> update();

    boolean putTile(Position position) throws IllegalArgumentException;

    int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile);

    int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile);

    void rotateNextTile();

    boolean canPutFollowerInPosition(Position position);

    String getNextPlayerInfo();

    boolean canPlayerPutAFollower();

    int getRemainingTiles();

    int getRemainingTypes();

    boolean checkPositionInTileForFollower(Position positionInGameBoard, Position positionInTile);

    void setTileWithFollowerInCount(Tile tileWithFollowerInCount);

    List<Integer> finishGame();

    String getFollowerInTileStyle(Tile tile);

    Player getCurrentPlayer();

    Tile getNextTile();
}
