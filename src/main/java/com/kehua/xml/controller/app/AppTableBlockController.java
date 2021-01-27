package com.kehua.xml.controller.app;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.AppBlockPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;


@FXMLController
public class AppTableBlockController extends BaseController<AppBlockPoint> {

    public TableColumn<AppBlockPoint, String> funcode;
    public TableColumn<AppBlockPoint, String> addr;
    public TableColumn<AppBlockPoint, String> regCount;
    public TableColumn<AppBlockPoint, String> queryType;
    public TableColumn<AppBlockPoint, Boolean> isLocked;
    public TableColumn<AppBlockPoint, String> module;
    public TableColumn<AppBlockPoint, String> userType;


    public void initialize() {
        super.initialize(AppTableDataManager.BLOCKS);
        choiceBoxColumnInit(userType, "APP用户类型");
        choiceBoxColumnInit(module, "模块类型");
        baseTableColumnInit(funcode);
        baseTableColumnInit(regCount);
        baseTableColumnInit(queryType);
        baseTableColumnInit(addr);
        checkBoxColumnInit(isLocked);

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setContextMenu(TableContextMenu.getMenuBox(table, AppBlockPoint.class));
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
