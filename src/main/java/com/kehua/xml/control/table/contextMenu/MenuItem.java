package com.kehua.xml.control.table.contextMenu;

import javafx.scene.control.TableView;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public abstract class MenuItem<T> extends javafx.scene.control.MenuItem {

    protected TableView<T> tableView;
    protected Class<T> dataClass;

    public MenuItem(TableView<T> tableView, Class<T> dataClass, String text) {
        super(text);
        this.dataClass = dataClass;
        this.tableView = tableView;
    }

    protected boolean getVisible(){
        return true;
    }

    public void setVisible(){
        setVisible(getVisible());
    }
}
