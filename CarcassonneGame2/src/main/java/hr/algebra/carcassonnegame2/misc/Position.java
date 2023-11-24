package hr.algebra.carcassonnegame2.misc;

import hr.algebra.carcassonnegame2.model.RelativePositionGrid;

import java.io.Serial;
import java.io.Serializable;

public final class Position implements Comparable<Position>, Serializable {
    private final int col;
    private final int row;

    @Serial
    private static final long serialVersionUID = 2L;

    public Position(int col, int row){
        this.col=col;
        this.row=row;
    }

    public Position(Position point) {
        this.col=point.col;
        this.row=point.row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Position getPositionInRight(){
        return new Position(col+1, row);
    }
    public Position getPositionInLeft(){
        return new Position(col-1, row);
    }
    public Position getPositionInTop(){
        return new Position(col, row-1);
    }
    public Position getPositionInBottom(){
        return new Position(col, row+1);
    }

    public Position castRelativePositionToPoint(RelativePositionGrid relativePositionGrid){
        Position point=null;
        switch (relativePositionGrid){
            case RIGHT -> point= getPositionInRight();
            case LEFT -> point= getPositionInLeft();
            case TOP -> point= getPositionInTop();
            case BOTTOM -> point= getPositionInBottom();
        }
        return point;
    }
    @Override
    public boolean equals(Object obj) {
        return obj!=null && ((Position) obj).col == col && ((Position) obj).row == row;
    }

    @Override
    public int compareTo(Position o) {
        if(col > o.col || col == o.col && row > o.row){
            return 1;
        }else if(col == o.col && row == o.row){
            return 0;
        }
        return -1;
    }
}
