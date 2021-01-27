package com.kehua.xml.control.table.contextMenu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.Optional;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class ClearMenuItem<T> extends MenuItem<T> {


    public ClearMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "清空数据");

        this.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("清空数据");
            alert.setHeaderText(null);
            alert.setContentText("清空后不可恢复，确认清空数据");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK){
                tableView.getItems().clear();
            }
        });
    }

    @Override
    protected boolean getVisible() {
        return tableView.getItems().size() > 0;
    }
}
