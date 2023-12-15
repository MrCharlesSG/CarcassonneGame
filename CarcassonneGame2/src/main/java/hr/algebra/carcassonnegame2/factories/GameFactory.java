package hr.algebra.carcassonnegame2.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.carcassonnegame2.model.game.Game;
import hr.algebra.carcassonnegame2.model.game.GameWorld;
import hr.algebra.carcassonnegame2.model.player.Player;
import hr.algebra.carcassonnegame2.model.player.PlayerType;
import hr.algebra.carcassonnegame2.model.tile.Tile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameFactory {

    private static final String jsonFileNameCity = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-city-test.json";
    private static final String jsonFileNameAll = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    private static final String jsonFileNameMonastery = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-monastery-test.json";
    private static final String jsonFileNamePath = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-path-test.json";
    private static final String jsonFileName = jsonFileNameAll;

    public static GameWorld createGame(String[] playersNames, int numberOfFollowersPerPlayer) throws IllegalArgumentException{
        try{

            //Create players
            List<Player> playerList = new ArrayList<>();
            for (int i = 0; i < playersNames.length && playersNames[i] != null; i++) {
                playerList.add(PlayerFactory.createPlayer(playersNames[i], numberOfFollowersPerPlayer, PlayerType.getElementByValue(i)));
            }

            //Create list of tiles from json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFileName));
            int numberOfRemainingTiles = rootNode.get("total").asInt();
            JsonNode typesNode = rootNode.get("types");

            //Create game for tiles
            GameWorld game = new Game();
            Tile.initializeTiles(game);
            List<Tile> listOfTiles = new ArrayList<Tile>();
            List<Integer> listOfRemainType = new ArrayList<Integer>();
            if(typesNode.isArray()){
                for (JsonNode typeNode: typesNode){
                    listOfRemainType.add( typeNode.get("total").asInt());
                    Tile tile = TileFactory.createTile(typeNode.get("data"));
                    if(tile != null){
                        listOfTiles.add(tile);
                    }
                }
            }
            game.initializeGame(playerList, numberOfRemainingTiles,listOfTiles, listOfRemainType);
            return game;
        }catch (Exception ignored) {
            throw new IllegalArgumentException("Something went wrong");
        }
    }
}
