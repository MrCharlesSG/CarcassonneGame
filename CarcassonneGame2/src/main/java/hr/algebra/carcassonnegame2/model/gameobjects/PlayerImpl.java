package hr.algebra.carcassonnegame2.model.gameobjects;

import hr.algebra.carcassonnegame2.misc.Position;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.TreeSet;

public class PlayerImpl implements Player{

    private final StringProperty name;
    private IntegerProperty points;
    private IntegerProperty numberOfFollowers;

    private TreeSet<Position> followersTiles;

    public PlayerImpl(String name, int numberOfFollowers){
        this.numberOfFollowers = new SimpleIntegerProperty(numberOfFollowers);
        this.points = new SimpleIntegerProperty(0);
        this.name=new SimpleStringProperty(name);
        this.followersTiles=new TreeSet<>();
    }

    @Override
    public int getPoints() {
        return points.get();
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void putFollower(Position destiny) {
        numberOfFollowers.setValue(numberOfFollowers.get()-1);
        followersTiles.add(destiny);
    }

    @Override
    public void removeFollower(Position position) {
        this.numberOfFollowers.setValue(numberOfFollowers.get()+1);
        this.followersTiles.remove(position);
    }

    @Override
    public int getNumberOfFollowers() {
        return numberOfFollowers.get();
    }

    @Override
    public int addPunctuation(int punctuation) {
        this.points.setValue(this.points.get() + punctuation);
        return this.points.get();
    }

    @Override
    public TreeSet<Position> getPositionsOfFollowers() {
        return followersTiles;
    }

}
