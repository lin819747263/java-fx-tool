package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Store {
    @ExcelProperty("数据点位描述")
    private SimpleStringProperty remark = new SimpleStringProperty();
    @ExcelProperty("存储设置")
    private SimpleStringProperty storeTime = new SimpleStringProperty("0");


    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getStoreTime() {
        return storeTime.get();
    }

    public SimpleStringProperty storeTimeProperty() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime.set(storeTime);
    }
}
