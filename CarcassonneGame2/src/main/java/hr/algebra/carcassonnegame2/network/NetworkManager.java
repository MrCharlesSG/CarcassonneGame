package hr.algebra.carcassonnegame2.network;

import hr.algebra.carcassonnegame2.control.controllers.StartViewController;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatServiceImpl;
import hr.algebra.carcassonnegame2.control.controllers.GameController;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public final class NetworkManager {

    private static RemoteChatService chatService;

    public static RemoteChatService getChatService(){
        return chatService;
    }

    public static void start() throws Exception {
        if(!NetworkingUtils.isServerConnected()) {
            new Thread(NetworkManager::startServer).start();
        }
        else if (!NetworkingUtils.isClientConnected()){
            new Thread(NetworkManager::startClient).start();
        }else{
            throw new Exception("No posibility to play online");
        }
    }

    public static void sendGame(PlayerType player, GameWorld game){
        if(player.isServer()){
            NetworkingUtils.sendGameToClient(game);
        }else {
            NetworkingUtils.sendGameToServer(game);
        }
    }

    private static void startServer() {
        startRmiServer();
        acceptRequestsOnPort(NetworkConfiguration.SERVER_PORT);
    }

    private static void startClient() {
        Platform.runLater(() -> StartViewController.startGameView(null));
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
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())){
            GameWorld game = (GameWorld) ois.readObject();
            if(StartViewController.isViewUpdated()){
                Platform.runLater(() -> StartViewController.startGameView(game));
            }else {
                Platform.runLater(() -> GameController.restoreGame(game));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void startRmiServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(NetworkConfiguration.RMI_PORT);
            chatService = new RemoteChatServiceImpl();
            RemoteChatService skeleton = (RemoteChatService)
                    UnicastRemoteObject.exportObject(chatService,
                            NetworkConfiguration.RANDOM_PORT_HINT);
            registry.rebind(RemoteChatService.REMOTE_CHAT_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        }
        catch(RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public static void startRmiClient(){
        try {
            Registry registry = LocateRegistry.getRegistry(
                    NetworkConfiguration.HOST_NAME,
                    NetworkConfiguration.RMI_PORT
            );
            GameController.setChat ((RemoteChatService) registry.lookup(RemoteChatService.REMOTE_CHAT_OBJECT_NAME));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
