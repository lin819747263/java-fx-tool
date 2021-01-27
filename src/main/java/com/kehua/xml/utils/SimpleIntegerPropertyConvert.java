package com.kehua.xml.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.math.BigDecimal;

public class SimpleIntegerPropertyConvert implements Converter<Integer> {
    @Override
    @SuppressWarnings("rawtypes")
    public Class supportJavaTypeKey() {
        return SimpleIntegerProperty.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return cellData.getNumberValue().intValue();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData(new BigDecimal(value));
    }


}
