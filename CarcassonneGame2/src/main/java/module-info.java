module hr.algebra.carcassonnegame2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.rmi;


    opens hr.algebra.carcassonnegame2 to javafx.fxml;
    exports hr.algebra.carcassonnegame2;
    exports hr.algebra.carcassonnegame2.model;
    opens hr.algebra.carcassonnegame2.model to javafx.fxml;
    exports hr.algebra.carcassonnegame2.control.controllers;
    opens hr.algebra.carcassonnegame2.control.controllers to javafx.fxml;
    exports hr.algebra.carcassonnegame2.misc;
    opens hr.algebra.carcassonnegame2.misc to javafx.fxml;
    opens hr.algebra.carcassonnegame2.model.player to javafx.fxml;
    exports hr.algebra.carcassonnegame2.model.player;
    opens hr.algebra.carcassonnegame2.model.tile to javafx.fxml;
    exports hr.algebra.carcassonnegame2.model.tile;
    opens hr.algebra.carcassonnegame2.model.game to javafx.fxml;
    exports hr.algebra.carcassonnegame2.model.game;
    opens hr.algebra.carcassonnegame2.model.chat to javafx.fxml;
    exports hr.algebra.carcassonnegame2.model.chat to java.rmi;
}