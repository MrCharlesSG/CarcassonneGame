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

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.TILES_FILE_NAME;

public class GameFactory {

    public static GameWorld createGame() throws IllegalArgumentException{
        try{

            //Create players
            List<Player> playerList = new ArrayList<>();

            //Create list of tiles from json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(TILES_FILE_NAME));
            int numberOfRemainingTiles = rootNode.get("total").asInt();
            JsonNode typesNode = rootNode.get("types");

            //Create game for tiles
            GameWorld game = new Game();
            Tile.initializeTiles(game);
            List<Tile> listOfTiles = new ArrayList<>();
            List<Integer> listOfRemainType = new ArrayList<>();
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
