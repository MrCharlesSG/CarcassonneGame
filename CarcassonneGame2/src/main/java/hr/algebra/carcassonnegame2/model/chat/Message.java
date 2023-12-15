package hr.algebra.carcassonnegame2.model.chat;

import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public final class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 5L;
    private final String text;
    private final Player player;

    private final LocalDateTime localDateTime;

    public Message(String text, Player player){
        this.text=text;
        this.player=player;
        this.localDateTime= LocalDateTime.now();
    }

    public String getMessage(){
        return player.getName() + " ["+localDateTime.getHour()+":"+localDateTime.getMinute() + "]: " + text;
    }
}
