package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import static hr.algebra.carcassonnegame2.utils.ViewUtils.resizeGridPane;

final class NextTileView extends GameView{

    private Position followerPosition;
    private Button buttonWithFollower;
    private final GridPane gpNextTile;
    private final static String KEY_NAME="NextTile";

    public NextTileView(GridPane gpNextTile) {
        super();
        this.gpNextTile=gpNextTile;
    }

    @Override
    public void updateView() {
        paintNextTile();
    }
    private void paintNextTile() {
        resizeGridPane(gpNextTile, Tile.NUM_COLS_TILE, true, MIN_HEIGHT_COL, MIN_WIDTH_COL);
        Tile tile = game.getNextTile();
        int numberColRow = Tile.NUM_COLS_TILE;
        for (int row = 0; row < numberColRow; row++) {
            for (int col = 0; col < numberColRow; col++) {
                gpNextTile.add(getTileButton(tile, new Position(col, row)), col, row);
            }
        }
    }

    private Button getTileButton(Tile tile, Position point){
        Button btn = getButton(true);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setStyle(tile.getValuePosition(point).getStyle() + " -fx-background-radius: 0;" );
        btn.setOnAction((actionEvent -> selectPosition(point, btn)));
        return btn;
    }

    private void selectPosition(Position point, Button btn){
        if(viewEnable) {
            if (game.canPlayerPutAFollower()) {
                if (followerPosition == null) {
                    if (game.canPutFollowerInPosition(point)) {
                        btn.setText("o");
                        followerPosition = point;
                        buttonWithFollower = btn;
                    }
                } else if (!point.equals(followerPosition)) {
                    if (game.canPutFollowerInPosition(point)) {
                        buttonWithFollower.setText("");
                        buttonWithFollower = btn;
                        buttonWithFollower.setText("o");
                        followerPosition = point;
                    }
                } else {
                    btn.setText("");
                    followerPosition = null;
                }
            } else {
                GameViewsManager.sendAlert("Put Follower", "You can not put more followers", Alert.AlertType.WARNING);
            }
        }
    }

    public Position getFollowerPosition() {
        return followerPosition;
    }

    public void resetFollowerPosition() {
        followerPosition = null;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }
}
