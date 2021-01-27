package com.kehua.xml.controller.web;

import com.kehua.xml.analysis.modbus.ModbusMasterFactory;
import com.kehua.xml.control.textformatter.NumberFormatter;
import com.kehua.xml.data.SettingConfig;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.DataUtils;
import com.kehua.xml.utils.ModbusUtil;
import com.kehua.xml.view.web.CollectSettingSerialView;
import com.kehua.xml.view.web.CollectSettingTcpView;
import com.serotonin.modbus4j.ModbusMaster;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@FXMLController
public class DebugCollectSettingController {

    private static final Logger logger = LoggerFactory.getLogger(DebugCollectSettingController.class);

    @Autowired
    private CollectSettingTcpView collectSettingTcpView;
    @Autowired
    private CollectSettingSerialView collectSettingSerialView;
    @Autowired
    public CollectSettingSerialController collectSettingSerialController;
    @Autowired
    public CollectSettingTcpController collectSettingTcpController;

    @FXML
    public VBox vBox;
    @FXML
    public TextField addr;
    @FXML
    public RadioButton tcp;
    @FXML
    public RadioButton serial;
    @FXML
    public VBox settingWay;
    @FXML
    public Button connect;
    @FXML
    public Button cancel;
    @FXML
    public Button ensure;

    public void initialize() {
        addrInit();
        settingWayInit();
        actionInit(vBox);
    }

    private void actionInit(Node node) {
        if (node instanceof Pane){
            ((Pane) node).getChildren().forEach(this::actionInit);
        }else if (node instanceof Control && !(node instanceof Button)){
            node.addEventFilter(ActionEvent.ACTION, event -> {
                ensure(null);
            });
        }
    }

    public void cancel(ActionEvent event) {
        ((Stage) cancel.getScene().getWindow()).close();
    }

    public void ensure(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        if (!checkSetting()) {
            return;
        }
        if (testSetting(null, "连接失败，请检查连接")) {
            saveConfig();
            stage.close();
        }
    }

    public void connect(ActionEvent event) {
        if (checkSetting()) {
            testSetting("连接成功", "连接失败，请检查连接");
        }
    }

    private void saveConfig() {
        SettingConfig config = SettingConfig.getDefault();
        config.setAddr(getAddr());
        if (tcp.isSelected()){
            config.setTcp(getHost(), getPort());
        }else {
            config.setSerial(getCommPort(), getBaudRate());
        }
    }

    private void addrInit() {
        addr.setTextFormatter(new NumberFormatter<>(Integer.class).build());
        addr.setText("1");
    }

    private void settingWayInit() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> children = settingWay.getChildren();
            children.clear();
            if (((RadioButton) newValue).getId().equals("tcp")) {
                children.add(collectSettingTcpView.getView());
            }else {
                children.add(collectSettingSerialView.getView());
            }
        });
        tcp.setToggleGroup(toggleGroup);
        serial.setToggleGroup(toggleGroup);
    }

    private boolean checkSetting() {
        if (addr.getText().equals("")) {
            AlertFactory.createAlert("请正确填写设备地址!");
            return false;
        }
        if (tcp.isSelected()) {
            if (!DataUtils.checkHost(collectSettingTcpController.host.getText())) {
                AlertFactory.createAlert("请正确填写主机地址!");
                return false;
            }
            if (collectSettingTcpController.port.getText().equals("")) {
                AlertFactory.createAlert("请正确填写端口地址!");
                return false;
            }
        }else if (serial.isSelected()) {
            if (StringUtils.isEmpty(collectSettingSerialController.commPort.getValue())) {
                AlertFactory.createAlert("请选择一个COM口!");
                return false;
            }
        }
        return true;
    }

    private boolean testSetting(String successMsg, String failMsg) {
        try {
            SettingConfig config = SettingConfig.getDebug();
            config.setAddr(getAddr());
            if (tcp.isSelected()){
                config.setTcp(getHost(), getPort());
            }else {
                config.setSerial(getCommPort(), getBaudRate());
            }
            ModbusMaster master = ModbusMasterFactory.createMaster(config);
            ModbusUtil.readRegisters(4, master, config.getAddr(), 1, 1);
            if (successMsg != null) {
                AlertFactory.createAlert(successMsg);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (failMsg != null) {
                AlertFactory.createAlert(failMsg);
            }
            return false;
        }
    }

    public Integer getAddr() {
        if (addr.getText() == null) {
            return null;
        }
        return Integer.parseInt(addr.getText());
    }

    public String getHost() {
        return collectSettingTcpController.getHost();
    }

    public Integer getPort() {
        return collectSettingTcpController.getPort();
    }

    public void setHost(String host) {
        collectSettingTcpController.setHost(host);
    }

    public void setPort(Integer port) {
        collectSettingTcpController.setPort(port);
    }

    public String getCommPort() {
        return collectSettingSerialController.getCommPort();
    }

    public Integer getBaudRate() {
        return collectSettingSerialController.getBaudRate();
    }

    public void setCommPort(String commPort) {
        collectSettingSerialController.setCommPort(commPort);
    }

    public void setBaudRate(Integer baudRate) {
        collectSettingSerialController.setBaudRate(baudRate);
    }

}
