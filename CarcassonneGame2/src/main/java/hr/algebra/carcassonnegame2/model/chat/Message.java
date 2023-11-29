package hr.algebra.carcassonnegame2.model.chat;

import hr.algebra.carcassonnegame2.model.player.Player;

public class Message {
    private String text;
    private Player owner;

    public Message(String text, Player owner){
        this.text=text;
        this.owner=owner;
    }

    public String getMessageText(){
        return owner + ": " + text;
    }
}
