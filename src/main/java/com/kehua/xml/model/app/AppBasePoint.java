package com.kehua.xml.model.app;


import com.alibaba.excel.annotation.ExcelProperty;
import com.kehua.xml.model.BasePoint;
import javafx.beans.property.SimpleStringProperty;

public class AppBasePoint extends BasePoint {

    @ExcelProperty(value = "sort")
    private SimpleStringProperty sort = new SimpleStringProperty();

    @ExcelProperty(value = "userType")
    private SimpleStringProperty userType = new SimpleStringProperty();

    @ExcelProperty(value = "group")
    private SimpleStringProperty group = new SimpleStringProperty();

    public String getSort() {
        return sort.get();
    }

    public SimpleStringProperty sortProperty() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort.set(sort);
    }

    public String getUserType() {
        return userType.get();
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    public String getGroup() {
        return group.get();
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }
}
