package hr.algebra.carcassonnegame2.model;

import hr.algebra.carcassonnegame2.factories.PlayerFactory;
import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.model.tile.TileDescription;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static hr.algebra.carcassonnegame2.utils.XmlUtils.createElement;
import static hr.algebra.carcassonnegame2.utils.XmlUtils.getChildElementText;

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

    public GameMove(Player player, Position position, LocalDateTime dateTime, TileDescription tileDescription) {
        this.player = player;
        this.position = position;
        this.time = dateTime;
        this.tileDescription = tileDescription;
    }

    public GameMove() {
        player = null;
        position = null;
        tileDescription = null;
        time = null;
    }

    @Override
    public String toString() {
        if (player == null) {
            return "";
        }
        return player.getName()
                + " [" + time.getHour() + ":" + time.getMinute() + "]: "
                + tileDescription.getDescription() + " in "
                + position.toString();
    }

    public void toXml(Element gameMoveElement, Document document, DateTimeFormatter formatter) {

        gameMoveElement.appendChild(createElement(document, "player",
                player.getName()));
        gameMoveElement.appendChild(createElement(document, "position",
                position.toString()));
        gameMoveElement.appendChild(createElement(document, "dateTime",
                time.format(formatter)));
        tileDescription.toXml(gameMoveElement, document);
    }

    public static GameMove fromXml(Element childElement, DateTimeFormatter formatter) {
        String playerName = getChildElementText(childElement, "player");
        String positionString = getChildElementText(childElement, "position");
        String dateTimeString = getChildElementText(childElement, "dateTime");

        Player player = PlayerFactory.createPlayer(playerName);
        Position position = Position.parsePosition(positionString);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        return new GameMove(player, position, dateTime, TileDescription.fromXml(childElement));
    }

    public Position getPosition() {
        return this.position;
    }

    public Tile getTile() {
        return tileDescription.createTile(position, player);
    }
}
