package hr.algebra.carcassonnegame2.model.gameobjects;

import hr.algebra.carcassonnegame2.misc.Position;

import java.util.TreeSet;

public interface Player {
    int getPoints();

    String getName();

    void putFollower(Position destiny);

    void removeFollower(Position position);

    int getNumberOfFollowers();

    int addPunctuation(int punctuation);

    TreeSet<Position> getPositionsOfFollowers();
}
