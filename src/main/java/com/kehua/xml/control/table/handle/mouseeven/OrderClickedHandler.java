package com.kehua.xml.control.table.handle.mouseeven;

import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/2
 * description：序号点击时选择整行
 */
public class OrderClickedHandler {

    public static void initialize(MouseEvent event) {
        if (event.getSource() instanceof TableView) {
            try {
                TableView<?> tableView = (TableView<?>) event.getSource();
                TableView.TableViewSelectionModel<?> selectionModel = tableView.getSelectionModel();
                List<Integer> rows = selectionModel.getSelectedCells()
                        .stream()
                        .filter(tablePosition -> tablePosition.getTableColumn().getId().equals("num"))
                        .map(TablePositionBase::getRow)
                        .collect(Collectors.toList());
                if (rows.size() > 0) selectionModel.clearSelection();
                rows.forEach(selectionModel::select);
            } catch (NullPointerException ignored) {
            }
        }
    }

}
