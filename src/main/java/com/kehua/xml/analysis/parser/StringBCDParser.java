package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

public class StringBCDParser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        return ByteUtil.bytesToHexString(bytes);
    }
}
