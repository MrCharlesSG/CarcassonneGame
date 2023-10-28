module hr.algebra.carcassonnegame2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens hr.algebra.carcassonnegame2 to javafx.fxml;
    exports hr.algebra.carcassonnegame2;
    exports hr.algebra.carcassonnegame2.model;
    opens hr.algebra.carcassonnegame2.model to javafx.fxml;
    exports hr.algebra.carcassonnegame2.model.gameobjects;
    opens hr.algebra.carcassonnegame2.model.gameobjects to javafx.fxml;
    exports hr.algebra.carcassonnegame2.control.controllers;
    opens hr.algebra.carcassonnegame2.control.controllers to javafx.fxml;
    exports hr.algebra.carcassonnegame2.misc;
    opens hr.algebra.carcassonnegame2.misc to javafx.fxml;
}