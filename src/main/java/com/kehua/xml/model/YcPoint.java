package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author admin
 */
public class YcPoint extends BasePoint implements Comparable<YcPoint>{

    @ExcelProperty(value = "levelNo",order = 11)
    private SimpleStringProperty levelNo = new SimpleStringProperty("-1");
    @ExcelProperty(value = "levelType",order = 12)
    private SimpleStringProperty levelType = new SimpleStringProperty("-1");
    @ExcelProperty(value = "oneTime",order = 15)
    private SimpleBooleanProperty oneTime = new SimpleBooleanProperty();
    @ExcelProperty(value = "unit",order = 10)
    private SimpleStringProperty unit = new SimpleStringProperty();

    public YcPoint(){
        this.setRegType("3");
    }

    @Override
    public void setAddr(String addr) {
        super.setAddr(addr);
        addr = getAddr();
        if (StringUtils.isEmpty(addr)){
            this.setProperty("");
        }else {
            this.setProperty("YC" + addr);
        }
    }

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

    public Boolean getOneTime() {
        return oneTime.get();
    }

    public SimpleBooleanProperty oneTimeProperty() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime.set(oneTime);
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

    @Override
    public int compareTo(YcPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }


}
