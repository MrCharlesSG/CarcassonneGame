package hr.algebra.carcassonnegame2.model.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteChatService extends Remote {
    String REMOTE_CHAT_OBJECT_NAME = "hr.algebra.rmi.chat.service";
    void sendChatMessage(Message chatMessage) throws RemoteException;;
    List<Message> getAllChatMessages() throws RemoteException;;
}
