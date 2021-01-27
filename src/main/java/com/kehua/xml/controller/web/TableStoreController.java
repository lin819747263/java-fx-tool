package com.kehua.xml.controller.web;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.Store;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableStoreController extends BaseController<Store> {
    @FXML
    public TableColumn remark;
    @FXML
    public TableColumn<Store, String> storeTime;

    public void initialize() {
        initialize(TableDataManager.STORES);
        table.setContextMenu(TableContextMenu.getMenuBox(table, Store.class));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);

        baseTableColumnInit(remark);
        choiceBoxColumnInit(storeTime, "存储周期");
    }

}
