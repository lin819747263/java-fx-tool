package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class I18n {
    @ExcelProperty("中文")
    private SimpleStringProperty remark = new SimpleStringProperty();
    @ExcelProperty("英语")
    private SimpleStringProperty remarkEn = new SimpleStringProperty();
    @ExcelProperty("波兰语")
    private SimpleStringProperty remarkPl = new SimpleStringProperty();
    @ExcelProperty("越南语")
    private SimpleStringProperty remarkVnm = new SimpleStringProperty();
    @ExcelProperty("西班牙语")
    private SimpleStringProperty remarkEs = new SimpleStringProperty();


    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getRemarkEn() {
        return remarkEn.get();
    }

    public SimpleStringProperty remarkEnProperty() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn.set(remarkEn);
    }

    public String getRemarkPl() {
        return remarkPl.get();
    }

    public SimpleStringProperty remarkPlProperty() {
        return remarkPl;
    }

    public void setRemarkPl(String remarkPl) {
        this.remarkPl.set(remarkPl);
    }

    public String getRemarkVnm() {
        return remarkVnm.get();
    }

    public SimpleStringProperty remarkVnmProperty() {
        return remarkVnm;
    }

    public void setRemarkVnm(String remarkVnm) {
        this.remarkVnm.set(remarkVnm);
    }

    public String getRemarkEs() {
        return remarkEs.get();
    }

    public SimpleStringProperty remarkEsProperty() {
        return remarkEs;
    }

    public void setRemarkEs(String remarkEs) {
        this.remarkEs.set(remarkEs);
    }
}
