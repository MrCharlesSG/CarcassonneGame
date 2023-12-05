package hr.algebra.carcassonnegame2.model.chat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteChatServiceImpl implements RemoteChatService{
    List<Message> chatMesagesList;

    public RemoteChatServiceImpl() {
        chatMesagesList = new ArrayList<>();
    }

    @Override
    public void sendChatMessage(Message chatMessage) throws RemoteException {
        chatMesagesList.add(chatMessage);
    }

    @Override
    public List<Message> getAllChatMessages() throws RemoteException {
        return chatMesagesList;
    }
}
