package com.kehua.xml.controller;

import com.kehua.xml.service.KeyCombinationService;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.view.app.AppMainView;
import com.kehua.xml.view.web.MainView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@FXMLController
public class HomeController {
    @Autowired
    private MainView mainView;
    @Autowired
    private AppMainView appMainView;
    @Autowired
    KeyCombinationService keyCombinationService;

    public Label web;
    public Label app;

    public void initialize(){
        web.setOnMouseClicked(event -> initView(mainView.getView()));
        app.setOnMouseClicked(event -> initView(appMainView.getView()));

        GUIState.getStage().setOnCloseRequest(event -> {
            Optional<ButtonType> result = AlertFactory.createAlert("关闭窗口会导致当前文件丢失，请确认是否保存", Alert.AlertType.CONFIRMATION);
            if(result.isPresent() && result.get().equals(ButtonType.CANCEL)){
                event.consume();
            }
        });
    }

    private void initView(Parent view){
        GUIState.getStage().hide();
        Scene scene = new Scene(view,1200, 600);
        GUIState.getStage().setScene(scene);
        GUIState.getStage().centerOnScreen();
        GUIState.getStage().show();
        keyCombinationService.initKeyCombination(scene);
    }
}
