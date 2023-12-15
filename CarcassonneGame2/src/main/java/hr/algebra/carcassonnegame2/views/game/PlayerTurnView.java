package hr.algebra.carcassonnegame2.views.game;

import hr.algebra.carcassonnegame2.control.controllers.GameController;
import javafx.scene.control.Label;

final class PlayerTurnView extends GameView{
    private final Label lbPlayerTurn;

    private static final String trueColor = "#10B981";
    private static final String falseColor = "#E11D48";
    private static final String falseText = "Not Your Turn";
    private static final String trueText = "Your Turn";
    private static final String offlineText = "Offline Game";
    private static final String style="-fx-background-color: ";
    private static final String offlineColor = "#c084fc";

    public PlayerTurnView(Label lbPlayerTurn) {
        super();
        this.lbPlayerTurn=lbPlayerTurn;
    }

    @Override
    public void updateView() {
        if(!GameController.isOnline()){
            setOfflineTurn();
        }
        else if(GameView.viewEnable){
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

    private void setOfflineTurn() {
        lbPlayerTurn.setText(offlineText);
        lbPlayerTurn.setStyle(style+offlineColor);
    }
}
