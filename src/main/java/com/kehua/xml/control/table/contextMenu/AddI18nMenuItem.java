package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.dialog.I18nDialog;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

public class AddI18nMenuItem<T> extends MenuItem<T> {

    public AddI18nMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "添加国际化");
        this.setOnAction(event -> I18nDialog.show(tableView));
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected boolean getVisible() {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        return selectedCells.size() == 1;
    }
}
