package com.kehua.xml.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class DebugYk {

    private SimpleStringProperty funCode = new SimpleStringProperty();

    private SimpleStringProperty addr = new SimpleStringProperty();

    private SimpleStringProperty remark = new SimpleStringProperty();

    private SimpleStringProperty regType = new SimpleStringProperty();

    private SimpleStringProperty regParam = new SimpleStringProperty();

    private SimpleStringProperty ratio = new SimpleStringProperty();

    private SimpleStringProperty rangeData = new SimpleStringProperty();

    private SimpleStringProperty sendValue = new SimpleStringProperty();

    private SimpleBooleanProperty isBlock = new SimpleBooleanProperty();

    private SimpleListProperty<DebugYk> points = new SimpleListProperty<>();

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

    public String getRangeData() {
        return rangeData.get();
    }

    public SimpleStringProperty rangeDataProperty() {
        return rangeData;
    }

    public void setRangeData(String rangeData) {
        this.rangeData.set(rangeData);
    }

    public String getSendValue() {
        return sendValue.get();
    }

    public SimpleStringProperty sendValueProperty() {
        return sendValue;
    }

    public void setSendValue(String sendValue) {
        this.sendValue.set(sendValue);
    }

    public boolean isIsBlock() {
        return isBlock.get();
    }

    public SimpleBooleanProperty isBlockProperty() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock.set(isBlock);
    }

    public ObservableList<DebugYk> getPoints() {
        return points.get();
    }

    public SimpleListProperty<DebugYk> pointsProperty() {
        return points;
    }

    public void setPoints(ObservableList<DebugYk> points) {
        this.points.set(points);
    }
}
