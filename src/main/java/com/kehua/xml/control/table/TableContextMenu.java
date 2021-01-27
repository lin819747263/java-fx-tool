package com.kehua.xml.control.table;

import com.kehua.xml.control.table.contextMenu.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableView;

public class TableContextMenu {

    public static <T> ContextMenu getMenuBox(TableView<T> tableView, Class<T> t) {

        ContextMenu cm = new ContextMenu();
        cm.getItems().addAll(
                new CopyMenuItem<>(tableView, t),
                new PasteMenuItem<>(tableView, t),
                new InsertRowMenuItem<>(tableView, t),
                new CopyRowMenuItem<>(tableView, t),
                new PasteRowMenuItem<>(tableView, t),
                new DeleteRowMenuItem<>(tableView, t),
                new ClearMenuItem<>(tableView, t),
                new AutoFillMenuItem<>(tableView, t),
                new AddI18nMenuItem<>(tableView, t),
                new DictFillMenuItem<>(tableView, t)
        );

        cm.setOnShowing(event -> cm.getItems().forEach(menuItem -> ((MenuItem) menuItem).setVisible()));

        return cm;
    }

    public static <T> ContextMenu getDictMenuBox(TableView<T> tableView, Class<T> t) {

        ContextMenu cm = new ContextMenu();
        cm.getItems().addAll(
                new InsertRowMenuItem<>(tableView, t),
                new DeleteRowMenuItem<>(tableView, t)
        );

        cm.setOnShowing(event -> cm.getItems().forEach(menuItem -> ((MenuItem) menuItem).setVisible()));

        return cm;
    }

}
