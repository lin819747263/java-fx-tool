package com.kehua.xml.data;

import com.kehua.xml.model.Device;
import com.kehua.xml.model.app.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class AppTableDataManager {

    public final static Map<String, String> GROUP_I18N = new HashMap<>();

    static {
        GROUP_I18N.put("设备信息", "Base");
        GROUP_I18N.put("设备状态", "Status");
        GROUP_I18N.put("电池信息", "Battery");
        GROUP_I18N.put("电网信息", "Grid");
        GROUP_I18N.put("光伏信息", "PV");
        GROUP_I18N.put("负载信息", "Load");
        GROUP_I18N.put("支路数据", "Access");
        GROUP_I18N.put("内部信息", "Internal");
        GROUP_I18N.put("普通告警类", "Alarm");
        GROUP_I18N.put("内部告警类", "Internal Alarm");
        GROUP_I18N.put("基本设置", "Base");
        GROUP_I18N.put("高级设置", "Senior");
        GROUP_I18N.put("电网设置", "Grid");
        GROUP_I18N.put("电池设置", "Battery");
        GROUP_I18N.put("调度设置", "Dispatch");
        GROUP_I18N.put("系统设置", "Inside");
        GROUP_I18N.put("模式设置", "Mode");
        GROUP_I18N.put("其他设置", "Other");
        GROUP_I18N.put("时段设置", "ChargeTime");
        GROUP_I18N.put("校准设置", "Cakubratuib");
    }

    public final static ObservableList<Device> DEVICES =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<AppBlockPoint> BLOCKS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<AppYcPoint> YC_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<AppYxPoint> YX_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<AppYkPoint> YK_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<RecordPoint> RECORDS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<Group> GROUPS =
            FXCollections.observableArrayList(
            );

}
