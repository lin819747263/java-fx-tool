package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

public class StringHexParser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        return "0x" + ByteUtil.bytesToHexString(bytes);
    }
}
