package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

import java.io.UnsupportedEncodingException;

public class BitParser implements Parser{
    private int offset;

    public BitParser(int offset){
        this.offset = offset;
    }

    @Override
    public String parser(byte[] bytes) throws UnsupportedEncodingException {
        String bits =  Integer.toBinaryString(ByteUtil.intFromBytes(bytes));
        return bits.substring(offset,offset + 1);
    }
}
