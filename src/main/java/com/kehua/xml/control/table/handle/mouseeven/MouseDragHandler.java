package com.kehua.xml.control.table.handle.mouseeven;

import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
@SuppressWarnings("rawtypes")
public class MouseDragHandler {

    public static TablePosition focusedCell = null;
    public static int[] begin = null;

    public static void initialize(TableView<?> table){
        table.setOnMouseDragEntered(new OnMouseDragEnteredHandler());
        table.setOnMouseDragOver(new OnMouseDragOverHandler());
        table.setOnMouseDragExited(new OnMouseDragExitedHandler());
    }
}
