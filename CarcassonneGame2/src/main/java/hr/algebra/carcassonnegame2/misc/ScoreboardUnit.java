package hr.algebra.carcassonnegame2.misc;

import hr.algebra.carcassonnegame2.model.player.Player;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public final class ScoreboardUnit {
    private final Label lbName;
    private final Label lbPoints;
    private final Label lbFollowers;
    private final Circle sphere;
    private Player player;

    public ScoreboardUnit(Label lbName, Label lbPoints, Label lbFollowers, Circle sphere){
        this.lbName=lbName;
        this.lbPoints = lbPoints;
        this.lbFollowers = lbFollowers;
        this.sphere=sphere;
    }

    public void updateScoreBoard(int points, int followers){
        this.lbFollowers.setText(Integer.toString(followers));
        this.lbPoints.setText(Integer.toString(points));
        //lbName.setStyle( "-fx-background-color: " + (isTurn? player.getTextColor(): "transparent" ));
    }

    public void initializePlayerInfo(Player player) {
        this.player=player;
        lbName.setText(player.getName());
        sphere.setStyle("-fx-fill: "+player.getTextColor());
    }
}
