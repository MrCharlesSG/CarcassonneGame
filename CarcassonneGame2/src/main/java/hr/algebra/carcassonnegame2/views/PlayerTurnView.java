package hr.algebra.carcassonnegame2.views;

import hr.algebra.carcassonnegame2.model.player.Player;
import javafx.scene.control.Label;

final class PlayerTurnView extends GameView{
    private final Label lbPlayerTurn;

    private static final String trueColor = "#10B981";
    private static final String falseColor = "#E11D48";
    private static final String falseText = "Not Your Turn";
    private static final String trueText = "Your Turn";
    private static final String style="-fx-background-color: ";
    public PlayerTurnView(Label lbPlayerTurn) {
        super();
        this.lbPlayerTurn=lbPlayerTurn;
    }

    @Override
    public void updateView() {
        if(GameView.viewEnable){
            setPlayerTurnTrue();
        }else{
            setPlayerTurnFalse();
        }
    }

    private void setPlayerTurnTrue() {
        lbPlayerTurn.setStyle(style+trueColor);
        lbPlayerTurn.setText(trueText);
    }

    private void setPlayerTurnFalse() {
        lbPlayerTurn.setStyle(style+falseColor);
        lbPlayerTurn.setText(falseText);
    }
}
