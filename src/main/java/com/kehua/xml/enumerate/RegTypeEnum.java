package com.kehua.xml.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HuangTengfei
 * 2020/12/4
 * description：
 */
@Getter
@AllArgsConstructor
public enum RegTypeEnum {

    $8U("1", "8位无符号"),
    $8("2", "8位有符号"),
    $16U("3", "16位无符号"),
    $16("4", "16位有符号"),
    $32U("5", "32位无符号"),
    $32("6", "32位有符号"),
    $16F("7", "16位浮点数"),
    $32F("8", "32位浮点数"),
    $String("9", "字符串"),
    $Alarm2("81", "告警量（2态）"),
    $Alarm("82", "告警量（大于2态）"),
    $Status("83", "状态量");

    String value;
    String title;

}
