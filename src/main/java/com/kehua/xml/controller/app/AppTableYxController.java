package com.kehua.xml.controller.app;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.AppYxPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.TableColumn;

@FXMLController
public class AppTableYxController extends BasePointController<AppYxPoint> {

    public TableColumn<AppYxPoint, String> levelNo;
    public TableColumn<AppYxPoint, String> levelType;
    public TableColumn<AppYxPoint, String> sort;
    public TableColumn<AppYxPoint, String> userType;
    public TableColumn<AppYxPoint, String> group;

    public void initialize() {
        initialize(AppTableDataManager.YX_POINTS, AppYxPoint.class);

        baseTableColumnInit(levelNo);
        baseTableColumnInit(levelType);
        baseTableColumnInit(sort);
        choiceBoxColumnInit(userType, "APP用户类型");
        choiceBoxColumnInit(group, "APP遥信分组");
    }

    @Override
    protected void setVisible() {
        super.setVisible();

        ratio.setVisible(false);
        levelNo.setVisible(false);
        levelType.setVisible(false);
        userDefined2.setVisible(false);
    }
}
