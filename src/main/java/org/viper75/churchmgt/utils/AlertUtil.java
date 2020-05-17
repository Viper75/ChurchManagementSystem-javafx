package org.viper75.churchmgt.utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setGraphic(null);
        alert.show();
    }
}
