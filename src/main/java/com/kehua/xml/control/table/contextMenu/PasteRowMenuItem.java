package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.table.tool.Copy;
import com.kehua.xml.control.table.tool.InsertRow;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class PasteRowMenuItem<T> extends MenuItem<T> {

    @SuppressWarnings({"unchecked"})
    public PasteRowMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "粘贴行");

        this.setOnAction(event -> {
            InsertRow.insertRow(tableView, (List<T>) Copy.copyData);
        });
    }

    @Override
    protected boolean getVisible() {
        return tableView.getSelectionModel().getSelectedCells().size() > 0 &&
                dataClass.equals(Copy.copyDataClass) &&
                Copy.copyData.size() > 0;
    }
}
