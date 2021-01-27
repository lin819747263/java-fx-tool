package com.kehua.xml.controller.web;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.model.I18n;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableI18nController extends BaseController<I18n> {
    @FXML
    public TableColumn<I18n, String> remark;
    @FXML
    public TableColumn<I18n, String> remarkEn;
    @FXML
    public TableColumn<I18n, String> remarkPl;
    public TableColumn<I18n, String> remarkVnm;
    public TableColumn<I18n, String> remarkEs;

    public void initialize() {
        initialize(CommonDataManager.I_18_NS);
        table.setContextMenu(TableContextMenu.getMenuBox(table, I18n.class));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);

        baseTableColumnInit(remark);
        baseTableColumnInit(remarkEn);
        baseTableColumnInit(remarkPl);
        baseTableColumnInit(remarkVnm);
        baseTableColumnInit(remarkEs);
    }
}
