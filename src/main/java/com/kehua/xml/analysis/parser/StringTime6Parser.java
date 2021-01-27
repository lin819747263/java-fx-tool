package com.kehua.xml.analysis.parser;

import com.kehua.xml.utils.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020-12-11 12:23:00 格式解析器
 */
public class StringTime6Parser implements Parser{
    @Override
    public String parser(byte[] bytes) {

        String cmd = ByteUtil.bytesToHexString(bytes);
        List<String> cmdList = getStrList(cmd, 2);

        String sb = "20" +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(0)), 16)) +
                "-" +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(1)), 16)) +
                "-" +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(2)), 16)) +
                " " +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(3)), 16)) +
                ":" +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(4)), 16)) +
                ":" +
                String.format("%02d", Long.parseLong(String.valueOf(cmdList.get(5)), 16));

        return sb;
    }

    private List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    private List<String> getStrList(String inputString, int length,
                                    int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    private String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f);
        } else {
            return str.substring(f, t);
        }
    }
}
