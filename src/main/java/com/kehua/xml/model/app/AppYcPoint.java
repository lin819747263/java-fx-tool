package com.kehua.xml.model.app;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kehua.xml.model.YxPoint;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class AppYcPoint extends AppBasePoint implements Comparable<AppYcPoint>{

    @ExcelProperty(value = "levelNo",order = 11)
    private SimpleStringProperty levelNo = new SimpleStringProperty("-1");
    @ExcelProperty(value = "levelType",order = 12)
    private SimpleStringProperty levelType = new SimpleStringProperty("-1");
    @ExcelProperty(value = "oneTime",order = 15)
    private SimpleBooleanProperty oneTime = new SimpleBooleanProperty();
    @ExcelProperty(value = "unit",order = 10)
    private SimpleStringProperty unit = new SimpleStringProperty();

    public String getLevelNo() {
        return levelNo.get();
    }

    public SimpleStringProperty levelNoProperty() {
        return levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo.set(levelNo);
    }

    public String getLevelType() {
        return levelType.get();
    }

    public SimpleStringProperty levelTypeProperty() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType.set(levelType);
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

    public boolean isOneTime() {
        return oneTime.get();
    }

    public SimpleBooleanProperty oneTimeProperty() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime.set(oneTime);
    }

    @Override
    public int compareTo(AppYcPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }
}
