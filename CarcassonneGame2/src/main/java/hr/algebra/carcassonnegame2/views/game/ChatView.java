package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.model.chat.Message;
import hr.algebra.carcassonnegame2.model.chat.RemoteChatService;
import hr.algebra.carcassonnegame2.views.start.StartViewsManager;
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

    public void sendMessage(Message message) {
        try {
            chat.sendChatMessage(message);
            updateChat();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateChat(){
        try {
            taChat.clear();
            List<Message> messages = chat.getAllChatMessages();
            for (Message message: messages) {
                taChat.appendText(getChatMessage(message) + "\n");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private String getChatMessage(Message message) {
        return message.getMessage();
    }
}
