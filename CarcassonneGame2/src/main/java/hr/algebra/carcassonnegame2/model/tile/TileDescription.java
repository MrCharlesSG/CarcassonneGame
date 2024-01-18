package hr.algebra.carcassonnegame2.model.tile;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.player.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serial;
import java.io.Serializable;

import static hr.algebra.carcassonnegame2.utils.GridUtils.*;
import static hr.algebra.carcassonnegame2.utils.XmlUtils.createElement;
import static hr.algebra.carcassonnegame2.utils.XmlUtils.getChildElementText;

public class TileDescription implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
    private final TileElementValue[][] description;
    private final Position followerPosition;

    public TileDescription(Tile tile){
        this(tile.getRepresentation(), tile.getFollowerPosition());
    }

    private TileDescription(TileElementValue[][] description, Position followerPosition) {
        this.description= new TileElementValue[description.length][description.length];
        copyGrid(this.description, description);
        this.followerPosition = followerPosition;
    }

    public String getDescription(){
        return getTileLayoutDescription() + getAdditionalDescription();
    }

    private String getTileLayoutDescription(){
        return
                "Top:" + getTop(description) + ", " +
                        "Right:" + getRight(description) + ", " +
                        "Left:" + getLeft(description) + ", " +
                        "Bottom:" + getBottom(description) + ", " +
                        "Center:" + getCenter(description);
    }

    public String getAdditionalDescription(){
        String additional = "";
        if(followerPosition !=null){
            additional = "; Follower-> " + followerPosition + "; ";
        }
        return additional;
    }

    public void toXml(Element gameMoveElement, Document document) {
        if (followerPosition != null) {
            gameMoveElement.appendChild(createElement(document, "followerPosition",
                    followerPosition.toString()));
        }
        for (int i = 0; i < description.length; i++) {
            for (int j = 0; j < description[i].length; j++) {
                gameMoveElement.appendChild(createElement(document, getTag(i, j), description[i][j].name()));
            }
        }
    }

    public static TileDescription fromXml(Element childElement) {
        String followerPositionString = getChildElementText(childElement, "followerPosition");
        Position followerPosition = (followerPositionString != null) ? Position.parsePosition(followerPositionString) : null;

        TileElementValue[][] description = new TileElementValue[Tile.NUM_COLS_TILE][Tile.NUM_COLS_TILE];
        for (int i = 0; i < Tile.NUM_COLS_TILE; i++) {
            for (int j = 0; j < Tile.NUM_COLS_TILE; j++) {
                String tileElementValueString = getChildElementText(childElement, getTag(i, j));
                description[i][j] = TileElementValue.valueOf(tileElementValueString);
            }
        }
        return new TileDescription(description, followerPosition);
    }

    private static String getTag(int i, int j) {
        return "tileDes" + (char) ('A' + i)+ (char) ('A' + j);
    }

    public Tile createTile(Position position, Player player) {

        return new TileImpl(description, position, player, followerPosition);
    }
}
