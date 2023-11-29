package hr.algebra.carcassonnegame2.model.chat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteChatServiceImpl implements RemoteChatService {

    private final List<Message> messages;

    public RemoteChatServiceImpl(){
        messages= new ArrayList<>();
    }

    public String getAllMessagesText() {
        StringBuilder builder = new StringBuilder();
        for (Message message: messages) {
            builder.append(message.getMessageText()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public void sendChatMessage(Message chatMessage) throws RemoteException {
        messages.add(chatMessage);
    }

    @Override
    public List<Message> getAllChatMessages() throws RemoteException {
        return messages;
    }
}
