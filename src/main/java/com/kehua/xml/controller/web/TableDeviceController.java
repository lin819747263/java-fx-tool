package com.kehua.xml.controller.web;

import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.Device;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableDeviceController extends BaseController<Device> {
    
    @FXML
    public TableColumn<Device,String> templateCode;
    @FXML
    public TableColumn<Device,String> devType;
    @FXML
    public TableColumn<Device,String> language;
    @FXML
    public TableColumn<Device,String> protocolName;
    @FXML
    public TableColumn<Device,String> maker;
    @FXML
    public TableColumn<Device,String> model;
    @FXML
    public TableColumn<Device,String> describe;
    @FXML
    public TableColumn<Device,String> protocolType;
    

    public void initialize() {
        super.initialize(TableDataManager.DEVICES);
        baseTableColumnInit(templateCode);
        baseTableColumnInit(devType);
        baseTableColumnInit(language);
        baseTableColumnInit(protocolName);
        baseTableColumnInit(maker);
        baseTableColumnInit(model);
        baseTableColumnInit(describe);
        baseTableColumnInit(protocolType);
        table.setTableMenuButtonVisible(true);
        table.getSelectionModel().setCellSelectionEnabled(true);
    }

    @Override
    protected void setVisible() {
        maker.setVisible(false);
        model.setVisible(false);
        describe.setVisible(false);
        protocolType.setVisible(false);
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
