package hr.algebra.carcassonnegame2.misc;

import hr.algebra.carcassonnegame2.model.player.Player;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public final class ScoreboardUnit {
    public Label lbName;
    public Label lbPoints;
    public Label lbFollowers;
    public Circle sphere;

    public ScoreboardUnit(Label lbName, Label lbPoints, Label lbFollowers, Circle sphere){
        this.lbName=lbName;
        this.lbPoints = lbPoints;
        this.lbFollowers = lbFollowers;
        this.sphere=sphere;
    }

    public void updateScoreBoard(int points, int followers){
        this.lbFollowers.setText(Integer.toString(followers));
        this.lbPoints.setText(Integer.toString(points));
    }

    public void initializePlayerInfo(Player player) {
        lbName.setText(player.getName());
        sphere.setStyle("-fx-fill: "+player.getTextColor());
    }
}
