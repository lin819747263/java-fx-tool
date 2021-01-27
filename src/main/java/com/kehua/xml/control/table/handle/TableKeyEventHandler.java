package com.kehua.xml.control.table.handle;

import com.kehua.xml.control.table.handle.keyevent.OnKeyPressedHandler;
import com.kehua.xml.control.table.handle.keyevent.OnKeyTypedHandler;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class TableKeyEventHandler {

    public static void initialize(TableView<?> table){
        table.setOnKeyPressed(new OnKeyPressedHandler());
        table.setOnKeyTyped(new OnKeyTypedHandler());
    }

}
