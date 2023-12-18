package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerImpl;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import hr.algebra.carcassonnegame2.network.NetworkingUtils;

public class PlayerFactory {

    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#3b82f6","#f472b6", "#57534e"};
    public static Player createPlayer(String name, int numberFollowersPerPlayer) {
        PlayerType playerType = PlayerType.DEFAULT;
        if(!NetworkingUtils.isServerConnected()){
            playerType=PlayerType.SERVER;
        }else if(!NetworkingUtils.isClientConnected()){
            playerType=PlayerType.CLIENT;
        }
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle(playerType), playerType);
    }

    public static Player createDefaultPlayer(){
        return new PlayerImpl("Default", 1, getStyle(PlayerType.DEFAULT), PlayerType.DEFAULT);
    }

    private static String getStyle(PlayerType playerType){
        if(playerType.isServer()){
            return colours[0];
        }else if(playerType.isClient()){
            return colours[1];
        }
        return colours[2];

    }
}
