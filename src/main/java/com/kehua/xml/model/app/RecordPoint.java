package com.kehua.xml.model.app;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;

public class RecordPoint {

    @ExcelProperty(value = "code")
    private SimpleStringProperty code = new SimpleStringProperty();

    @ExcelProperty(value = "remark")
    private SimpleStringProperty remark = new SimpleStringProperty();

    @ExcelProperty(value = "userDefined1")
    private SimpleStringProperty userDefined1 = new SimpleStringProperty();

    @ExcelProperty(value = "userDefined2")
    private SimpleStringProperty userDefined2 = new SimpleStringProperty();

    @ExcelProperty(value = "ratio")
    private SimpleStringProperty ratio = new SimpleStringProperty("1");

    @ExcelProperty(value = "regType")
    private SimpleStringProperty regType = new SimpleStringProperty();

    @ExcelProperty(value = "unit")
    private SimpleStringProperty unit = new SimpleStringProperty();

    @ExcelProperty(value = "type")
    private SimpleStringProperty recordType = new SimpleStringProperty();

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
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

    public String getUserDefined1() {
        return userDefined1.get();
    }

    public SimpleStringProperty userDefined1Property() {
        return userDefined1;
    }

    public void setUserDefined1(String userDefined1) {
        this.userDefined1.set(userDefined1);
    }

    public String getUserDefined2() {
        return userDefined2.get();
    }

    public SimpleStringProperty userDefined2Property() {
        return userDefined2;
    }

    public void setUserDefined2(String userDefined2) {
        this.userDefined2.set(userDefined2);
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

    public String getRegType() {
        return regType.get();
    }

    public SimpleStringProperty regTypeProperty() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType.set(regType);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public String getRecordType() {
        return recordType.get();
    }

    public SimpleStringProperty recordTypeProperty() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType.set(recordType);
    }
}
