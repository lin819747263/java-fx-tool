package com.kehua.xml.control.table.contextMenu;

import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class DeleteRowMenuItem<T> extends MenuItem<T> {

    public DeleteRowMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "删除行");

        this.setOnAction(event -> {
            TableView.TableViewSelectionModel<T> selectionModel = tableView.getSelectionModel();
            Integer[] rows = selectionModel.getSelectedCells()
                    .stream()
                    .map(TablePositionBase::getRow)
                    .collect(Collectors.toSet())
                    .toArray(new Integer[]{});
            Arrays.sort(rows, (o1, o2) -> o2 - o1);
            for (Integer row : rows){
                tableView.getItems().remove(row.intValue());
            }
            selectionModel.clearSelection();
        });
    }

    @Override
    protected boolean getVisible() {
        return tableView.getSelectionModel().getSelectedCells().size() > 0;
    }
}
