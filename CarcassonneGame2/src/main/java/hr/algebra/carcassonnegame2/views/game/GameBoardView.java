package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.utils.ViewUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import static hr.algebra.carcassonnegame2.utils.ViewUtils.resizeGridPane;

final class GameBoardView extends GameView {
    private Button selectedPositionButton;
    private Position selectedPosition;
    private final GridPane gpGameBoard;
    private final static String KEY_NAME="GameBoard";

    public GameBoardView(GridPane gpGameBoard) {
        super();
        this.gpGameBoard=gpGameBoard;
    }

    @Override
    public void updateView() {
        paintGameBoard();
    }

    private void paintGameBoard() {
        gpGameBoard.setGridLinesVisible(true);
        Tile[][] gameBoard = game.getGameBoard();
        resizeGridPane(gpGameBoard, gameBoard.length, false, MIN_HEIGHT_COL, MIN_WIDTH_COL);
        completeBoard(gameBoard,gpGameBoard);
    }

    private void completeBoard(Tile[][] gameBoard, GridPane gpGameBoard) {
        for (int colPos = 0; colPos < gameBoard.length; colPos++) {
            for (int rowPos = 0; rowPos < gameBoard[colPos].length; rowPos++) {
                if(gameBoard[colPos][rowPos] != null){
                    Tile tile = gameBoard[colPos][rowPos];
                    GridPane gpTile = new GridPane();
                    resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, true, MIN_HEIGHT_COL, MIN_WIDTH_COL);
                    representTileInGridPane(tile, gpTile);
                    gpGameBoard.add(gpTile, colPos, rowPos);
                }else{
                    Button btnTile = getButton(false);
                    int finalCol = colPos;
                    int finalRow = rowPos;
                    btnTile.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
                    btnTile.setOnAction(actionEvent -> selectBoardPosition(new Position(finalCol, finalRow), btnTile));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    private void selectBoardPosition(Position point, Button btn) {
        if(viewEnable) {
            btn.setStyle("-fx-background-color: " + game.getCurrentPlayer().getStyle() + "; -fx-background-radius: 0;");
            if (selectedPositionButton != null && !selectedPosition.equals(point)) {
                selectedPositionButton.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;");
            }
            selectedPosition = point;
            selectedPositionButton = btn;
        }
    }

    private void representTileInGridPane(Tile tile, GridPane gridPane) {
        ViewUtils.representTileInGridPane(tile, gridPane);
    }

    public Position getSelecetedPosition() {
        return selectedPosition;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

}
