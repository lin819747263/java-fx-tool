package com.kehua.xml.controller;

import com.kehua.xml.control.table.handle.TableColumnMouseHandle;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.service.TemplateService;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.PathUtil;
import com.kehua.xml.utils.UIFactory;
import com.kehua.xml.view.DictView;
import com.kehua.xml.view.SystemSettingView;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class BaseMainController {

    @FXML
    protected BorderPane tablePane;

    private Scene settingScene;
    private Scene dictScene;

    @Autowired
    private SystemSettingView systemSettingView;
    @Autowired
    private DictView dictView;
    @Autowired
    private TemplateService templateService;


    /**
     * 字典管理界面
     * @param actionEvent
     */
    public void dict(ActionEvent actionEvent) {
        Stage stage = new Stage();
        if (dictScene == null || dictScene.getRoot() == null) {
            dictScene = new Scene(dictView.getView());
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(dictScene);
        stage.show();
    }

    /**
     * 打开网站
     * @param actionEvent
     * @throws IOException
     * @throws URISyntaxException
     */
    public void openWeb(ActionEvent actionEvent) throws IOException, URISyntaxException {
        java.awt.Desktop.getDesktop().browse(new URI("http://energy.kehua.com.cn/"));
    }

    /**
     * 软件指导说明书
     * @param actionEvent
     * @throws IOException
     */
    public void openGuide(ActionEvent actionEvent) throws IOException {
        File file = new File(PathUtil.getBasePath(GloableSetting.workPath) + "软件手册.docx");
        Desktop.getDesktop().open(file);
    }

    /**
     * 协议配置指导说明
     * @param actionEvent
     * @throws IOException
     */
    public void openSetting(ActionEvent actionEvent) throws IOException {
        File file = new File(PathUtil.getBasePath(GloableSetting.workPath) + "如何配置.docx");
        Desktop.getDesktop().open(file);
    }

    /**
     * 导出国际化
     * @param actionEvent
     */
    public void exportI18n(ActionEvent actionEvent) {
        File chooseFile = UIFactory.saveFile("I18n.xlsx");
        if (chooseFile == null) return;
        templateService.exportI18n(chooseFile);
        AlertFactory.createAlert("导出成功");
    }

    /**
     * 导入国际化
     * @param actionEvent
     */
    public void importI18n(ActionEvent actionEvent) {
        File file = UIFactory.chooseFile();
        if (file != null) {
            Boolean res = templateService.checkI18nFile(file);
            if (!res) {
                AlertFactory.createAlert("文件格式有误，请重新选择");
                return;
            }
            templateService.importI18n(file);
            AlertFactory.createAlert("导入成功");
        }

    }

    /**
     * 系统设置
     * @param actionEvent
     */
    public void setting(ActionEvent actionEvent) {
        Stage stage = new Stage();
        if (settingScene == null || settingScene.getRoot() == null) {
            settingScene = new Scene(systemSettingView.getView());
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(settingScene);
        stage.show();
    }

    /**
     * 块保存
     * @param actionEvent
     */
    public void saveBlock(ActionEvent actionEvent) {
        CommonDataManager.BLOCK_QUEUE.clear();
        TableView tableView = (TableView) tablePane.getCenter();
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        if(selectedCells.size() == 0) return;

        Integer first = getFirstValue(tableView, selectedCells);
        selectedCells.forEach(cell -> {
            TableColumn column = tableView.getVisibleLeafColumn(cell.getColumn());
            if(column.getText().contains("地址")){
                Object value = column.getCellData(cell.getRow());
                Integer differ = Integer.parseInt(value.toString()) - first;
                CommonDataManager.BLOCK_QUEUE.add(differ);
            }
        });
    }

    public Integer getFirstValue(TableView tableView, ObservableList<TablePosition> selectedCells){
        TablePosition tablePosition = selectedCells.get(0);
        TableColumn column = tableView.getVisibleLeafColumn(tablePosition.getColumn());
        Object value = column.getCellData(tablePosition.getRow());
        return Integer.valueOf(value.toString());
    }

    /**
     * 块应用
     * @param actionEvent
     */
    public void applyBlock(ActionEvent actionEvent) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = UIFactory.createBaseGridPane();

        TextField startAddr = new TextField();
        startAddr.setPrefWidth(200);

        grid.add(new Label("起始地址"), 0, 0);
        grid.add(startAddr, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                checkAddr(startAddr.getText());
            }catch (Exception e){
                AlertFactory.createAlert(e.getMessage());
                return;
            }

            Integer addr = Integer.valueOf(startAddr.getText());
            TableView tableView = (TableView) tablePane.getCenter();
            ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
            int size = CommonDataManager.BLOCK_QUEUE.size();
            if(size == 0 || selectedCells.size() == 0) return;

            int i = 0;
            for(TablePosition cell :selectedCells){
                if(i >= size){
                    i=0;
                }
                TableColumn column = tableView.getVisibleLeafColumn(cell.getColumn());
                if(column.getText().contains("地址")){
                    Object value = CommonDataManager.BLOCK_QUEUE.get(i);
                    if(value != null){
                        ObservableValue<?> observableValue = column.getCellObservableValue(cell.getRow());
                        if(observableValue instanceof StringProperty){
                            ((StringProperty) observableValue).set(String.valueOf(addr + Integer.parseInt(value.toString())));
                        }
                    }
                }
                i++;
            }

        }
    }

    private void checkAddr(String text) {
        int addr;
        try {
            addr = Integer.parseInt(text);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("只能填写数字类型");
        }
        if(addr == 0){
            throw new RuntimeException("起始地址不能为零");
        }
    }

    /**
     * 左边菜单点击
     * @param mouseEvent
     */
    public void leftMenuClick(MouseEvent mouseEvent) {
        try {
            HBox hBox = (HBox) mouseEvent.getSource();
            tablePaneSetCenter(hBox);
            leftMenuActive(hBox);
            TableColumnMouseHandle.tooltip.hide();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void leftMenuActive(HBox hBox) {
        VBox vBox = (VBox) hBox.getParent();
        vBox.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                ObservableList<String> styleClass = node.getStyleClass();
                styleClass.remove("active");
            }
        });
        hBox.getStyleClass().add("active");
    }

    public void tablePaneSetCenter(HBox hBox) throws NoSuchFieldException, IllegalAccessException {

    }
}
