package com.kehua.xml.controller.app;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.model.app.AppYkPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.TableColumn;

@FXMLController
public class AppTableYkController extends BasePointController<AppYkPoint> {

    public TableColumn<AppYkPoint, String> nextProp;
    public TableColumn<AppYkPoint, String> defaultVal;
    public TableColumn<AppYkPoint, String> rangeData;
    public TableColumn<AppYkPoint, Integer> regNum;
    public TableColumn<AppYkPoint, Integer> pointNum;
    public TableColumn<AppYkPoint, String> unit;
    public TableColumn<AppYkPoint, String> sort;
    public TableColumn<AppYkPoint, String> userType;
    public TableColumn<AppYkPoint, String> group;
    public TableColumn<AppYkPoint, String> collectFuncode;

    public void initialize() {
        initialize(AppTableDataManager.YK_POINTS, AppYkPoint.class);

        baseTableColumnInit(unit);
        baseTableColumnInit(nextProp);
        baseTableColumnInit(defaultVal);
        baseTableColumnInit(rangeData);
        numberColumnInit(regNum, Integer.class, 5, 1, null);
        numberColumnInit(pointNum, Integer.class, 5, 1, null);
        baseTableColumnInit(sort);
        choiceBoxColumnInit(userType, "APP用户类型");
        choiceBoxColumnInit(group, "APP遥控分组");
        choiceBoxColumnInit(collectFuncode, "", "1", "2", "3", "4", "5", "6", "16");
    }

    @Override
    protected void setVisible() {
        super.setVisible();

        regParam.setVisible(false);
        nextProp.setVisible(false);
        defaultVal.setVisible(false);
        userDefined2.setVisible(false);
    }
}
