package com.kehua.xml.controller;

import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import com.kehua.xml.utils.UIFactory;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Properties;

@FXMLController
public class SystemSettingController {

    public TextField dir;
    public TextField regNum;
    public TextField maxRegNum;
    public TextField backupInterval;

    public void initialize(){
        initSetting();
    }

    public void chooseDir(ActionEvent actionEvent) {
        File file = UIFactory.chooseDir();
        if(file == null) return;
        dir.clear();
        dir.appendText(file.getAbsolutePath());
    }

    public void save(ActionEvent actionEvent) {
        GloableSetting.workPath = dir.getText();
        if(StringUtils.isBlank(backupInterval.getCharacters()) ||
                StringUtils.isBlank(backupInterval.getCharacters()) ||
                StringUtils.isBlank(backupInterval.getCharacters())){
            AlertFactory.createAlert("信息不可以为空");
            return;
        }
        try {
            GloableSetting.backTimeInterval = Integer.parseInt(backupInterval.getCharacters().toString());
            GloableSetting.blockMaxNum = Integer.parseInt(maxRegNum.getCharacters().toString());
            GloableSetting.blockIntervalNum = Integer.parseInt(regNum.getCharacters().toString());
        }catch (Exception e){
            AlertFactory.createAlert("信息填写错误");
        }

        flush2Properties();
        AlertFactory.createAlert("保存成功");
    }

    private void flush2Properties() {
        Properties properties = new Properties();
        properties.put("workPath" , GloableSetting.workPath);
        properties.put("backTimeInterval" , String.valueOf(GloableSetting.backTimeInterval));
        properties.put("blockMaxNum" , String.valueOf(GloableSetting.blockMaxNum));
        properties.put("blockIntervalNum" , String.valueOf(GloableSetting.blockIntervalNum ));
        FileUtil.writeToProperties(properties, PathUtil.getSettingPath(GloableSetting.workPath) + "setting.properties");
    }

    private void initSetting(){
        dir.appendText(GloableSetting.workPath);
        backupInterval.appendText(String.valueOf(GloableSetting.backTimeInterval));
        maxRegNum.appendText(String.valueOf(GloableSetting.blockMaxNum));
        regNum.appendText(String.valueOf(GloableSetting.blockIntervalNum));
    }
}
