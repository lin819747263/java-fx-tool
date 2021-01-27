package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.experimental.Accessors;

public class Device {

    @ExcelProperty("templateCode")
    private SimpleStringProperty templateCode = new SimpleStringProperty();
    @ExcelProperty("devType")
    private SimpleStringProperty devType = new SimpleStringProperty();
    @ExcelProperty("language")
    private SimpleStringProperty language = new SimpleStringProperty();
    @ExcelProperty("protocolName")
    private SimpleStringProperty protocolName = new SimpleStringProperty();
    @ExcelProperty("maker")
    private SimpleStringProperty maker = new SimpleStringProperty();
    @ExcelProperty("model")
    private SimpleStringProperty model = new SimpleStringProperty();
    @ExcelProperty("describe")
    private SimpleStringProperty describe = new SimpleStringProperty();
    @ExcelProperty("protocolType")
    private SimpleStringProperty protocolType = new SimpleStringProperty();

    public String getTemplateCode() {
        return templateCode.get();
    }

    public SimpleStringProperty templateCodeProperty() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode.set(templateCode);
    }

    public String getDevType() {
        return devType.get();
    }

    public SimpleStringProperty devTypeProperty() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType.set(devType);
    }

    public String getLanguage() {
        return language.get();
    }

    public SimpleStringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getProtocolName() {
        return protocolName.get();
    }

    public SimpleStringProperty protocolNameProperty() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName.set(protocolName);
    }

    public String getMaker() {
        return maker.get();
    }

    public SimpleStringProperty makerProperty() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker.set(maker);
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getDescribe() {
        return describe.get();
    }

    public SimpleStringProperty describeProperty() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe.set(describe);
    }

    public String getProtocolType() {
        return protocolType.get();
    }

    public SimpleStringProperty protocolTypeProperty() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType.set(protocolType);
    }
}
