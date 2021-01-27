package com.kehua.xml.utils;

import com.kehua.xml.data.CommonDataManager;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class UIFactory {

    public static GridPane createBaseGridPane(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 30));
        return grid;
    }

    public static File chooseDir() {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择文件夹");
        if(CommonDataManager.currentFile !=null){
            directoryChooser.setInitialDirectory(new File(CommonDataManager.currentFile.getParent()));
        }else {
            directoryChooser.setInitialDirectory(new File("C:\\"));
        }
        stage.initModality(Modality.WINDOW_MODAL);
        return directoryChooser.showDialog(stage);
    }

    public static File chooseFile() {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择要打开的文件");
        if(CommonDataManager.currentFile !=null){
            fileChooser.setInitialDirectory(new File(CommonDataManager.currentFile.getParent()));
        }else {
            fileChooser.setInitialDirectory(new File("C:\\"));
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        stage.initModality(Modality.WINDOW_MODAL);
        return fileChooser.showOpenDialog(stage);
    }

    public static File saveFile(String initFileName) {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择要打开的文件");
        if(CommonDataManager.currentFile !=null){
            fileChooser.setInitialDirectory(new File(CommonDataManager.currentFile.getParent()));
        }else {
            fileChooser.setInitialDirectory(new File("C:\\"));
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        stage.initModality(Modality.WINDOW_MODAL);
        fileChooser.setInitialFileName(initFileName);
        return fileChooser.showSaveDialog(stage);
    }


}
