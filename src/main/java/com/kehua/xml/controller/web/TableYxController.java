package com.kehua.xml.controller.web;

import com.kehua.xml.controller.BasePointController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.YxPoint;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

@FXMLController
public class TableYxController extends BasePointController<YxPoint> {

    @FXML
    public TableColumn<YxPoint, String> levelNo;
    @FXML
    public TableColumn<YxPoint, String> levelType;

    public void initialize() {
        initialize(TableDataManager.YX_POINTS, YxPoint.class);

        baseTableColumnInit(levelNo);
        baseTableColumnInit(levelType);
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
