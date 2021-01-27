package com.kehua.xml.analysis.parser;

import java.nio.charset.StandardCharsets;

public class StringAsciiParser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII).trim();
    }
}
