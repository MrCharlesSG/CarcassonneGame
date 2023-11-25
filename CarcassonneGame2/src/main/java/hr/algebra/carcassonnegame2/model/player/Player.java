package hr.algebra.carcassonnegame2.model.player;

import hr.algebra.carcassonnegame2.misc.Position;

import java.util.List;

public interface Player{
    int getPoints();

    String getName();

    void putFollower(Position destiny);

    void removeFollower(Position position);

    int getNumberOfFollowers();

    int addPunctuation(int punctuation);

    List<Position> getPositionsOfFollowers();

    String getTextColor();
}
