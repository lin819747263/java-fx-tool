package com.kehua.xml.control.table.callback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HuangTengfei
 * 2020/12/3
 * description：表头排序
 */
public class SortCallBack<T> implements Callback<TableView<T>, Boolean> {

    static final List<String> IGNORE_TABLE_COLUMN_ID = new ArrayList<String>() {{
        add("num");
        add("funCode");
        add("collectFuncode");
    }};

    private TableColumn<T, ?> funCode;

    public SortCallBack(TableView<T> tableView) {
        tableView.getColumns().forEach(column -> {
            if (IGNORE_TABLE_COLUMN_ID.contains(column.getId())) {
                column.sortableProperty().setValue(false);
                if (column.getId().equals("funCode")) {
                    funCode = column;
                }
            }
        });
    }

    @Override
    public Boolean call(TableView<T> tableView) {
        ObservableList<TableColumn<T, ?>> sortOrder = tableView.getSortOrder();
        if (sortOrder.size() > 0) {
            Map<T, T> funCodeMap = new HashMap<>();
            if (funCode != null){
                AtomicReference<T> lastItem = new AtomicReference<>();
                tableView.getItems().forEach(item -> {
                    if (lastItem.get() == null){
                        lastItem.set(item);
                        return;
                    }
                    Object cellData = funCode.getCellData(item);
                    if (cellData == null || cellData.toString().length() == 0){
                        funCodeMap.put(item, lastItem.get());
                    }else {
                        lastItem.set(item);
                    }
                });
            }

            TableColumn<T, ?> column = sortOrder.get(0);
            FXCollections.sort(tableView.getItems(), (item0, item1) -> {
                if (funCodeMap.size() > 0){
                    T t0 = funCodeMap.get(item0);
                    T t1 = funCodeMap.get(item1);
                    item0 = t0 == null ? item0 : t0;
                    item1 = t1 == null ? item1 : t1;

                    if (item0.equals(item1)) {
                        return 0;
                    }
                }

                int sortType = TableColumn.SortType.ASCENDING.equals(column.getSortType()) ? 1 : -1;
                Object o0 = column.getCellData(item0);
                Object o1 = column.getCellData(item1);
                //  空值
                if (o0 == null) {
                    return -sortType;
                }
                if (o1 == null) {
                    return sortType;
                }

                String s0 = o0.toString();
                String s1 = o1.toString();

                return compare(s0, s1) * sortType;
            });
        }
        return true;
    }

    private int compare(String s0, String s1) {
        Pattern pattern = Pattern.compile("([^\\d]*)([\\d]+)");
        Matcher m0 = pattern.matcher(s0);
        Matcher m1 = pattern.matcher(s1);
        while (m0.find() && m1.find()) {
            String str0 = m0.group(1);
            String str1 = m1.group(1);
            String numStr0 = m0.group(2);
            String numStr1 = m1.group(2);
            int compare = str0.compareToIgnoreCase(str1);
            if (compare != 0) {
                return compare;
            }
            double num0 = Double.parseDouble(numStr0);
            double num1 = Double.parseDouble(numStr1);
            compare = Double.compare(num0, num1);
            if (compare != 0) {
                return compare;
            }
        }
        return s0.compareToIgnoreCase(s1);
    }

}
