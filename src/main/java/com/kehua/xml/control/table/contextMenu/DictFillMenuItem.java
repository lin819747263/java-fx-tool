package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.dialog.UserDefined1Dialog;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

public class DictFillMenuItem<T> extends MenuItem<T> {

    public DictFillMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "字典填充");
        this.setOnAction(event -> UserDefined1Dialog.build(tableView));
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected boolean getVisible() {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        return selectedCells.size() == 1 && isUserDefined1Column(tableView, selectedCells.get(0));
    }

    @SuppressWarnings({"rawtypes"})
    private boolean isUserDefined1Column(TableView<T> tableView, TablePosition position){
        TableColumn column = tableView.getVisibleLeafColumn(position.getColumn());
        return "自定义1".equals(column.getText());
    }
}
