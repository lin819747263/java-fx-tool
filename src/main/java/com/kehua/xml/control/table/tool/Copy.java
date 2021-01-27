package com.kehua.xml.control.table.tool;

import com.kehua.xml.utils.DataUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
public class Copy {

    public static final String SPAN_SIGN = "\t";
    public static final String ROW_SIGN = "\n";

    public static Class<?> copyDataClass;
    public static final List<Object> copyData = new ArrayList<>();

    /**
     * 获得表格中选中的项作为待复制项
     * @param tableView
     * @param <T>
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public static <T> String getCopyStr(TableView<T> tableView) {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        selectedCells.sorted(Comparator.comparingInt((ToIntFunction<TablePosition>) TablePositionBase::getRow).thenComparingInt(TablePosition::getColumn));
        Map<Integer, List<String>> copyList = new LinkedHashMap<>();
        selectedCells.forEach(cell -> {
            List<String> strings = copyList.computeIfAbsent(cell.getRow(), k -> new ArrayList<>());
            TableColumn<T, ?> column = tableView.getVisibleLeafColumn(cell.getColumn());
            Object value = column.getCellData(cell.getRow());
            if (value == null) {
                value = "";
            }
            strings.add(value.toString().trim());
        });

        List<String> strings = new ArrayList<>();
        copyList.forEach((rowIndex, row) -> {
            strings.add(StringUtils.join(row, SPAN_SIGN));
        });
        return StringUtils.join(strings, ROW_SIGN);
    }

    public static <T> void copy(TableView<T> tableView) {
        String copyStr = getCopyStr(tableView);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent cc = new ClipboardContent();
        cc.putString(copyStr);
        clipboard.setContent(cc);
    }

    public static <T> void copyRow(TableView<T> tableView, Class<T> dataClass){
        copyData.clear();
        TableView.TableViewSelectionModel<T> selectionModel = tableView.getSelectionModel();
        Set<Integer> rows = selectionModel.getSelectedCells().stream().map(TablePositionBase::getRow).collect(Collectors.toSet());
        selectionModel.clearSelection();
        rows.forEach(selectionModel::select);
        Integer[] rowsList = rows.toArray(new Integer[]{});
        Arrays.sort(rowsList, (o1, o2) -> o2 - o1);
        ObservableList<T> items = tableView.getItems();
        for (Integer row : rowsList) {
            try {
                copyData.add(DataUtils.clone(items.get(row)));
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        copyDataClass = dataClass;
    }

}
