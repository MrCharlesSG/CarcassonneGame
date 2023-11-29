package hr.algebra.carcassonnegame2.network;

import hr.algebra.carcassonnegame2.Main;
import hr.algebra.carcassonnegame2.control.controllers.GameController;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {



    public static void startServer() {
        acceptRequestsOnPort(NetworkConfiguration.SERVER_PORT);
    }

    public static void startClient() {
        //acceptServerRequests();
        acceptRequestsOnPort(NetworkConfiguration.CLIENT_PORT);
    }

    private static void acceptRequestsOnPort(Integer port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                new Thread(() -> processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){
            Game game = (Game) ois.readObject();
            Platform.runLater(() -> GameController.restoreGame(game));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void start(PlayerType playerLoggedIn) {
        if(playerLoggedIn.isServer()) {
            playerLoggedIn = PlayerType.SERVER;
            GameController.setPlayer(playerLoggedIn);
            new Thread(NetworkManager::startServer).start();
        }
        else {
            playerLoggedIn = PlayerType.CLIENT;
            GameController.setPlayer(playerLoggedIn);
            new Thread(NetworkManager::startClient).start();
        }
    }
}
