package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.configuration.GameConfiguration;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerImpl;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import hr.algebra.carcassonnegame2.network.NetworkingUtils;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.IS_GAME_MODE_ONLINE;

public class PlayerFactory {
    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#3b82f6", "#f472b6", "#57534e"};
    private static int numDefaultPlayersCreated = 0;

    public static Player createPlayer(String name, int numberFollowersPerPlayer) {
        PlayerType playerType;
        if (!NetworkingUtils.isServerConnected() && IS_GAME_MODE_ONLINE) {
            playerType = PlayerType.SERVER;
        } else if (!NetworkingUtils.isClientConnected() && IS_GAME_MODE_ONLINE) {
            playerType = PlayerType.CLIENT;
        } else {
            playerType = PlayerType.DEFAULT;
            numDefaultPlayersCreated++;
        }
        return new PlayerImpl(name, numberFollowersPerPlayer, getStyle(playerType), playerType);
    }

    public static Player createDefaultPlayer() {
        return new PlayerImpl("Default", 1, getStyle(PlayerType.DEFAULT), PlayerType.DEFAULT);
    }

    private static String getStyle(PlayerType playerType) {
        if (playerType.isServer() || numDefaultPlayersCreated==1) {
            return colours[0];
        } else if (playerType.isClient() || numDefaultPlayersCreated==2) {
            return colours[1];
        }
        return colours[2];

    }
}
