package com.kehua.xml.controller;


import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.model.Dict;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.FileUtil;
import com.kehua.xml.utils.PathUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Properties;

/**
 * @author admin
 */
@FXMLController
public class DictController extends BaseController<Dict>{

    private String currentDict = "";

    @FXML
    public BorderPane tablePane;
    @FXML
    public TableView<Dict> table;
    @FXML
    public TableColumn<Dict, String> key;
    @FXML
    public TableColumn<Dict, String> value;
    @FXML
    public VBox vbox;
    public Button buttonSave;

    public void initialize(){
        baseTableColumnInit(key);
        baseTableColumnInit(value);
        initializeBase(CommonDataManager.DICT);
        table.setContextMenu(TableContextMenu.getDictMenuBox(table, Dict.class));

        initGroup();
    }

    private void initGroup() {
        List<String> files = FileUtil.getAllFile(PathUtil.getDictPath(GloableSetting.workPath));
        for (String file : files) {
            String[] urls = file.split("\\\\");
            Label label = new Label(urls[urls.length -1].replace(".properties",""));
            label.setOnMouseClicked(event -> {
                try {
                    getProperties(file);
                    currentDict = file;
                    clearStyle();
                    label.setStyle("-fx-background-color: #dddddd");
                    label.setPrefWidth(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            label.setPadding(new Insets(5,10,5,10));
            vbox.getChildren().add(label);
        }
    }

    public void clearStyle(){
        vbox.getChildren().forEach(x -> x.setStyle(""));
    }

    public void getProperties(String path) {
        Properties properties = FileUtil.readProperties(path);

        CommonDataManager.DICT.clear();
        properties.keySet().forEach(x -> {
            Dict dict = new Dict();
            dict.setKey(x.toString());
            dict.setValue(properties.getProperty(x.toString()));
            CommonDataManager.DICT.add(dict);
        });
    }

    public void save(ActionEvent actionEvent) {
        String path = currentDict;
        if(StringUtils.isBlank(path)){
            AlertFactory.createAlert("请选择字典类型");
            return;
        }
        Properties properties = new Properties();
        for(Dict dict : CommonDataManager.DICT){
            properties.put(dict.getKey(), dict.getValue());
        }
        FileUtil.writeToProperties(properties,path);
    }
}
