package com.kehua.xml.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
@AllArgsConstructor
@Getter
public enum EndianEnum {

    Big("0", "大端"),
    Small("1", "小端");

    String value;
    String title;
}
