package com.kehua.xml.control.table.handle.mouseeven;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class OnMousePressedHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        OrderClickedHandler.initialize(event);
    }
}
