package com.kehua.xml.controller.web;

import com.kehua.xml.analysis.modbus.ModbusMasterFactory;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.data.LogQueue;
import com.kehua.xml.data.SettingConfig;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.ModbusUtil;
import com.kehua.xml.utils.PathUtil;
import com.kehua.xml.utils.UIFactory;
import com.serotonin.modbus4j.ModbusMaster;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@FXMLController
public class DebugLogController {

    @FXML
    public TextArea log;

    public void initialize() {
        LogQueue.register(s -> log.appendText(s + "\n"));
    }

    public void openLog() throws IOException {
        File file = new File(PathUtil.getLogPath(GloableSetting.workPath) + "log.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        Desktop.getDesktop().open(file);
    }

    public void customerCommandSend() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = UIFactory.createBaseGridPane();

        TextField deviceAddr = new TextField();
        deviceAddr.appendText("1");
        ChoiceBox<String> funCode = new ChoiceBox<>(FXCollections.observableArrayList("1","2","3","4"));
        funCode.setValue("3");
        TextField startAddr = new TextField();
        TextField regNum = new TextField();

        grid.add(new Label("设备地址"), 0, 0);
        grid.add(deviceAddr, 1, 0);
        grid.add(new Label("功能码"), 0, 1);
        grid.add(funCode, 1, 1);
        grid.add(new Label("起始地址"), 0, 2);
        grid.add(startAddr, 1, 2);
        grid.add(new Label("寄存器个数"), 0, 3);
        grid.add(regNum, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                check(funCode.getValue(), startAddr.getText(), regNum.getText());
            }catch (Exception e){
                AlertFactory.createAlert(e.getMessage());
                return;
            }
            sendCommand(Integer.valueOf(funCode.getValue()), Integer.valueOf(startAddr.getText()), Integer.valueOf(regNum.getText()));
        }
    }

    private void check(String funCode, String startAddr, String regNum) {
        try {
            Integer.valueOf(funCode);
            Integer.valueOf(startAddr);
            Integer.valueOf(regNum);
        }catch (Exception e){
            throw new IllegalArgumentException("参数有误");
        }
    }

    public void clearLog() {
        log.clear();
    }

    public void sendCommand(Integer funCode,Integer startAddr, Integer num){
        try {
            ModbusMaster master = ModbusMasterFactory.createMaster();
            ModbusUtil.readRegisters(funCode, master, SettingConfig.getDefault().getAddr(), startAddr, num);
        } catch (Exception e) {
            AlertFactory.createAlert("采集失败,请检查连接");
        }
    }
}
