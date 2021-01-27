package com.kehua.xml.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;

public class BasePoint {

    @ExcelProperty(value = "funCode", order = 1)
    private SimpleStringProperty funCode = new SimpleStringProperty();

    @ExcelProperty(value = "addr", order = 2)
    private SimpleStringProperty addr = new SimpleStringProperty();

    @ExcelProperty(value = "regType", order = 3)
    private SimpleStringProperty regType = new SimpleStringProperty();

    @ExcelProperty(value = "regParam", order = 4)
    private SimpleStringProperty regParam = new SimpleStringProperty();

    @ExcelProperty(value = "endian", order = 5)
    private SimpleStringProperty endian = new SimpleStringProperty("0");

    @ExcelProperty(value = "ratio", order = 6)
    private SimpleStringProperty ratio = new SimpleStringProperty("1");

    @ExcelProperty(value = "offset", order = 7)
    private SimpleStringProperty offset = new SimpleStringProperty("0");

    @ExcelIgnore
    private SimpleStringProperty property = new SimpleStringProperty();

    @ExcelProperty(value = "remark", order = 9)
    private SimpleStringProperty remark = new SimpleStringProperty();

    @ExcelProperty(value = "userDefined1", order = 13)
    private SimpleStringProperty userDefined1 = new SimpleStringProperty();

    @ExcelProperty(value = "userDefined2", order = 14)
    private SimpleStringProperty userDefined2 = new SimpleStringProperty();

    public String getFunCode() {
        return funCode.get();
    }

    public SimpleStringProperty funCodeProperty() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode.set(funCode);
    }

    public String getAddr() {
        try {
            if (Integer.parseInt(addr.get()) > 0){
                return addr.get();
            }
        }catch (Exception ignored){}
        return null;
    }

    public SimpleStringProperty addrProperty() {
        return addr;
    }

    public void setAddr(String addr) {
        if (StringUtils.isEmpty(addr)){
            this.addr.set(null);
        }
        try {
            if (Integer.parseInt(addr) > 0){
                this.addr.set(addr);
            }
        }catch (Exception ignored){
        }
    }

    public String getRegType() {
        return regType.get();
    }

    public SimpleStringProperty regTypeProperty() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType.set(regType);
    }

    public String getRegParam() {
        return regParam.get();
    }

    public SimpleStringProperty regParamProperty() {
        return regParam;
    }

    public void setRegParam(String regParam) {
        this.regParam.set(regParam);
    }

    public String getEndian() {
        return endian.get();
    }

    public SimpleStringProperty endianProperty() {
        return endian;
    }

    public void setEndian(String endian) {
        this.endian.set(endian);
    }

    public String getRatio() {
        return ratio.get();
    }

    public SimpleStringProperty ratioProperty() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio.set(ratio);
    }

    public String getOffset() {
        return offset.get();
    }

    public SimpleStringProperty offsetProperty() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset.set(offset);
    }

    public String getProperty() {
        return property.get();
    }

    public SimpleStringProperty propertyProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property.set(property);
    }

    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getUserDefined1() {
        return userDefined1.get();
    }

    public SimpleStringProperty userDefined1Property() {
        return userDefined1;
    }

    public void setUserDefined1(String userDefined1) {
        this.userDefined1.set(userDefined1);
    }

    public String getUserDefined2() {
        return userDefined2.get();
    }

    public SimpleStringProperty userDefined2Property() {
        return userDefined2;
    }

    public void setUserDefined2(String userDefined2) {
        this.userDefined2.set(userDefined2);
    }


}
