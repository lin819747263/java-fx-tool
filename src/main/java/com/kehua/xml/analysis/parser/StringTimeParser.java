package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

/**
 * 12:25 格式解析器
 */
public class StringTimeParser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        String valueStr = ByteUtil.bytesToHexString(bytes);
        String low = valueStr.substring(2,4);
        String high = valueStr.substring(0,2);
        String hour;
        String min;
        if(Integer.parseInt(high,16) < 10){
            hour = "0" + Integer.parseInt(high,16);
        }else{
            hour = "" + Integer.parseInt(high,16);
        }
        if(Integer.parseInt(low,16) < 10){
            min = "0" + Integer.parseInt(low,16);
        }else{
            min = "" + Integer.parseInt(low,16);
        }
        return hour + ":" + min;
    }
}
