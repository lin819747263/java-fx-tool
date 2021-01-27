package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

public class StringBinaryParser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        return Integer.toBinaryString(ByteUtil.intFromBytes(bytes));
    }
}
