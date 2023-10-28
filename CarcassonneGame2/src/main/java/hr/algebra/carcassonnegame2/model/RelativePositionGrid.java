package hr.algebra.carcassonnegame2.model;

public enum RelativePositionGrid {
    LEFT, TOP,RIGHT, BOTTOM  /*, TOP_LEFT, TOP_RIGHT, BUTTON_LEFT, BUTTON_RIGHT*/;

    public RelativePositionGrid getNext(){
        if(this==LEFT) return TOP;
        else if (this==TOP) return RIGHT;
        else if (this==RIGHT) return BOTTOM;
        else return LEFT;

    }
}
