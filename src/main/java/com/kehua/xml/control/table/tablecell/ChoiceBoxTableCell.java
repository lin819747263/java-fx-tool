package com.kehua.xml.control.table.tablecell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

/**
 * HuangTengfei
 * 2020/12/14
 * description：
 */
public class ChoiceBoxTableCell<T, S> extends javafx.scene.control.cell.ChoiceBoxTableCell<T, S> {

    public ChoiceBoxTableCell() {
        this(null, FXCollections.observableArrayList());
    }

    public ChoiceBoxTableCell(String tipText) {
        this(tipText, FXCollections.observableArrayList());
    }

    public ChoiceBoxTableCell(String tipText, S... items) {
        this(tipText, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxTableCell(String tipText, StringConverter<S> converter, S... items) {
        this(tipText, converter, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxTableCell(String tipText, ObservableList<S> items) {
        this(tipText, null, items);
    }

    public ChoiceBoxTableCell(String tipText, StringConverter<S> converter, ObservableList<S> items) {
        super(converter, items);

        setOnDragDetected(event -> startFullDrag());
        setOnMouseDragEntered(event -> getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn()));

        if (tipText != null){
            Tooltip tooltip = new Tooltip();
            tooltip.setText(tipText);
            this.setTooltip(tooltip);
        }
    }
}
