package com.kehua.xml.control.table.tool;

import com.kehua.xml.control.table.TableUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
public class Paste {

    public static String getClipboard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return clipboard.getContent(DataFormat.PLAIN_TEXT).toString();
    }

    public static <T> void paste(TableView<T> tableView) {
        paste(tableView, getClipboard());
    }

    @SuppressWarnings({"rawtypes"})
    public static <T> void paste(TableView<T> tableView, String content) {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() == 0) {
            return;
        }
        selectedCells.sorted(Comparator.comparingInt((ToIntFunction<TablePosition>) TablePositionBase::getRow).thenComparingInt(TablePosition::getColumn));
        List<List<String>> strings = Arrays.stream(content.split(Copy.ROW_SIGN)).map(str -> Arrays.asList(str.split(Copy.SPAN_SIGN))).collect(Collectors.toList());

        TablePosition position = selectedCells.get(0);
        AtomicInteger row = new AtomicInteger(position.getRow());

        //单个单元格复制多个单元格
        if(strings.size() == 1 && strings.get(0).size() == 1){
            for(TablePosition position1 : selectedCells){
                TableUtil.setItemValue(tableView, position1.getRow(), position1.getColumn(), strings.get(0).get(0));
                row.getAndIncrement();
            }
        //多个单元格复制多个单元格
        }else {
            int totalRows = tableView.getItems().size();
            int totalColumns = tableView.getColumns().size();

            AtomicInteger column;

            for(List<String> rows : strings){
                column = new AtomicInteger(position.getColumn());
                for(String cell : rows){
                    TableUtil.setItemValue(tableView, row.intValue(), column.intValue(), cell);
                    column.getAndIncrement();
                    if(column.intValue() == totalColumns) break;
                }
                row.getAndIncrement();
                if(row.intValue() == totalRows) break;
            }
        }


    }

}
