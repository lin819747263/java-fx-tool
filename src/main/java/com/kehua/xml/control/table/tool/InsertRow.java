package com.kehua.xml.control.table.tool;

import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
public class InsertRow {

    /**
     * 插入行
     *
     * @param tableView
     * @param data       数据
     * @param <T>
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public static <T> int insertRow(TableView<T> tableView, List<T> data) {
        ObservableList<TablePosition> list = tableView.getSelectionModel().getSelectedCells();
        TablePosition position = list.get(list.size() - 1);
        ObservableList<T> items = tableView.getItems();
        int row;
        for (T datum : data) {
            try {
                if (position == null) {
                    row = items.size();
                }else {
                    row = position.getRow() + 1;
                }
                tableView.getItems().add(row, datum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data.size();
    }

}
