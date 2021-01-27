package com.kehua.xml.control.table;

import com.kehua.xml.control.table.callback.SortCallBack;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/3
 * description：
 */
public class TablePolicy {
    public static <T> void initialize(TableView<T> table){
        table.setSortPolicy(new SortCallBack<>(table));
    }
}
