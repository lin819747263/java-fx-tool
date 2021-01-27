package com.kehua.xml.controller.web;

import com.kehua.xml.view.web.DebugCollectorView;
import com.kehua.xml.view.web.DebugLogView;
import com.kehua.xml.view.web.DebugYkView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class DebugController {

    @FXML
    private BorderPane debugPane;

    @Autowired
    private DebugCollectorView debugCollectorView;
    @Autowired
    private DebugLogView debugLogView;
    @Autowired
    private DebugYkView debugYkView;

    public void initialize() {
        debugLog();
        debugYk();
        debugDevice();
    }

    public void debugDevice() {
        active(0);
        debugPane.setCenter(debugCollectorView.getView());
    }

    public void debugYk() {
        active(1);
        debugPane.setCenter(debugYkView.getView());
    }

    public void debugLog() {
        active(2);
        debugPane.setCenter(debugLogView.getView());
    }

    private void active(Integer labelIndex){
        ObservableList<Node> children = ((HBox) debugPane.getTop()).getChildren();
        for (int i = 0; i< children.size(); i++){
            if (i == labelIndex){
                children.get(i).getStyleClass().add("debug-top-active");
            }else {
                children.get(i).getStyleClass().remove("debug-top-active");
            }
        }
    }
}
