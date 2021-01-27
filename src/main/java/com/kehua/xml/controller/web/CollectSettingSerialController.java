package com.kehua.xml.controller.web;

import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@FXMLController
public class CollectSettingSerialController {

    String query = "reg query \"HKLM\\HARDWARE\\DEVICEMAP\\SERIALCOMM\"";
    @FXML
    public ChoiceBox<String> commPort;
    @FXML
    public ChoiceBox<Integer> baudRate;

    public void initialize() {
        commPort.setItems(FXCollections.observableArrayList(getComPorts()));
        baudRate.setItems(FXCollections.observableArrayList(300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 56000, 57600, 115200, 128000, 153600, 230400, 256000, 460800, 821600));
        baudRate.setValue(9600);
    }

    private List<String> getComPorts() {
        List<String> ports = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(query);
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                if ("".equals(line)) {
                    continue;
                }
                if (index != 0) {
                    String[] strs = line.replaceAll(" +", ",").split(",");
                    String comPort = strs[strs.length - 1];
                    ports.add(comPort);
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ports;
    }

    public String getCommPort() {
        return commPort.getValue();
    }

    public Integer getBaudRate() {
        return baudRate.getValue();
    }

    public void setCommPort(String commPort) {
        this.commPort.setValue(commPort);
    }

    public void setBaudRate(Integer baudRate) {
        this.baudRate.setValue(baudRate);
    }
}
