package hr.algebra.carcassonnegame2.model;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.Serializable;
import java.util.List;

public interface GameWorld extends Serializable {
    void initializeGame(List<Tile> allTiles, List<Integer> listOfRemainType);

    void setNextTile();

    void changeNextTile();

    boolean isGameIsFinished();

    List<Player> getPlayersInfo();

    Tile[][] getGameBoard();

    Tile getNextTile();

    List<Integer> update();

    boolean putTile(Position position) throws IllegalArgumentException;

    int countCitiesForTile(Position positionInGameBoardOfOtherTile, Position positionInsideOtherTile);

    int countPathForClosingPath(Position positionInGameBoard, Position positionInsideTile);

    boolean checkPositionTileCorrect(Position position);

    void rotateNextTile();

    void setNextPlayer();

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
}
