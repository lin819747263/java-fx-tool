package com.kehua.xml.model;

import javafx.beans.property.SimpleStringProperty;

public class Dict{

    private SimpleStringProperty key = new SimpleStringProperty();

    private SimpleStringProperty value = new SimpleStringProperty();

    public Dict() {
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
