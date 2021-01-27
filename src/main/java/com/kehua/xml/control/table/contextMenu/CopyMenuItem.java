package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.table.tool.Copy;
import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class CopyMenuItem<T> extends MenuItem<T> {



    public CopyMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "复制");

        this.setOnAction(event -> {
            Copy.copy(tableView);
        });

    }


    @Override
    protected boolean getVisible() {
        return tableView.getSelectionModel().getSelectedCells().size() > 0;
    }
}
