package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.misc.Position;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

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
        resizeGridPane(gpGameBoard, gameBoard.length, false);
        completeBoard(gameBoard,gpGameBoard);
    }

    public void completeBoard(Tile[][] gameBoard, GridPane gpGameBoard) {
        for (int colPos = 0; colPos < gameBoard.length; colPos++) {
            for (int rowPos = 0; rowPos < gameBoard[colPos].length; rowPos++) {
                if(gameBoard[colPos][rowPos] != null){
                    Tile tile = gameBoard[colPos][rowPos];
                    GridPane gpTile = new GridPane();
                    resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, true);
                    representTileInGridPane(tile, gpTile);
                    gpGameBoard.add(gpTile, colPos, rowPos);
                }else{
                    Button btnTile = getButton(false);
                    int finalCol = colPos;
                    int finalRow = rowPos;
                    btnTile.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;" );
                    btnTile.setOnAction(actionEvent -> selectTilePosition(new Position(finalCol, finalRow), btnTile));
                    gpGameBoard.add(btnTile, colPos, rowPos);
                }
            }
        }
    }

    private void selectTilePosition(Position point, Button btn) {
        if(viewEnable) {
            btn.setStyle("-fx-background-color: " + game.getCurrentPlayer().getTextColor() + "; -fx-background-radius: 0;");
            if (selectedPositionButton != null && !selectedPosition.equals(point)) {
                selectedPositionButton.setStyle("-fx-background-color:  #F8FAFC" + "; -fx-background-radius: 0;");
            }
            selectedPosition = point;
            selectedPositionButton = btn;
        }
    }

    private void representTileInGridPane(Tile tile, GridPane gridPane) {
        gridPane.setGridLinesVisible(true);
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                StackPane pane = new StackPane();

                if(tile.isFollowerInPosition(new Position(col, row))){
                    Circle circle = new Circle(2);
                    circle.setStyle("-fx-fill: " + game.getFollowerInTileStyle(tile));
                    pane.getChildren().add(circle);
                    StackPane.setAlignment(circle, Pos.CENTER);
                }
                pane.setStyle(tile.getValuePosition(new Position(col, row)).getStyle());
                gridPane.add(pane, col, row);
            }
        }
    }

    public Position getSelecetedPosition() {
        return selectedPosition;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

}
