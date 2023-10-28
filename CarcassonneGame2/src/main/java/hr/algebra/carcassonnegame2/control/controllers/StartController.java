package hr.algebra.carcassonnegame2.control.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.carcassonnegame2.factories.FactoryPlayer;
import hr.algebra.carcassonnegame2.factories.FactoryTile;
import hr.algebra.carcassonnegame2.model.Game;
import hr.algebra.carcassonnegame2.model.gameobjects.Player;
import hr.algebra.carcassonnegame2.model.gameobjects.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartController {

    public static int MAX_NUM_PLAYERS=5;
    private final String jsonFileName = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    private final int numberOfFollowersPerPlayer = 7;

    /*
    FXML Objects
     */
    public TextField tfPlayer1;
    public TextField tfPlayer2;
    public Label lbErrorMessage;
    public TextField tfPlayer3;
    public TextField tfPlayer4;
    public TextField tfPlayer5;
    public TextField[] allTextField;

    public void createNewGame(){
        List<String> names = this.getNamesFromTextField();
        if(names.size()>=2){
            initializeGame(names);
            closeStartView();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/carcassonnegame2/views/gameView.fxml"));
                Parent root = loader.load();

                Stage gameStage = new Stage();
                gameStage.setTitle("Game");
                gameStage.setScene(new Scene(root));
                gameStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            lbErrorMessage.setText("At lest 2 players");
        }
    }

    private void initializeGame(List<String> names){
        try{
            //Create players
            List<Player> playerList = new ArrayList<Player>();
            for(String name: names){
                playerList.add(FactoryPlayer.INSTANCE.createPlayer(name, numberOfFollowersPerPlayer));
            }

            //Create list of tiles from json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(this.jsonFileName));
            int numberOfRemainingTiles = rootNode.get("total").asInt();
            JsonNode typesNode = rootNode.get("types");

            List<Tile> listOfTiles = new ArrayList<Tile>();
            List<Integer> listOfRemainType = new ArrayList<Integer>();

            if(typesNode.isArray()){
                for (JsonNode typeNode: typesNode){
                    listOfRemainType.add( typeNode.get("total").asInt());
                    Tile tile = FactoryTile.INSTANCE.createTile(typeNode.get("data"));
                    if(tile != null){
                        listOfTiles.add(tile);
                    }
                }
                Game.INSTANCE.initializeGame(listOfTiles, playerList, numberOfRemainingTiles, listOfRemainType);
            }
        }catch (Exception ignored) { //Cerrar App
        }

    }

    private void closeStartView(){
        Stage stage = (Stage) tfPlayer1.getScene().getWindow();
        stage.close();
    }

    public List<String> getNamesFromTextField(){
        allTextField = new TextField[MAX_NUM_PLAYERS];
        allTextField[0] = tfPlayer1;
        allTextField[1] = tfPlayer2;
        allTextField[2] = tfPlayer3;
        allTextField[3] = tfPlayer4;
        allTextField[4] = tfPlayer5;
        List<String> names = new ArrayList<>();
        for (TextField tf: this.allTextField){
            if(tf != null && !Objects.equals(tf.getText(), "")){
                names.add(tf.getText());
            }
        }
        return names;
    }

    public void loadGame(){
        lbErrorMessage.setText("Load Game Function Not Implemented Yet");
    }
}