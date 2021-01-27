package com.kehua.xml.control.textformatter;

import com.kehua.xml.utils.DataUtils;
import javafx.scene.control.TextFormatter;


public class NumberFormatter<T> {

    Class<T> type;

    public NumberFormatter(Class<T> type) {
        this.type = type;
    }

    public TextFormatter.Change getChange(TextFormatter.Change change){
        return change;
    }

    public TextFormatter<String> build(){
        return new TextFormatter<>(change -> {
            T cast = DataUtils.cast(change.getControlNewText(), type);
            if (!(cast instanceof Number)) {
                change.setText("");
            }
            change = getChange(change);
            return change;
        });
    }

}
