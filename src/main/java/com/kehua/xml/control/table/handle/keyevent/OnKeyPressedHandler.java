package com.kehua.xml.control.table.handle.keyevent;

import com.kehua.xml.control.table.tool.Copy;
import com.kehua.xml.control.table.tool.Paste;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class OnKeyPressedHandler implements EventHandler<KeyEvent> {

    KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
    KeyCodeCombination pasteKeyCodeCompination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_ANY);

    @Override
    public void handle(KeyEvent event) {
        if (copyKeyCodeCompination.match(event)) {

            if (event.getSource() instanceof TableView) {

                // copy to clipboard
                Copy.copy((TableView<?>) event.getSource());

                // event is handled, consume it
                event.consume();

            }

        } else if (pasteKeyCodeCompination.match(event)) {

            if (event.getSource() instanceof TableView) {

                // copy to clipboard
                Paste.paste((TableView<?>) event.getSource());

                // event is handled, consume it
                event.consume();

            }

        }
    }
}
