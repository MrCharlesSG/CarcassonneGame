package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.configuration.GameConfiguration;
import hr.algebra.carcassonnegame2.model.GameMove;
import hr.algebra.carcassonnegame2.model.game.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameMoveUtils {

    private GameMoveUtils(){}

    public static void saveMove(GameMove gameMove){

        List<GameMove> allMoves = getAllMoves();
        allMoves.add(gameMove);
        System.out.println(gameMove);
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GameConfiguration.MOVES_FILE_NAME))) {
            oos.writeObject(allMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<GameMove> getAllMoves(){
        List<GameMove> gameMoveList = new ArrayList<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GameConfiguration.MOVES_FILE_NAME))) {
            gameMoveList.addAll((List) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameMoveList;
    }

    public static GameMove getTheLastMove(){
        return getAllMoves().getLast();
    }

}
