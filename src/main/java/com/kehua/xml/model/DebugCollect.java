package com.kehua.xml.model;

import javafx.beans.property.SimpleStringProperty;

public class DebugCollect {

    private SimpleStringProperty funCode = new SimpleStringProperty();

    private SimpleStringProperty addr = new SimpleStringProperty();

    private SimpleStringProperty remark = new SimpleStringProperty();

    private SimpleStringProperty regType = new SimpleStringProperty();

    private SimpleStringProperty regParam = new SimpleStringProperty();

    private SimpleStringProperty ratio = new SimpleStringProperty();

    private SimpleStringProperty offset = new SimpleStringProperty();

    private SimpleStringProperty value = new SimpleStringProperty();

    public String getFunCode() {
        return funCode.get();
    }

    public SimpleStringProperty funCodeProperty() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode.set(funCode);
    }

    public String getAddr() {
        return addr.get();
    }

    public SimpleStringProperty addrProperty() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr.set(addr);
    }

    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getRegType() {
        return regType.get();
    }

    public SimpleStringProperty regTypeProperty() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType.set(regType);
    }

    public String getRegParam() {
        return regParam.get();
    }

    public SimpleStringProperty regParamProperty() {
        return regParam;
    }

    public void setRegParam(String regParam) {
        this.regParam.set(regParam);
    }

    public String getRatio() {
        return ratio.get();
    }

    public SimpleStringProperty ratioProperty() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio.set(ratio);
    }

    public String getOffset() {
        return offset.get();
    }

    public SimpleStringProperty offsetProperty() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset.set(offset);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
