package com.kehua.xml.model.app;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kehua.xml.model.YcPoint;
import com.kehua.xml.model.YxPoint;
import javafx.beans.property.SimpleStringProperty;

public class AppYxPoint extends AppBasePoint implements Comparable<AppYxPoint>{

    @ExcelProperty(value = "levelNo", order = 10)
    private SimpleStringProperty levelNo = new SimpleStringProperty("-1");

    @ExcelProperty(value = "levelType",order = 11)
    private SimpleStringProperty levelType = new SimpleStringProperty("-1");

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

    @Override
    public int compareTo(AppYxPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }
}
