package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.dialog.AutoFillDialog;
import com.kehua.xml.control.table.handle.keyevent.OnKeyTypedHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class AutoFillMenuItem<T> extends MenuItem<T> {

    public AutoFillMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "自定义填充");

        this.setOnAction(event -> AutoFillDialog.build(tableView));
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected boolean getVisible() {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        TableColumn[] col = selectedCells.stream()
                .map(TablePosition::getTableColumn)
                .collect(Collectors.toSet())
                .toArray(new TableColumn[]{});
        return col.length == 1 && !OnKeyTypedHandler.IGNORE_TABLE_COLUMN_ID.contains(col[0].getId());
    }


}
