package com.kehua.xml.controller.web;

import com.kehua.xml.controller.BaseMainController;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.exception.CheckException;
import com.kehua.xml.service.*;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import com.kehua.xml.utils.UIFactory;
import com.kehua.xml.view.web.*;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@FXMLController
@Slf4j
public class MainController extends BaseMainController {

    public Menu menuRecovery;

    private Scene scene;


    @Autowired
    private HelpService helpService;
    @Autowired
    DictService dictService;
    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    I18nService i18nService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DebugService debugService;
    @Autowired
    private TableDataService tableDataService;


    @Autowired
    private TableDeviceView tableDeviceView;
    @Autowired
    private TableBlockView tableBlockView;
    @Autowired
    private TableYxView tableYxView;
    @Autowired
    private TableYcView tableYcView;
    @Autowired
    private TableYkView tableYkView;
    @Autowired
    private TableEventView tableEventView;
    @Autowired
    private TableI18nView tableI18nView;
    @Autowired
    private TableStoreView tableStoreView;
    @Autowired
    private DebugView debugView;

    public void initialize() throws IOException {
        systemSettingService.initSetting();
        dictService.initDictData();
        helpService.initFile();
        tablePane.setCenter(tableDeviceView.getView());
        GUIState.getStage().setTitle("XMLTool(Web模式)");
        fileService.createBackupFileTask(GloableSetting.backTimeInterval).play();
    }

    private List<MenuItem> getItems(String path) {
        List<MenuItem> items = new ArrayList<>();
        List<String> files = FileUtil.getAllFile(path);
        files.forEach(x -> items.add(new MenuItem(x)));
        return items;
    }

    public void open(ActionEvent actionEvent) {
        try {
            fileService.open();
        }catch (Exception e) {
            log.warn("文件打开失败",e);
            AlertFactory.createAlert("文件打开失败");
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            fileService.save();
        }catch (Exception e) {
            log.warn("文件保存失败",e);
            AlertFactory.createAlert("文件保存失败" + e.getMessage());
        }
    }

    public void saveAs(ActionEvent actionEvent) {
        try {
            fileService.saveAs();
        }catch (Exception e) {
            log.warn("文件保存失败",e);
            AlertFactory.createAlert("文件保存失败" + e.getMessage());
        }
    }

    public void exportZip(ActionEvent actionEvent) {
        try {
            fileService.exportZip();
        }catch (Exception e){
            log.warn("文件导出失败",e);
            AlertFactory.createAlert("文件导出失败");
        }
    }

    public void extractSetting(ActionEvent actionEvent) {
        try {
            templateService.extractSetting();
            AlertFactory.createAlert("配置提取成功");
        } catch (Exception e) {
            AlertFactory.createAlert(e.getMessage());
        }
    }

    public void createBlocks(ActionEvent actionEvent) {
        try {
            templateService.createBlocks();
            AlertFactory.createAlert("查询指令生成完成");
        } catch (Exception e) {
            AlertFactory.createAlert(e.getMessage());
        }
    }

    public void deviceDebug(ActionEvent actionEvent) throws IOException {
        debugService.initData();
        Stage stage = new Stage();
        if (scene == null || scene.getRoot() == null) {
            scene = new Scene(debugView.getView(), 750, 400);
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            TableDataManager.DEBUG_COLLECT_YK.clear();
            TableDataManager.DEBUG_COLLECT.clear();
        });

    }

    @Override
    public void tablePaneSetCenter(HBox hBox) throws NoSuchFieldException, IllegalAccessException {
        AbstractFxmlView view = (AbstractFxmlView) getClass().getDeclaredField(hBox.getId()).get(this);
        tablePane.setCenter(view.getView());
    }

    public void check(ActionEvent actionEvent) {
        try {
            templateService.checkTemplate();
            AlertFactory.createAlert("校验通过");
        } catch (CheckException e) {
            AlertFactory.createAlert(e.getMessage());
        } catch (Exception e){
            log.warn("校验出错", e);
            AlertFactory.createAlert("校验过程错误，详情请查看日志");
        }
    }

    public void extractI18n(ActionEvent actionEvent) {
        i18nService.extractI18n();
    }

    public void newTemplate(ActionEvent actionEvent) {
        try {
            fileService.newTemplate();
        }catch (IllegalArgumentException e){
            AlertFactory.createAlert("参数不能为空");
        }
    }

    public void menuRecovery(ActionEvent actionEvent) {
        menuRecovery.getItems().clear();
        menuRecovery.getItems().addAll(getItems(PathUtil.getBackPath(GloableSetting.workPath)));
        menuRecovery.getItems().forEach(x -> x.setOnAction(event -> fileService.openFile(new File(x.getText()))));
    }

    public void convertTemplate(ActionEvent event) throws FileNotFoundException, DocumentException {
        File file = UIFactory.chooseFile();
        if(file == null) return;
        templateService.convertTemplate(file);
    }

    public void importEvent(ActionEvent event) {
        File file = UIFactory.chooseFile();
        if(file == null) return;
        templateService.importEvent(file);
    }

    public void importStore(ActionEvent event) {
        File file = UIFactory.chooseFile();
        if(file == null) return;
        templateService.importStore(file);
    }
}