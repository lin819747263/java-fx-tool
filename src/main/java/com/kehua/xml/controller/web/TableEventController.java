package com.kehua.xml.controller.web;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.Event;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableEventController extends BaseController<Event> {
    @FXML
    public TableColumn<Event, String> remark;
    @FXML
    public TableColumn<Event, String> eventLevel;
    @FXML
    public TableColumn<Event, String> recommend;

    public void initialize() {
        initialize(TableDataManager.EVENTS);
        table.setContextMenu(TableContextMenu.getMenuBox(table, Event.class));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);

        baseTableColumnInit(remark);
        baseTableColumnInit(recommend);
        choiceBoxColumnInit(eventLevel, "事件等级");
    }
}
