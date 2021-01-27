package com.kehua.xml.control.table.tablecell;

import com.kehua.xml.control.table.tool.Paste;
import com.kehua.xml.utils.DataUtils;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * HuangTengfei
 * 2020/12/24
 * description：
 */
public class EditingTextTableCell<T, E> extends TableCell<T, E> {

    @Getter
    @AllArgsConstructor
    protected static class Change<E> {
        E oldValue;
        E newValue;

        public void setNewValue(E newValue) {
            this.newValue = newValue;
        }
    }

    private final Class<E> type;
    private final Integer limit;
    protected TextField textField = new TextField();

    public EditingTextTableCell(Class<E> type) {
        this(type, null, null);
    }

    public EditingTextTableCell(Class<E> type, Integer limit) {
        this(type, null, limit);
    }

    public EditingTextTableCell(Class<E> type, String tipText) {
        this(type, tipText, null);
    }

    public EditingTextTableCell(Class<E> type, String tipText, Integer limit) {
        this.limit = limit;
        this.type = type;

        setOnDragDetected(event -> startFullDrag());
        setOnMouseDragEntered(event -> getTableView().getSelectionModel().select(getIndex(), getTableColumn()));

        if (tipText != null) {
            Tooltip tooltip = new Tooltip();
            tooltip.setFont(new Font(12));
            tooltip.setText(tipText);
            setTooltip(tooltip);
        }

        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                textField.setText(getText());
                cancelEdit();
            }
            if (event.isControlDown() && event.getCode().equals(KeyCode.V)) {
                String clipboard = Paste.getClipboard();
                String text = textField.getText();
                IndexRange selection = textField.getSelection();
                E oldValue = DataUtils.cast(text, type), newValue;
                if (text == null) {
                    newValue = DataUtils.cast(clipboard, type);
                }else {
                    String newValueStr = text.substring(0, selection.getStart()) + clipboard + text.substring(selection.getEnd());
                    newValue = DataUtils.cast(newValueStr, type);
                    if (DataUtils.stringNotEqual(newValue, newValueStr)) {
                        event.consume();
                        return;
                    }
                }
                Change<E> change = new Change<>(oldValue, newValue);
                if (!defaultCheck(change)) {
                    event.consume();
                }
            }
        });
        textField.setOnAction(event -> commitEdit());
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                commitEdit();
            }
        });
        textField.setTextFormatter(new TextFormatter<E>(change -> {
            String controlText = change.getControlText();
            String controlNewText = change.getControlNewText();
            E data = DataUtils.cast(controlNewText, type);
            E last = DataUtils.cast(controlText, type);
            Change<E> c = new Change<>(last, data);
            if (DataUtils.stringNotEqual(data, controlNewText) || !defaultCheck(c)) {
                change.setText("");
            }
            return change;
        }));

        init();
    }

    protected void init() {
    }

    protected boolean check(Change<E> change) {
        return true;
    }

    protected boolean checkLimit(E data, Integer limit) {
        return limit == null || data == null || data.toString().length() <= limit;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <R extends Number> boolean checkMinAndMax(R data, R min, R max) {
        return ((Comparable) data).compareTo(max) <= 0 && ((Comparable) data).compareTo(min) >= 0;
    }

    @SuppressWarnings({"unchecked"})
    private boolean defaultCheck(E data) {
        if (!checkLimit(data, limit)) {
            return false;
        }
        try {
            if (data instanceof Number) {
                E min = (E) type.getField("MIN_VALUE").get(null);
                E max = (E) type.getField("MAX_VALUE").get(null);
                if (!checkMinAndMax((Number) data, (Number) min, (Number) max)) {
                    return false;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean defaultCheck(Change<E> change) {
        E data = change.newValue;
        return defaultCheck(data) && check(change) && Objects.equals(change.newValue, data);
    }

    @Override
    public void startEdit() {
        if (isEditable()) {
            setGraphic(textField);
            textField.setText(getText());
            textField.setPrefWidth(getWidth() - getGraphicTextGap() * 2);
            textField.requestFocus();
            super.startEdit();
        }
    }

    @Override
    public void cancelEdit() {
        setGraphic(null);
        super.cancelEdit();
    }

    public void commitEdit() {
        commitEdit(DataUtils.cast(textField.getText(), type));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void commitEdit(E newValue) {
        setGraphic(null);
        if (isEditing()) {
            super.commitEdit(newValue);
        }else {
            TableView<T> table = getTableView();
            if (table != null) {
                Event.fireEvent(getTableColumn(), new TableColumn.CellEditEvent(table, new TablePosition(table, getIndex(), getTableColumn()), TableColumn.editCommitEvent(), newValue));
            }
            updateItem(newValue, false);
            if (table != null) {
                table.edit(-1, null);
            }
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void updateItem(E item, boolean empty) {
        if (item instanceof String) {
            item = (E) ((String) item).trim();
        }

        E oldValue = DataUtils.cast(getText(), type);
        item = DataUtils.cast(item, type);
        Change<E> change = new Change<>(oldValue, item);
        if (defaultCheck(change)) {
            item = change.newValue;
        }else {
            item = oldValue;
        }

        if (item == null) {
            setText(null);
        }else {
            setText(item.toString());
        }
        super.updateItem(item, empty);
    }
}
