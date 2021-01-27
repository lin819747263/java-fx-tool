package com.kehua.xml.control.table.handle.mouseeven;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class OnMouseDragExitedHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof TableView) {
            MouseDragHandler.begin = null;
            MouseDragHandler.focusedCell = null;
        }
    }
}
