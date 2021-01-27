package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

import java.io.UnsupportedEncodingException;

public class IntUnSignedParser implements Parser{

    private double ratio;

    public IntUnSignedParser(double ratio){
        this.ratio = ratio;
    }

    @Override
    public String parser(byte[] bytes) throws UnsupportedEncodingException {
        long valueInt = 0;
        String binaryString = Integer.toBinaryString(ByteUtil.intFromBytes(bytes));
        if (binaryString.startsWith("1")) {
            String reverseStr = reverseZeroOne(binaryString);
            valueInt = -(Long.parseLong(reverseStr, 2) + 1);
        } else {
            valueInt = Long.parseLong(ByteUtil.bytesToHexString(bytes), 16);
        }
        return String.valueOf(valueInt * ratio);
    }


    public static String reverseZeroOne(String source){
        StringBuffer returnStr = new StringBuffer();
        for(int i=0 ;i <source.length();i++){
            if(source.startsWith("0", i)){
                returnStr.append("1");
            }else if(source.startsWith("1", i)){
                returnStr.append("0");
            }
        }
        return returnStr+"";
    }
}
