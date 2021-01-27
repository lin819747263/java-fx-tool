package com.kehua.xml.controller.app;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.RecordPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;

@FXMLController
public class AppTableRecordController extends BaseController<RecordPoint> {

    public TableColumn<RecordPoint, String> code;
    public TableColumn<RecordPoint, String> remark;
    public TableColumn<RecordPoint, String> regType;
    public TableColumn<RecordPoint, String> ratio;
    public TableColumn<RecordPoint, String> unit;
    public TableColumn<RecordPoint, String> userDefined1;
    public TableColumn<RecordPoint, String> userDefined2;
    public TableColumn<RecordPoint, String> recordType;

    public void initialize() {
        super.initialize(AppTableDataManager.RECORDS);
        choiceBoxColumnInit(recordType,"APP记录类型");
        baseTableColumnInit(code);
        baseTableColumnInit(remark);
        baseTableColumnInit(regType);
        baseTableColumnInit(ratio);
        baseTableColumnInit(unit);
        baseTableColumnInit(userDefined1);
        baseTableColumnInit(userDefined2);

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setContextMenu(TableContextMenu.getMenuBox(table, RecordPoint.class));
    }

    @Override
    protected void cellFactoryBuild() {
        super.cellFactoryBuild();
    }

    @Override
    protected void cellValueFactoryBuild() {
        super.cellValueFactoryBuild();
    }
}
