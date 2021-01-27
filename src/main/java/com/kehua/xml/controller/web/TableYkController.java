package com.kehua.xml.controller.web;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.enumerate.RegTypeEnum;
import com.kehua.xml.model.YkPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

@FXMLController
public class TableYkController extends BasePointController<YkPoint> {

    @FXML
    public TableColumn<YkPoint, String> nextProp;
    @FXML
    public TableColumn<YkPoint, String> defaultVal;
    @FXML
    public TableColumn<YkPoint, String> rangeData;
    @FXML
    public TableColumn<YkPoint, Integer> regNum;
    @FXML
    public TableColumn<YkPoint, Integer> pointNum;
    @FXML
    public TableColumn<YkPoint, Boolean> noCollect;
    @FXML
    public TableColumn<YkPoint, Boolean> noRealTime;
    @FXML
    public TableColumn<YkPoint, String> unit;
    @FXML
    public TableColumn<YkPoint, String> collectFuncode;
    @FXML
    public TableColumn<YkPoint, String> collectRegType;
    @FXML
    public TableColumn<YkPoint, String> collectRegParam;

    public void initialize() {
        initialize(TableDataManager.YK_POINTS, YkPoint.class);

        baseTableColumnInit(unit);
        baseTableColumnInit(nextProp);
        baseTableColumnInit(defaultVal);
        baseTableColumnInit(rangeData);
        numberColumnInit(regNum, Integer.class, 5, 1, null);
        numberColumnInit(pointNum, Integer.class, 5, 1, null);
        baseTableColumnInit(collectRegParam);
        choiceBoxColumnInit(collectFuncode, "", "1", "2", "3", "4", "5", "6", "16");
        choiceBoxColumnInit(collectRegType, RegTypeEnum.class);
        checkBoxColumnInit(noCollect);
        checkBoxColumnInit(noRealTime);
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
