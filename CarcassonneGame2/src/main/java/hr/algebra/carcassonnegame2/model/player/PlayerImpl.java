package hr.algebra.carcassonnegame2.model.player;

import hr.algebra.carcassonnegame2.misc.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerImpl implements Player, Serializable {

    @Serial
    private static final long serialVersionUID = 3L;
    private final PlayerType type;
    private int points;
    private int numberOfFollowers;

    private final List<Position> followersTiles;

    private final String styleColor;

    public PlayerImpl(String name, int numberOfFollowers, String styleColor){
        this.numberOfFollowers = numberOfFollowers;
        this.points = 0;
        this.type = PlayerType.valueOf(name);
        this.followersTiles=new ArrayList<>();
        this.styleColor=styleColor;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public String getName() {
        return type.name();
    }

    @Override
    public void putFollower(Position destiny) {
        numberOfFollowers-=1;
        followersTiles.add(destiny);
    }

    @Override
    public void removeFollower(Position position) {
        this.numberOfFollowers++;
        this.followersTiles.remove(position);
    }

    @Override
    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    @Override
    public int addPunctuation(int punctuation) {
        this.points +=punctuation;
        return this.points;
    }

    @Override
    public List<Position> getPositionsOfFollowers() {
        return followersTiles;
    }

    @Override
    public String getTextColor() {
        return this.styleColor;
    }

    @Override
    public boolean isServer() {
        return type.isServer();
    }

    @Override
    public boolean equals(Object obj) {
        return ((Player) obj).isServer() && this.isServer() || !((Player) obj).isServer() && !this.isServer() ;
    }
}
