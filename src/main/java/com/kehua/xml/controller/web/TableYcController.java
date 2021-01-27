package com.kehua.xml.controller.web;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.YcPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableYcController extends BasePointController<YcPoint> {

    @FXML
    public TableColumn<YcPoint, String> levelNo;
    @FXML
    public TableColumn<YcPoint, String> levelType;
    @FXML
    public TableColumn<YcPoint, Boolean> oneTime;
    @FXML
    public TableColumn<YcPoint, String> unit;

    public void initialize() {
        initialize(TableDataManager.YC_POINTS, YcPoint.class);

        baseTableColumnInit(unit);
        baseTableColumnInit(levelNo);
        baseTableColumnInit(levelType);
        checkBoxColumnInit(oneTime);
    }

    @Override
    protected void setVisible() {
        super.setVisible();

        levelNo.setVisible(false);
        levelType.setVisible(false);
    }
}
