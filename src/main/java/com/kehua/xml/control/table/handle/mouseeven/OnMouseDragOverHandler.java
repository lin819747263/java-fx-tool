package com.kehua.xml.control.table.handle.mouseeven;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseDragEvent;


/**
 * HuangTengfei
 * 2020/12/3
 * description：
 */
public class OnMouseDragOverHandler implements EventHandler<MouseDragEvent> {

    @Override
    @SuppressWarnings("rawtypes")
    public void handle(MouseDragEvent event) {
        if (event.getSource() instanceof TableView) {

            TableView<?> tableView = (TableView<?>) event.getSource();
            TableView.TableViewFocusModel<?> focusModel = tableView.getFocusModel();
            TablePosition focusedCell = focusModel.getFocusedCell();
            if (focusedCell.equals(MouseDragHandler.focusedCell) || MouseDragHandler.begin == null) {
                return;
            }

            MouseDragHandler.focusedCell = focusedCell;
            int[][] selectRange = getSelectRange(focusedCell);

            if (selectRange == null) {
                return;
            }

            select(tableView.getSelectionModel(), tableView.getVisibleLeafColumns(), selectRange);

        }
    }

    @SuppressWarnings("unchecked")
    private <S> void select(TableView.TableViewSelectionModel<S> selectionModel, ObservableList<? extends TableColumn<?, ?>> columns, int[][] selectRange) {

        ObservableList<? extends TableColumn<S, ?>> _columns = (ObservableList<? extends TableColumn<S, ?>>) columns;
        int[] rows = selectRange[0];
        int[] cols = selectRange[1];

        selectionModel.clearSelection();

        for (int row = rows[0]; row - rows[2] != rows[1]; row = row + rows[2]){
            for (int col = cols[0]; col - cols[2] != cols[1]; col = col + cols[2]){
                selectionModel.select(row, _columns.get(col));
            }
        }

    }

    @SuppressWarnings("rawtypes")
    private int[][] getSelectRange(TablePosition focusedCell) {
        Integer rowEnd = null;
        Integer colEnd = null;
        if (focusedCell != null) {
            rowEnd = focusedCell.getRow();
            colEnd = focusedCell.getColumn();
        }

        if (rowEnd == null) {
            return null;
        }

        int[] rows = getOrder(MouseDragHandler.begin[0], rowEnd);
        int[] cols = getOrder(MouseDragHandler.begin[1], colEnd);

        return new int[][]{rows, cols};
    }

    private int[] getOrder(int begin, int end) {
        if (begin > end) {
            return new int[]{begin, end, -1};
        } else {
            return new int[]{begin, end, 1};
        }
    }

}
