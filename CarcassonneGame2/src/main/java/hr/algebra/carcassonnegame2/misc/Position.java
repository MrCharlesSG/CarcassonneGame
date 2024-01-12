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

    @Override
    public String toString() {
        return "[" + col +
                ", " + row +
                ']';
    }

    public static Position parsePosition(String string) {
        if (string.startsWith("[") && string.endsWith("]")) {
            try {
                String[] values = string.substring(1, string.length() - 1).split(", ");

                int col = Integer.parseInt(values[0]);
                int row = Integer.parseInt(values[1]);

                return new Position(col, row);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error parsing position: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid position format: " + string);
        }

        // Return null if parsing fails
        return null;
    }
}
