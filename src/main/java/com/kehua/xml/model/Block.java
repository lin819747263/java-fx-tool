package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

public class Block {

    @ExcelProperty("funcode")
    private SimpleStringProperty funcode = new SimpleStringProperty();
    @ExcelProperty("addr")
    private SimpleStringProperty addr = new SimpleStringProperty();
    @ExcelProperty("regCount")
    private SimpleStringProperty regCount = new SimpleStringProperty();
    @ExcelProperty("queryType")
    private SimpleStringProperty queryType = new SimpleStringProperty();
    @ExcelProperty("是否锁定")
    private SimpleBooleanProperty isLocked = new SimpleBooleanProperty();

    public String getFuncode() {
        return funcode.get();
    }

    public SimpleStringProperty funcodeProperty() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode.set(funcode);
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

    public String getRegCount() {
        return regCount.get();
    }

    public SimpleStringProperty regCountProperty() {
        return regCount;
    }

    public void setRegCount(String regCount) {
        this.regCount.set(regCount);
    }

    public String getQueryType() {
        return queryType.get();
    }

    public SimpleStringProperty queryTypeProperty() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType.set(queryType);
    }

    public boolean isIsLocked() {
        return isLocked.get();
    }

    public SimpleBooleanProperty isLockedProperty() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked.set(isLocked);
    }
}
