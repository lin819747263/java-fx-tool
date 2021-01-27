package com.kehua.xml.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HuangTengfei
 * 2020/12/24
 * description：
 */
@Getter
@AllArgsConstructor
public enum ModbusFunCodeEnum {

    Coil_Status(1, "读线圈"),
    Input_Status(2, "读状态"),
    Holding_Register(3, "模拟量"),
    Input_Register(4, "模拟量");

    private final Integer value;
    private final String title;

}
