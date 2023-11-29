package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.model.chat.RemoteChatServiceImpl;

import java.awt.*;

final class ChatView {

    private final RemoteChatServiceImpl chat;

    public ChatView(RemoteChatServiceImpl chat){
        this.chat=chat;
    }

    public void updateChat(TextArea textArea){
        textArea.setText(chat.getAllMessagesText());
    }
}
