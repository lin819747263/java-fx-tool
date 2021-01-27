package com.kehua.xml.view.web;


import com.kehua.xml.controller.web.DebugCollectSettingController;
import com.kehua.xml.data.SettingConfig;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLView("/fxml/web/debud-collect-setting.fxml")
public class DebugCollectSettingView extends AbstractFxmlView {

    @Autowired
    DebugCollectSettingController settingController;

    public void init(){
        SettingConfig config = SettingConfig.getDefault();
        Integer addr = config.getAddr();
        settingController.addr.setText((addr == null ? "" : addr).toString());
        SettingConfig.ConnectWay connect = config.getConnectWay();
        if (connect instanceof SettingConfig.TCPConnect){
            settingController.tcp.setSelected(true);
            settingController.setHost(((SettingConfig.TCPConnect) connect).getHost());
            settingController.setPort(((SettingConfig.TCPConnect) connect).getPort());
        }else if (connect instanceof SettingConfig.SERIALConnect){
            settingController.serial.setSelected(true);
            settingController.setCommPort(((SettingConfig.SERIALConnect) connect).getCommPort());
            settingController.setBaudRate(((SettingConfig.SERIALConnect) connect).getBaudRate());
        }
    }

}
