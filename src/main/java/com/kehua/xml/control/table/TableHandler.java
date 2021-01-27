package com.kehua.xml.control.table;

import com.kehua.xml.control.table.handle.TableColumnMouseHandle;
import com.kehua.xml.control.table.handle.TableKeyEventHandler;
import com.kehua.xml.control.table.handle.TableMouseEventHandler;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class TableHandler {

    public static <T> void initialize(TableView<T> table){
        TableKeyEventHandler.initialize(table);
        TableMouseEventHandler.initialize(table);
        TableColumnMouseHandle.initialize(table);
    }

}
