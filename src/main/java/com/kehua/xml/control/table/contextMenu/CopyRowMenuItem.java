package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.table.TableUtil;
import com.kehua.xml.control.table.tool.Copy;
import com.kehua.xml.utils.DataUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class CopyRowMenuItem<T> extends MenuItem<T> {

    public CopyRowMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "复制行");

        this.setOnAction(event -> {
            Copy.copyRow(tableView, dataClass);
        });
    }

    @Override
    protected boolean getVisible() {
        return tableView.getSelectionModel().getSelectedCells().size() > 0;
    }
}
