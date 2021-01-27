package com.kehua.xml.analysis.parser;

import java.nio.charset.StandardCharsets;

public class StringUTF8Parser implements Parser{
    @Override
    public String parser(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }
}
