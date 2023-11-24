package hr.algebra.carcassonnegame2.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.carcassonnegame2.control.controllers.GameController;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.player.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.tile.Tile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameFactory {

    public static Game createGame(String[] playersNames, String jsonFileName, int numberOfFollowersPerPlayer) throws IllegalArgumentException{
        try{

            //Create players
            List<Player> playerList = new ArrayList<>();
            for(String name: playersNames){
                playerList.add(PlayerFactory.createPlayer(name, numberOfFollowersPerPlayer));
            }

            //Create list of tiles from json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFileName));
            int numberOfRemainingTiles = rootNode.get("total").asInt();
            JsonNode typesNode = rootNode.get("types");

            //Create game for tiles
            Game game = new Game(playerList, numberOfRemainingTiles);

            List<Tile> listOfTiles = new ArrayList<Tile>();
            List<Integer> listOfRemainType = new ArrayList<Integer>();
            if(typesNode.isArray()){
                for (JsonNode typeNode: typesNode){
                    listOfRemainType.add( typeNode.get("total").asInt());
                    Tile tile = TileFactory.createTile(typeNode.get("data"), game);
                    if(tile != null){
                        listOfTiles.add(tile);
                    }
                }
            }
            game.initializeGame(listOfTiles, listOfRemainType);
            return game;
        }catch (Exception ignored) {
            throw new IllegalArgumentException("Something went wrong");
        }
    }
}
