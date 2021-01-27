package com.kehua.xml.controller.app;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.Group;
import com.kehua.xml.utils.DictUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;

import java.util.Properties;

@FXMLController
public class AppTableGroupController extends BaseController<Group> {

    public TableColumn<Group, String> key;
    public TableColumn<Group, String> name;
    public TableColumn<Group, String> sort;

    public void initialize() {
        super.initialize(AppTableDataManager.GROUPS);
        baseTableColumnInit(key);
        baseTableColumnInit(name);
        baseTableColumnInit(sort);

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setContextMenu(TableContextMenu.getMenuBox(table, Group.class));
        initData();
    }

    private void initData() {
        buildGroup(DictUtil.getDictByType("APP遥信分组"));
        buildGroup(DictUtil.getDictByType("APP遥测分组"));
        buildGroup(DictUtil.getDictByType("APP遥控分组"));
    }

    public void buildGroup(Properties properties){
        properties.forEach((k,v) -> {
            Group group = new Group();
            group.setKey(k.toString());
            group.setName(v.toString());
            group.setSort("1");
            AppTableDataManager.GROUPS.add(group);
        });
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
