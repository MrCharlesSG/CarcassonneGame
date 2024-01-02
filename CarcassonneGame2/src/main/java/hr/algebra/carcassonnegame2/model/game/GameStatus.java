package hr.algebra.carcassonnegame2.model.game;

import hr.algebra.carcassonnegame2.configuration.GameConfiguration;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.Serializable;
import java.util.List;

public interface GameStatus extends Serializable {
    void changeNextTile();

    boolean isGameFinished();

    List<Player> getPlayers();

    Tile[][] getGameBoard();

    Tile getNextTile();

    void update();

    List<Integer> getWinner();

    void putTile(Position position, Tile newTile);

    void finishGame();

    Player getCurrentPlayer();

    int getRemainingTypes();

    int getRemainingTiles();

    void rotateNextTile();

    int getNumColsGameBoard();

    int getNumRowsGameBoard();

    void addPlayer(Player player);
}
