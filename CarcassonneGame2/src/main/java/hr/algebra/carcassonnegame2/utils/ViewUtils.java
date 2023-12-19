package hr.algebra.carcassonnegame2.utils;

import javafx.scene.control.Alert;

public class ViewUtils {
    private ViewUtils(){}

    public static void sendAlert(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
