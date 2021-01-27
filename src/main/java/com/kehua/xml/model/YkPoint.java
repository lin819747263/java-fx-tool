package com.kehua.xml.model;

import com.alibaba.excel.annotation.ExcelProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class YkPoint  extends BasePoint  implements Comparable<YkPoint>{

    @ExcelProperty(value = "rangeData", order = 15)
    private SimpleStringProperty rangeData = new SimpleStringProperty();
    @ExcelProperty(value = "nextProp",order = 16)
    private SimpleStringProperty nextProp = new SimpleStringProperty();
    @ExcelProperty(value = "defaultVal",order = 17)
    private SimpleStringProperty defaultVal = new SimpleStringProperty();
    @ExcelProperty(value = "regNum",order = 11)
    private SimpleStringProperty regNum = new SimpleStringProperty();
    @ExcelProperty(value = "num",order = 12)
    private SimpleStringProperty pointNum = new SimpleStringProperty();
    @ExcelProperty(value = "noCollect",order = 21)
    private SimpleBooleanProperty noCollect = new SimpleBooleanProperty();
    @ExcelProperty(value = "noRealTime",order = 22)
    private SimpleBooleanProperty noRealTime = new SimpleBooleanProperty();
    @ExcelProperty(value = "unit",order = 10)
    private SimpleStringProperty unit = new SimpleStringProperty();
    @ExcelProperty(value = "collectFuncode",order = 18)
    private SimpleStringProperty collectFuncode = new SimpleStringProperty();
    @ExcelProperty(value = "collectRegType",order = 19)
    private SimpleStringProperty collectRegType = new SimpleStringProperty();
    @ExcelProperty(value = "collectRegParam", order = 20)
    private SimpleStringProperty collectRegParam = new SimpleStringProperty();

    public YkPoint(){
        this.setFunCode("");
        this.setCollectFuncode("1");
        this.setRegType("3");
        this.setCollectRegType("3");
    }

    @Override
    public void setAddr(String addr) {
        super.setAddr(addr);
        addr = getAddr();
        if (StringUtils.isEmpty(addr)){
            this.setProperty("");
        }else {
            this.setProperty("YK" + addr);
        }
    }

    public String getRangeData() {
        return rangeData.get();
    }

    public SimpleStringProperty rangeDataProperty() {
        return rangeData;
    }

    public void setRangeData(String rangeData) {
        this.rangeData.set(rangeData);
    }

    public String getNextProp() {
        return nextProp.get();
    }

    public SimpleStringProperty nextPropProperty() {
        return nextProp;
    }

    public void setNextProp(String nextProp) {
        this.nextProp.set(nextProp);
    }

    public String getDefaultVal() {
        return defaultVal.get();
    }

    public SimpleStringProperty defaultValProperty() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal.set(defaultVal);
    }

    public String getRegNum() {
        return regNum.get();
    }

    public SimpleStringProperty regNumProperty() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum.set(regNum);
    }

    public String getPointNum() {
        return pointNum.get();
    }

    public SimpleStringProperty pointNumProperty() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum.set(pointNum);
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

    public String getCollectFuncode() {
        return collectFuncode.get();
    }

    public SimpleStringProperty collectFuncodeProperty() {
        return collectFuncode;
    }

    public void setCollectFuncode(String collectFuncode) {
        this.collectFuncode.set(collectFuncode);
    }

    public String getCollectRegType() {
        return collectRegType.get();
    }

    public SimpleStringProperty collectRegTypeProperty() {
        return collectRegType;
    }

    public void setCollectRegType(String collectRegType) {
        this.collectRegType.set(collectRegType);
    }

    public String getCollectRegParam() {
        return collectRegParam.get();
    }

    public SimpleStringProperty collectRegParamProperty() {
        return collectRegParam;
    }

    public void setCollectRegParam(String collectRegParam) {
        this.collectRegParam.set(collectRegParam);
    }

    public boolean isNoCollect() {
        return noCollect.get();
    }

    public SimpleBooleanProperty noCollectProperty() {
        return noCollect;
    }

    public void setNoCollect(boolean noCollect) {
        this.noCollect.set(noCollect);
    }

    public boolean isNoRealTime() {
        return noRealTime.get();
    }

    public SimpleBooleanProperty noRealTimeProperty() {
        return noRealTime;
    }

    public void setNoRealTime(boolean noRealTime) {
        this.noRealTime.set(noRealTime);
    }

    @Override
    public int compareTo(YkPoint o) {
        if (Integer.parseInt(this.getAddr()) == Integer.parseInt(o.getAddr())){
            return 0;
        }else if (Integer.parseInt(this.getAddr()) > Integer.parseInt(o.getAddr())){
            return 1;
        }else {
            return -1;
        }
    }
}
