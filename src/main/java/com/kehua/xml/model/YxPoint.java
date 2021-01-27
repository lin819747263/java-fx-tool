package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author admin
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class YxPoint extends BasePoint implements Comparable<YxPoint>{

    @ExcelProperty(value = "levelNo", order = 10)
    private SimpleStringProperty levelNo = new SimpleStringProperty("-1");

    @ExcelProperty(value = "levelType",order = 11)
    private SimpleStringProperty levelType = new SimpleStringProperty("-1");

    public YxPoint(){
        this.setRegType("81");
        this.setRegParam("0,0");
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

    @Override
    public int compareTo(YxPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }
}
