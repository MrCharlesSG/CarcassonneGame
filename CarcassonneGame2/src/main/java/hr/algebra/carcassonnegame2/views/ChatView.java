package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;
import java.util.List;

final class ChatView {

    private final RemoteChatService chat;

    private final TextArea taChat;

    public ChatView(RemoteChatService chat, TextArea textArea){
        this.chat=chat;
        taChat=textArea;
    }

    public void updateChat(){
        try {
            taChat.clear();
            List<Message> messages = chat.getAllChatMessages();
            for (Message message: messages) {
                taChat.appendText(message.getMessage() + "\n");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Message message) {
        try {
            chat.sendChatMessage(message);
            updateChat();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
