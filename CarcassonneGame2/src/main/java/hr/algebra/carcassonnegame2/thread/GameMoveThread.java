package hr.algebra.carcassonnegame2.thread;

import hr.algebra.carcassonnegame2.model.GameMove;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.MOVES_FILE_NAME;

public abstract class GameMoveThread {

    private static Boolean fileAccessInProgress = false;

    public synchronized void saveMove(GameMove gameMove) {
        startSynchronization();
        List<GameMove> allMoves = getAllMoves();
        allMoves.add(gameMove);
        System.out.println(gameMove);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MOVES_FILE_NAME))) {
            oos.writeObject(allMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
        endSynchronization();
    }

    private List<GameMove> getAllMoves() {
        List<GameMove> gameMoveList = new ArrayList<>();
        if(!isFileEmpty(MOVES_FILE_NAME)){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MOVES_FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    gameMoveList.addAll((List<GameMove>) obj);
                }
            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return gameMoveList;
    }

    public static boolean isFileEmpty(String fileName) {
        Path filePath = Paths.get(fileName);

        try {
            long size = Files.size(filePath);
            return size == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Tratamiento de error: devuelve false si hay algún problema al obtener el tamaño del archivo
        }
    }

    private synchronized void startSynchronization() {
        while (fileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        fileAccessInProgress = true;
    }

    private synchronized void endSynchronization() {
        fileAccessInProgress = false;
        notifyAll();
    }


    public synchronized GameMove getTheLastMove() {
        startSynchronization();
        List<GameMove> allMoves = getAllMoves();
        endSynchronization();
        if(allMoves.isEmpty())
            return new GameMove();
        return allMoves.getLast();
    }
}
