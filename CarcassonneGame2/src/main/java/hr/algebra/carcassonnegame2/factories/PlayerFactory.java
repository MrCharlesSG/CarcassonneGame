package hr.algebra.carcassonnegame2.factories;

import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerImpl;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import hr.algebra.carcassonnegame2.network.NetworkingUtils;

import java.util.ArrayList;
import java.util.List;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.IS_GAME_MODE_ONLINE;
import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.NUM_FOLLOWERS_PER_PLAYER;

public class PlayerFactory {
    private static final String headOfStyle = "-fx-fill: ";
    private static final String[] colours = new String[]{"#3b82f6", "#f472b6", "#57534e"};
    private static int numDefaultPlayersCreated = 0;
    private static List<Player> players = new ArrayList<>();

    public static Player createPlayer(String name) {
        PlayerType playerType;
        if (!NetworkingUtils.isServerConnected() && IS_GAME_MODE_ONLINE) {
            playerType = PlayerType.SERVER;
        } else if (!NetworkingUtils.isClientConnected() && IS_GAME_MODE_ONLINE) {
            playerType = PlayerType.CLIENT;
        } else {
            playerType = PlayerType.DEFAULT;
            int pl = isAlreadyCreated(name);
            if (pl != -1) return players.get(pl);
            numDefaultPlayersCreated++;
        }
        Player player = new PlayerImpl(name, NUM_FOLLOWERS_PER_PLAYER, getStyle(playerType), playerType);
        if (player.getType().isDefault()) {
            players.add(player);
        }
        return player;
    }

    private static int isAlreadyCreated(String name) {
        int i = 0;
        while (players.size() > i && !players.get(i).getName().equals(name)) {
            i++;
        }
        return i != players.size() ? i : -1;
    }

    public static Player createDefaultPlayer() {
        return new PlayerImpl("Default", 1, getStyle(PlayerType.DEFAULT), PlayerType.DEFAULT);
    }

    private static String getStyle(PlayerType playerType) {
        if (playerType.isServer() || numDefaultPlayersCreated == 1) {
            return colours[0];
        } else if (playerType.isClient() || numDefaultPlayersCreated == 2) {
            return colours[1];
        }
        return colours[2];

    }
}
