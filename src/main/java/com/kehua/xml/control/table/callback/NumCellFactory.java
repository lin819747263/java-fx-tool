package com.kehua.xml.control.table.callback;

import com.kehua.xml.control.table.TableUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.*;

/**
 * HuangTengfei
 * 2020/12/9
 * description：序号工厂
 */
public class NumCellFactory<T> implements Callback<TableColumn<T, String>, TableCell<T, String>> {

    public static class TableCell<T> extends javafx.scene.control.TableCell<T, String> {
        @Override
        public void updateItem(String item, boolean empty) {
            this.setGraphic(null);
            if (!empty) {
                numTableCellMap.put(this.getIndex(), this);
                int rowIndex = this.getIndex() + 1;
                item = String.valueOf(rowIndex);
                this.setText(item);
            } else {
                numTableCellMap.remove(this.getIndex());
                this.setText(null);
            }

            super.updateItem(item, empty);
        }
    }

    private final TableView<T> table;
    private static Map<Integer, TableCell<?>> numTableCellMap = new HashMap<>();

    public NumCellFactory(TableView<T> table) {
        this.table = table;
    }

    @Override
    public javafx.scene.control.TableCell<T, String> call(TableColumn<T, String> param) {
        TableCell<T> cell = new TableCell<>();

        cell.setOnDragDetected(event -> {
            Integer[] rows = table.getSelectionModel().getSelectedIndices().toArray(new Integer[]{});
            if (rows.length > 0) {
                Dragboard dragboard = table.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                cc.put(TableUtil.SERIALIZED_MIME_TYPE, rows);
                dragboard.setContent(cc);
                event.consume();
            }
        });

        cell.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(TableUtil.SERIALIZED_MIME_TYPE)) {
                Integer[] rows = (Integer[]) db.getContent(TableUtil.SERIALIZED_MIME_TYPE);
                boolean has = false;
                for (Integer row : rows) {
                    if (cell.getIndex() == row) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            }
        });

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(TableUtil.SERIALIZED_MIME_TYPE)) {
                Integer[] draggedIndexs = (Integer[]) db.getContent(TableUtil.SERIALIZED_MIME_TYPE);
                Arrays.sort(draggedIndexs, (o1, o2) -> o2 - o1);
                TableView.TableViewSelectionModel<T> selectionModel = table.getSelectionModel();
                selectionModel.clearSelection();
                int dropIndex;
                List<T> items = new ArrayList<>();
                for (int draggedIndex : draggedIndexs) {
                    items.add(table.getItems().remove(draggedIndex));
                }
                dropIndex = Math.min(cell.getIndex(), table.getItems().size());
                for (T t : items) {
                    table.getItems().add(dropIndex, t);
                    selectionModel.select(dropIndex);
                }
                event.setDropCompleted(true);
                event.consume();
            }
        });

        return cell;
    }
}
