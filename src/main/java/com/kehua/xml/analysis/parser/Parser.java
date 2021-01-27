package com.kehua.xml.analysis.parser;

import java.io.UnsupportedEncodingException;

public interface Parser {
    /**
     *
     * @param bytes 数组值
     * @return 返回值
     */
    String parser(byte[] bytes) throws UnsupportedEncodingException;
}
