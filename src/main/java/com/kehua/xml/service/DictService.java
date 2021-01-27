package com.kehua.xml.service;

import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class DictService {

    public void initDictData(){
        initProtocolType("协议类型");
        initLanguageType("语言");
        initUserDefined("自定义");
        initEvent("事件等级");
        initStore("存储周期");
        initAppYxGroup("APP遥信分组");
        initAppYcGroup("APP遥测分组");
        initAppYkGroup("APP遥控分组");
        initUserType("APP用户类型");
        initRecordTypeGroup("APP记录类型");
        initModuleGroup("模块类型");
    }

    private void writeProperties(String type, Properties properties){
        String path = PathUtil.getDictPath(GloableSetting.workPath) + type + ".properties";
        FileUtil.writeToProperties(properties,path);
    }

    private void initProtocolType(String type){
        Properties properties = new Properties();
        properties.put("三相组串式光伏逆变器","00010001");
        properties.put("集中式三相光伏逆变器","00010002");
        properties.put("三相光伏储能逆变器","00010001");
        properties.put("单项光伏变流器","00100002");
        properties.put("单相光伏储能变流器","00110002");
        properties.put("三项储能变流器","00200001");
        properties.put("1500V三项储能变流器","00200001");

        writeProperties(type, properties);
    }

    private void initLanguageType(String type){
        Properties properties = new Properties();

        properties.put("中文","zh-CN");
        properties.put("波兰语","pl");
        properties.put("英文","en");
        properties.put("越南语","vnm");
        properties.put("西班牙语","es");

        writeProperties(type, properties);
    }

    private void initUserDefined(String type){
        Properties properties = new Properties();

        properties.put("发电功率","activePower");
        properties.put("当日发电量","dayElec");
        properties.put("光伏功率","pvActivePower");
        properties.put("当日光伏发电量","dayPvInput");
        properties.put("负载功率","loadActivePower");

        properties.put("当日负载用电量","dayConsum");
        properties.put("售电功率","gridActivePower");
        properties.put("当日售电电量","dayOngrid");
        properties.put("购电功率","gridActivePower");
        properties.put("当日购电电量","dayOffgrid");

        properties.put("电池充电功率","chargePower");
        properties.put("当日充电电量","dayCharge");
        properties.put("累计充电电量","totalChange");
        properties.put("剩余电池容量","batteryCapacity");
        properties.put("电池放电功率","batteryPower");

        properties.put("当日放电电量","dayDischarge");
        properties.put("累计放电电量","totalDischange");
        properties.put("累计发电量","totalElec");
        properties.put("累计光伏发电量","totalPvInput");
        properties.put("累计负载用电量","totalConsum");

        properties.put("累计售电电量","totalOngrid");
        properties.put("累计购电电量","totalOffgrid");
        writeProperties(type, properties);
    }

    private void initStore(String type){
        Properties properties = new Properties();

        properties.put("长期存储(25年)","2");
        properties.put("短期存储(30天)","1");
        properties.put("不存储","0");

        writeProperties(type, properties);
    }

    private void initEvent(String type){
        Properties properties = new Properties();

        properties.put("故障","1");
        properties.put("告警","2");
        properties.put("事件","3");
        properties.put("不告警","4");

        writeProperties(type, properties);
    }

    private void initUserType(String type){
        Properties properties = new Properties();

        properties.put("厂家","1");
        properties.put("运维","2");
        properties.put("用户","3");

        writeProperties(type, properties);
    }

    private void initAppYxGroup(String type){
        Properties properties = new Properties();

        properties.put("普通告警类","yx_normal");
        properties.put("内部告警类","yx_inside");

        writeProperties(type, properties);
    }
    private void initAppYcGroup(String type){
        Properties properties = new Properties();

        properties.put("设备信息","yc_device");
        properties.put("设备状态","yc_status");
        properties.put("电池信息","yc_battery");
        properties.put("电网信息","yc_grid");
        properties.put("光伏信息","yc_pv");
        properties.put("负载信息","yc_load");
        properties.put("支路数据","yc_access");
        properties.put("内部信息","yc_inside");

        writeProperties(type, properties);
    }
    private void initAppYkGroup(String type){
        Properties properties = new Properties();

        properties.put("基本设置","yk_base");
        properties.put("高级设置","yk_senior");
        properties.put("电网设置","yk_grid");
        properties.put("电池设置","yk_battery");
        properties.put("调度设置","yk_dispatch");
        properties.put("系统设置","yk_system");
        properties.put("模式设置","yk_mode");
        properties.put("其他设置","yk_other");
        properties.put("时段设置","yk_time");
        properties.put("校准设置","yk_cakubratuib");

        writeProperties(type, properties);
    }

    private void initRecordTypeGroup(String type){
        Properties properties = new Properties();

        properties.put("功率","POWERLOGER");
        properties.put("用户","USERLOGER");
        properties.put("告警","ALARMLOGER");
        properties.put("并网","GRIDLOGER");

        writeProperties(type, properties);
    }

    private void initModuleGroup(String type){
        Properties properties = new Properties();

        properties.put("监控模块","101");
        properties.put("告警模块","201");
        properties.put("控制模块","301");
        properties.put("并脱网记录数量","401");
        properties.put("历史故障数量","402");
        properties.put("用户日志记录数量","403");
        properties.put("功率调度记录数量","404");
        properties.put("记录请求数据写入","405");
        properties.put("记录数据读取","406");

        writeProperties(type, properties);
    }


}
