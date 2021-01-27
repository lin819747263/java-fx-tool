package com.kehua.xml.controller.app;

import com.kehua.xml.controller.BaseMainController;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.exception.CheckException;
import com.kehua.xml.service.*;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import com.kehua.xml.view.app.*;
import com.kehua.xml.view.web.TableI18nView;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@FXMLController
@Slf4j
public class AppMainController extends BaseMainController {

    public Menu menuRecovery;


    @Autowired
    private HelpService helpService;
    @Autowired
    DictService dictService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    I18nService i18nService;
    @Autowired
    private AppFileService appFileService;
    @Autowired
    private AppI18nService appI18nService;
    @Autowired
    AppTemplateService appTemplateService;
    @Autowired
    TemplateService templateService;


    @Autowired
    private AppTableDeviceView tableDeviceView;
    @Autowired
    private AppTableBlockView tableBlockView;
    @Autowired
    private AppTableYxView tableYxView;
    @Autowired
    private AppTableYcView tableYcView;
    @Autowired
    private AppTableYkView tableYkView;
    @Autowired
    private AppTableRecordView tableRecordView;
    @Autowired
    private TableI18nView tableI18nView;
    @Autowired
    private TableGroupView tableGroupView;


    public void initialize() throws IOException {
        systemSettingService.initSetting();
        dictService.initDictData();
        helpService.initFile();
        tablePane.setCenter(tableDeviceView.getView());
        GUIState.getStage().setTitle("XMLTool(APP模式)");
        appFileService.createBackupFileTask(GloableSetting.backTimeInterval).play();
    }

    public void open(ActionEvent actionEvent) {
        try {
            appFileService.open();
        }catch (Exception e) {
            log.warn("文件打开失败", e);
            AlertFactory.createAlert("文件打开失败" + e.getMessage());
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            appFileService.save();
        }catch (Exception e) {
            log.warn("文件保存失败", e);
            AlertFactory.createAlert("文件保存失败" + e.getMessage());
        }
    }

    public void saveAs(ActionEvent actionEvent) {
        try {
            appFileService.saveAs();
        }catch (Exception e) {
            log.warn("文件保存失败", e);
            AlertFactory.createAlert("文件保存失败" + e.getMessage());
        }
    }

    public void exportZip(ActionEvent actionEvent) {
        try {
            appFileService.exportZip();
        }catch (CheckException e){
            AlertFactory.createAlert(e.getMessage());
        }catch (Exception e) {
            log.warn("文件导出失败", e);
            AlertFactory.createAlert("文件导出失败" + e.getMessage());
        }
    }

    @Override
    public void tablePaneSetCenter(HBox hBox) throws NoSuchFieldException, IllegalAccessException {
        AbstractFxmlView view = (AbstractFxmlView) getClass().getDeclaredField(hBox.getId()).get(this);
        tablePane.setCenter(view.getView());
    }

    public void menuRecovery(ActionEvent actionEvent) {
        menuRecovery.getItems().clear();
        menuRecovery.getItems().addAll(getItems(PathUtil.getBackPath(GloableSetting.workPath)));
        menuRecovery.getItems().forEach(x -> x.setOnAction(event -> appFileService.openFile(new File(x.getText()))));
    }

    private List<MenuItem> getItems(String path) {
        List<MenuItem> items = new ArrayList<>();
        List<String> files = FileUtil.getAllFile(path);
        files.forEach(x -> items.add(new MenuItem(x)));
        return items;
    }

    public void extractI18n(ActionEvent event) {
        appI18nService.extractAppI18n();
        AlertFactory.createAlert("提取成功");
    }

    public void createBlocks(ActionEvent actionEvent) {
        try {
            appTemplateService.createAppBlocks();
        } catch (CheckException e) {
            AlertFactory.createAlert(e.getMessage());
        } catch (Exception e){
            log.warn("校验错误", e);
            AlertFactory.createAlert("校验过程错误，详情请查看日志");
        }
    }
}
