package hr.algebra.carcassonnegame2.network;

import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.network.NetworkConfiguration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class NetworkingUtils {

    private NetworkingUtils(){}
    public static void sendGameToServer(GameWorld game) {
        sendGame(game, NetworkConfiguration.SERVER_PORT);
    }

    public static void sendGameToClient(GameWorld game) {
        sendGame(game, NetworkConfiguration.CLIENT_PORT);
    }

    private static void sendGame(GameWorld game, Integer port) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST_NAME, port))
        {
            sendSerializableRequest(clientSocket, game);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static void sendSerializableRequest(Socket client, GameWorld game) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(game);
    }

    public static boolean isClientConnected(){
        return isPortInUse(NetworkConfiguration.HOST_NAME, NetworkConfiguration.CLIENT_PORT);
    }
    public static boolean isServerConnected(){
        return isPortInUse(NetworkConfiguration.HOST_NAME, NetworkConfiguration.SERVER_PORT);
    }

    public static boolean isPortInUse(String host, int port) {
        try (Socket socket = new Socket()) {
            // Intenta conectarse al puerto
            socket.connect(new InetSocketAddress(host, port), 1000);
            return true;  // Si la conexión tiene éxito, el puerto está en uso
        } catch (Exception e) {
            return false;  // Si hay una excepción, el puerto no está en uso
        }
    }

}
