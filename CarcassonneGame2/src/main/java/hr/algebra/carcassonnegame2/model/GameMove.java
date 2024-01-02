package hr.algebra.carcassonnegame2.model;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileDescription;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class GameMove implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private final Player player;

    private final TileDescription tileDescription;
    private final LocalDateTime time;
    private final Position position;

    public GameMove(Player player, Tile tile, Position position) {
        this.player = player;
        this.tileDescription = new TileDescription(tile);
        this.position = position;
        this.time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return player.getName()
                + " ["+time.getHour()+":"+time.getMinute() + "]: "
                + tileDescription.getDescription() +" in "
                + position.toString();
    }
}
