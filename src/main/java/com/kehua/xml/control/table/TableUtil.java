package com.kehua.xml.control.table;

import com.kehua.xml.utils.ReflectionUtil;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DataFormat;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class TableUtil {
    public static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    @Deprecated
    public static void setValue(TableView<?> table, int row, int col, Object value) {
        if (col == 0) {
            return;
        }
        TableColumn<?, ?> visibleLeafColumn = table.getVisibleLeafColumn(col);
        ObservableValue<?> observableValue = visibleLeafColumn.getCellObservableValue(row);
        if (observableValue instanceof StringProperty) {
            ((StringProperty) observableValue).set(value.toString());
        } else if (observableValue instanceof IntegerProperty) {
            try {
                ((IntegerProperty) observableValue).set(Integer.parseInt(value.toString()));
            } catch (Exception e) {
                //  e.printStackTrace();
            }
        } else if (observableValue instanceof BooleanProperty) {
            try {
                ((BooleanProperty) observableValue).set(Boolean.parseBoolean(value.toString()));
            } catch (Exception e) {
                //  e.printStackTrace();
            }
        } else {
            ((ObjectProperty) observableValue).set(value);
        }
    }

    public static <T> void setItemValue(TableView<T> table, int row, int col, Object value) {
        if (col == 0) {
            return;
        }

        String id = table.getVisibleLeafColumn(col).getId();

        setItemValue(table, row, id, value);
    }

    public static <T> void setItemValue(TableView<T> table, int row, String id, Object value) {
        T item = table.getItems().get(row);
        ReflectionUtil.setValue(item, id, value);
    }

    public static <T> void setValueForTableModel(T t, String field, String value) {
        SimpleStringProperty data = (SimpleStringProperty) ReflectionUtil.getValue(t, field);
        data.set(value);
    }

    public static <T> String getValueForTableModel(T t, String field) {
        SimpleStringProperty data = (SimpleStringProperty) ReflectionUtil.getValue(t, field);
        return data.get();
    }

}
