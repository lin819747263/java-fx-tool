package com.kehua.xml.control.dialog;

import com.kehua.xml.utils.DictUtil;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
public class UserDefined1Dialog {

    @SuppressWarnings("rawtypes")
    public static <T> void build(TableView<T> tableView) {
        TablePosition tablePosition = tableView.getSelectionModel().getSelectedCells().get(0);
        TableColumn column = tableView.getVisibleLeafColumn(tablePosition.getColumn());

        Dialog dialog = new Dialog();
        VBox box = new VBox();

        ObservableList<Node> children = box.getChildren();
        DictUtil.getDictByType("自定义").forEach((key, value) -> {
            Label label = new Label((String) key);
            label.setOnMouseClicked(event -> {
                ObservableValue<?> observableValue = column.getCellObservableValue(tablePosition.getRow());
                if(observableValue instanceof StringProperty){
                    ((StringProperty) observableValue).set((String) value);
                }
                dialog.close();
            });

            HBox hBox = new HBox();
            hBox.getStylesheets().addAll("/css/sample.css");
            hBox.getStyleClass().addAll("user-defined-1-dialog-label");
            hBox.getChildren().add(label);

            children.add(hBox);
        });

        ScrollPane pane = new ScrollPane();
        pane.setContent(box);
        pane.setPrefHeight(300);

        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.managedProperty().bind(cancel.visibleProperty());
        cancel.setVisible(false);

        dialog.showAndWait();
    }
}
