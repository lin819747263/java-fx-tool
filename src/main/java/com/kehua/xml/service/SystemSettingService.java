package com.kehua.xml.service;

import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SystemSettingService {

    public void initSetting(){
        Properties properties = FileUtil.readProperties(PathUtil.getSettingPath(GloableSetting.workPath) + "setting.properties");
        if(properties.size() != 0){
            GloableSetting.workPath = properties.get("workPath").toString();
            GloableSetting.backTimeInterval = Integer.parseInt(properties.get("backTimeInterval").toString());
            GloableSetting.blockMaxNum = Integer.parseInt(properties.get("blockMaxNum").toString());
            GloableSetting.blockIntervalNum = Integer.parseInt(properties.get("blockIntervalNum").toString());
        }
    }
}
