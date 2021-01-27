package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.table.TableUtil;
import com.kehua.xml.control.table.tool.Paste;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class PasteMenuItem<T> extends MenuItem<T> {

    public PasteMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "粘贴");

        this.setOnAction(event -> Paste.paste(tableView));
    }



    @Override
    protected boolean getVisible() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return tableView.getSelectionModel().getSelectedCells().size() > 0 &&
                clipboard.getContent(DataFormat.PLAIN_TEXT) != null;
    }
}
