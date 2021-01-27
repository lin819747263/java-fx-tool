package com.kehua.xml.control.table.tablecell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * HuangTengfei
 * 2020/12/14
 * description：
 */
public class CheckBoxTableCell<T, S> extends javafx.scene.control.cell.CheckBoxTableCell<T, S> {

    public CheckBoxTableCell() {
        this(null);
    }

    public CheckBoxTableCell(String tipText) {
        this(tipText, null, null);
    }

    public CheckBoxTableCell(String tipText, Callback<Integer, ObservableValue<Boolean>> getSelectedProperty) {
        this(tipText, getSelectedProperty, null);
    }

    public CheckBoxTableCell(String tipText, Callback<Integer, ObservableValue<Boolean>> getSelectedProperty, StringConverter<S> converter) {
        super(getSelectedProperty, converter);

        setOnDragDetected(event -> startFullDrag());
        setOnMouseDragEntered(event -> getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn()));

        if (tipText != null){
            Tooltip tooltip = new Tooltip();
            tooltip.setText(tipText);
            setTooltip(tooltip);
        }
    }
}
