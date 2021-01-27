package com.kehua.xml.utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertFactory {

    public static Optional<ButtonType> createAlert(String message){
        return createAlert(message, null,null);
    }

    public static Optional<ButtonType> createAlert(String message, Alert.AlertType type){
        return createAlert(message, null, type);
    }

    public static Optional<ButtonType> createAlert(String message, Node content, Alert.AlertType type){
        type = (type == null? Alert.AlertType.INFORMATION : type);
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        if (content != null){
            alert.setHeaderText(message);
            alert.getDialogPane().setContent(content);
        }
        return alert.showAndWait();
    }
}
