package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

import java.io.UnsupportedEncodingException;

public class IntSignedParser implements Parser{
    private double ratio;

    public IntSignedParser(double ratio){
        this.ratio = ratio;
    }

    @Override
    public String parser(byte[] bytes) throws UnsupportedEncodingException {
        return String.valueOf(ByteUtil.longFromBytes(bytes) * ratio);
    }
}
