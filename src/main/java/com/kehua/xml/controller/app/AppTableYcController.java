package com.kehua.xml.controller.app;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.AppYcPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.TableColumn;


@FXMLController
public class AppTableYcController extends BasePointController<AppYcPoint> {

    public TableColumn<AppYcPoint, String> levelNo;
    public TableColumn<AppYcPoint, String> levelType;
    public TableColumn<AppYcPoint, String> unit;
    public TableColumn<AppYcPoint, String> sort;
    public TableColumn<AppYcPoint, String> userType;
    public TableColumn<AppYcPoint, String> group;
    public TableColumn<AppYcPoint, Boolean> oneTime;

    public void initialize() {
        initialize(AppTableDataManager.YC_POINTS, AppYcPoint.class);

        baseTableColumnInit(unit);
        baseTableColumnInit(levelNo);
        baseTableColumnInit(levelType);
        baseTableColumnInit(sort);
        checkBoxColumnInit(oneTime);
        choiceBoxColumnInit(userType, "APP用户类型");
        choiceBoxColumnInit(group, "APP遥测分组");
    }

    @Override
    protected void setVisible() {
        super.setVisible();

        levelNo.setVisible(false);
        levelType.setVisible(false);
    }
}
