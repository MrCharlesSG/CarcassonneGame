package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.model.game.GameWorld;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

abstract class GameView {

    protected final double MIN_HEIGHT_COL = 10;
    protected final double MIN_WIDTH_COL = 10;
    protected static GameWorld game;
    protected static boolean viewEnable=true;
    protected static String KEY_NAME="main";
    public GameView(){}

    public static void updateGame(GameWorld game){
        GameView.game=game;
    }

    protected Button getButton(boolean isNextTile){
        Button btn = new Button();
        btn.setMinHeight(MIN_HEIGHT_COL*(isNextTile ? 0.20:5));
        btn.setMinWidth(MIN_WIDTH_COL*(isNextTile ? 0.20:5));
        return btn;
    }

    public abstract void updateView();

    public static void disableView(){
        viewEnable=false;
    }

    public static void enableView(){
        viewEnable=true;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

    public void initialize(){}

    protected void resizeGridPane(GridPane gridPane, int numberColRow, boolean isNextTile){
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        if(isNextTile){
            rowConstraints.setVgrow(Priority.ALWAYS);
            columnConstraints.setHgrow(Priority.ALWAYS);
        }else{
            rowConstraints.setMaxHeight(MIN_HEIGHT_COL* 5);
            columnConstraints.setMinWidth(MIN_WIDTH_COL* 5);
            rowConstraints.setMinHeight(MIN_HEIGHT_COL* 5);
            columnConstraints.setMinWidth(MIN_WIDTH_COL* 5);
        }
        for (int i = 0; i < numberColRow; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }
}

