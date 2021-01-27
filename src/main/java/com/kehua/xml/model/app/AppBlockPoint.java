package com.kehua.xml.model.app;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kehua.xml.model.Block;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author admin
 */
public class AppBlockPoint extends Block {

    @ExcelProperty(value = "userType")
    private SimpleStringProperty userType = new SimpleStringProperty();

    @ExcelProperty(value = "module")
    private SimpleStringProperty module = new SimpleStringProperty();

    public String getUserType() {
        return userType.get();
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    public String getModule() {
        return module.get();
    }

    public SimpleStringProperty moduleProperty() {
        return module;
    }

    public void setModule(String module) {
        this.module.set(module);
    }
}
