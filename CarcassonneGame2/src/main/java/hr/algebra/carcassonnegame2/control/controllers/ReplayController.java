package hr.algebra.carcassonnegame2.control.controllers;

import hr.algebra.carcassonnegame2.Main;
import hr.algebra.carcassonnegame2.factories.TileFactory;
import hr.algebra.carcassonnegame2.model.GameMove;
import hr.algebra.carcassonnegame2.model.tile.Tile;
import hr.algebra.carcassonnegame2.utils.GridUtils;
import hr.algebra.carcassonnegame2.utils.ViewUtils;
import hr.algebra.carcassonnegame2.utils.XmlUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static hr.algebra.carcassonnegame2.utils.GridUtils.resizeGrid;
import static hr.algebra.carcassonnegame2.utils.ViewUtils.representTileInGridPane;
import static hr.algebra.carcassonnegame2.utils.ViewUtils.resizeGridPane;

public class ReplayController implements Initializable {
    private static final int NUM_COLS_ROW_GAME_BOARD_INI = 11;
    private static final String STYLE_START = "-fx-background-color: ";
    private static final String NON_SELECTED_COLOR = " #94a3b8;";
    private static final String SELECTED_COLOR = "#ca8a04;";
    private static final double MIN_SIZE = 10;
    public GridPane gpGameBoard;
    public Button btnFinish;
    public Button btnPrevious;
    public Button btnStop;
    public Button btnPlay;
    public Button btnNext;
    private Timeline timeline;
    private static List<GameMove> gameMoves;
    private int currentMove =0;
    private Tile[][] gameBoard;
    private Status status;
    private static Stage stage;

    private int numColsRowsGameBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!gameMoves.isEmpty()) {
            this.gameBoard = new Tile[NUM_COLS_ROW_GAME_BOARD_INI][NUM_COLS_ROW_GAME_BOARD_INI];
            setNumColsRowsGameBoard(NUM_COLS_ROW_GAME_BOARD_INI);
            playNextGameMove();
            onPlayAction();
        }
    }

    public static boolean canStart() {
        gameMoves = XmlUtils.readGameMoves();
        return !gameMoves.isEmpty();
    }

    public static void setStage(Stage stage){
        ReplayController.stage=stage;
    }

    private void startStartView() {
        try {
            Stage stage = new Stage();
            Main.setStage(stage);
            StartViewController.setStage(stage);
            stage.setTitle("Initialize Carcassonne Game");
            stage.setWidth(420);
            stage.setHeight(300);
            Image icon = new Image(Objects.requireNonNull(Main.class.getResource("/hr/algebra/carcassonnegame2/images/icon.png")).toExternalForm());
            stage.getIcons().add(icon);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/hr/algebra/carcassonnegame2/views/startView.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNumColsRowsGameBoard(int numColsRowGameBoard) {
        this.numColsRowsGameBoard= numColsRowGameBoard;
        Tile[][] newGameBoard = new Tile[numColsRowsGameBoard][numColsRowsGameBoard];
        gameBoard = resizeGrid(gameBoard, newGameBoard);
        resizeGridPane(gpGameBoard, numColsRowGameBoard, false, MIN_SIZE , MIN_SIZE);
    }

    public void onFinishAction() {
        stage.close();
        startStartView();
    }

    public void onPreviousAction() {
        stopTimeline();
        changeStatus(Status.MANUAL);
        int limitCurrentMove = currentMove-1 <= 0 ? gameMoves.size():currentMove-1;
        currentMove=0;
        reset();
        for (int i = 0; i < limitCurrentMove-1; i++) {
            playNextGameMove();
        }
    }

    private void changeStatus(Status status) {
        this.status=status;
        btnNext.setStyle(STYLE_START+ (status == Status.MANUAL ? SELECTED_COLOR : NON_SELECTED_COLOR ));
        btnPlay.setStyle(STYLE_START+ (status == Status.PLAYING ? SELECTED_COLOR : NON_SELECTED_COLOR ));
        btnPrevious.setStyle(STYLE_START+ (status == Status.MANUAL ? SELECTED_COLOR : NON_SELECTED_COLOR ));
        btnStop.setStyle(STYLE_START+ (status == Status.PAUSED ? SELECTED_COLOR : NON_SELECTED_COLOR ));
    }

    public void onPlayAction() {
        stopTimeline();
        changeStatus(Status.PLAYING);
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        event -> playNextGameMove()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void onStopAction() {
        stopTimeline();
        changeStatus(Status.PAUSED);
    }

    private void stopTimeline() {
        if(status == Status.PLAYING){
            timeline.stop();
        }
    }

    private void playNextGameMove() {
        doNextGameMove();
        completeBoard();
    }

    private void doNextGameMove() {
        if (currentMove < gameMoves.size()) {
            GameMove move = gameMoves.get(currentMove);
            GridUtils.setValueInPosition(gameBoard, move.getPosition(), move.getTile());
            if(GridUtils.needGridToResize(move.getPosition(), numColsRowsGameBoard)){
                setNumColsRowsGameBoard(numColsRowsGameBoard*2+1);
            }
            currentMove++;
        }else{
            currentMove=0;
            reset();
        }
    }

    private void reset() {
        gameBoard = new Tile[NUM_COLS_ROW_GAME_BOARD_INI][NUM_COLS_ROW_GAME_BOARD_INI];
        playNextGameMove();
    }

    private void completeBoard() {
        for (int colPos = 0; colPos < gameBoard.length; colPos++) {
            for (int rowPos = 0; rowPos < gameBoard[colPos].length; rowPos++) {
                Tile tile = gameBoard[colPos][rowPos];
                tile = tile==null? TileFactory.createDefaultTile() : tile;
                GridPane gpTile = new GridPane();
                resizeGridPane(gpTile, Tile.NUM_ROWS_TILE, true, MIN_SIZE, MIN_SIZE);
                representTileInGridPane(tile, gpTile);
                gpGameBoard.add(gpTile, colPos, rowPos);
            }
        }
    }



    public void onNextAction(ActionEvent actionEvent) {
        stopTimeline();
        playNextGameMove();
        changeStatus(Status.MANUAL);
    }

    private enum Status {
        PAUSED, MANUAL, PLAYING
    }
}
