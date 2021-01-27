package com.kehua.xml.control.table.handle;

import com.kehua.xml.control.table.handle.mouseeven.MouseDragHandler;
import com.kehua.xml.control.table.handle.mouseeven.OnMousePressedHandler;
import com.kehua.xml.control.table.handle.mouseeven.OnMouseReleasedHandler;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class TableMouseEventHandler {

    public static void initialize(TableView<?> table){
        table.setOnMousePressed(new OnMousePressedHandler());
        table.setOnMouseReleased(new OnMouseReleasedHandler());
        MouseDragHandler.initialize(table);
    }

}
