package com.kehua.xml.model.app;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kehua.xml.model.YxPoint;
import javafx.beans.property.SimpleStringProperty;

public class AppYkPoint extends AppBasePoint implements Comparable<AppYkPoint>{

    @ExcelProperty(value = "rangeData", order = 15)
    private SimpleStringProperty rangeData = new SimpleStringProperty();
    @ExcelProperty(value = "nextProp",order = 16)
    private SimpleStringProperty nextProp = new SimpleStringProperty();
    @ExcelProperty(value = "defaultVal",order = 17)
    private SimpleStringProperty defaultVal = new SimpleStringProperty();
    @ExcelProperty(value = "regNum",order = 11)
    private SimpleStringProperty regNum = new SimpleStringProperty();
    @ExcelProperty(value = "num",order = 12)
    private SimpleStringProperty pointNum = new SimpleStringProperty();
    @ExcelProperty(value = "unit",order = 10)
    private SimpleStringProperty unit = new SimpleStringProperty();
    @ExcelProperty(value = "collectFuncode",order = 18)
    private SimpleStringProperty collectFuncode = new SimpleStringProperty();

    public String getRangeData() {
        return rangeData.get();
    }

    public SimpleStringProperty rangeDataProperty() {
        return rangeData;
    }

    public void setRangeData(String rangeData) {
        this.rangeData.set(rangeData);
    }

    public String getNextProp() {
        return nextProp.get();
    }

    public SimpleStringProperty nextPropProperty() {
        return nextProp;
    }

    public void setNextProp(String nextProp) {
        this.nextProp.set(nextProp);
    }

    public String getDefaultVal() {
        return defaultVal.get();
    }

    public SimpleStringProperty defaultValProperty() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal.set(defaultVal);
    }

    public String getRegNum() {
        return regNum.get();
    }

    public SimpleStringProperty regNumProperty() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum.set(regNum);
    }

    public String getPointNum() {
        return pointNum.get();
    }

    public SimpleStringProperty pointNumProperty() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum.set(pointNum);
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

    public String getCollectFuncode() {
        return collectFuncode.get();
    }

    public SimpleStringProperty collectFuncodeProperty() {
        return collectFuncode;
    }

    public void setCollectFuncode(String collectFuncode) {
        this.collectFuncode.set(collectFuncode);
    }

    @Override
    public int compareTo(AppYkPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }
}
