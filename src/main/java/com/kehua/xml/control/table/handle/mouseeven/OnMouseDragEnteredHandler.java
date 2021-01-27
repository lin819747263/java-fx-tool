package com.kehua.xml.control.table.handle.mouseeven;

import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseDragEvent;

/**
 * HuangTengfei
 * 2020/12/3
 * description：
 */
public class OnMouseDragEnteredHandler implements EventHandler<MouseDragEvent> {
    @Override
    @SuppressWarnings("rawtypes")
    public void handle(MouseDragEvent event) {
        if ( event.getSource() instanceof TableView){
            TableView<?> tableView = (TableView<?>) event.getSource();
            TablePosition focusedCell = tableView.getFocusModel().getFocusedCell();
            MouseDragHandler.begin = new int[]{focusedCell.getRow(), focusedCell.getColumn()};
        }
    }
}
