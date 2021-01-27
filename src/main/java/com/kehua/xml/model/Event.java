package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {

    @ExcelProperty("事件名称")
    private SimpleStringProperty remark = new SimpleStringProperty();
    @ExcelProperty("事件等级")
    private SimpleStringProperty eventLevel = new SimpleStringProperty("3");
    @ExcelProperty("事件处理建议")
    private SimpleStringProperty recommend = new SimpleStringProperty();

    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getEventLevel() {
        return eventLevel.get();
    }

    public SimpleStringProperty eventLevelProperty() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel.set(eventLevel);
    }

    public String getRecommend() {
        return recommend.get();
    }

    public SimpleStringProperty recommendProperty() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend.set(recommend);
    }
}
