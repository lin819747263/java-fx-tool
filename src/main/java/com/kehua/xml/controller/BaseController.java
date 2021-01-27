package com.kehua.xml.controller;


import com.kehua.xml.control.table.*;
import com.kehua.xml.control.table.callback.NumCellFactory;
import com.kehua.xml.control.table.TableHandler;
import com.kehua.xml.control.table.tablecell.CheckBoxTableCell;
import com.kehua.xml.control.table.tablecell.ChoiceBoxTableCell;
import com.kehua.xml.control.table.tablecell.EditingNumberTableCell;
import com.kehua.xml.control.table.tablecell.EditingTextTableCell;
import com.kehua.xml.utils.DictUtil;
import com.kehua.xml.utils.EnumUtils;
import com.kehua.xml.utils.ReflectionUtil;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * HuangTengfei
 * 2020/12/3
 * description：
 */
public abstract class BaseController<T> {

    @FXML
    public TableView<T> table;
    @FXML
    public TableColumn<T, String> num;

    protected void initialize(ObservableList<T> items) {
        TableHandler.initialize(table);
        TablePolicy.initialize(table);

        initializeBase(items);
    }

    protected void initializeBase(ObservableList<T> items) {
        table.setItems(items);

        setVisible();
        cellFactoryBuild();
        cellValueFactoryBuild();
    }

    protected void setVisible() {

    }

    protected void cellFactoryBuild() {
        num.setCellFactory(new NumCellFactory<>(table));
    }

    protected void cellValueFactoryBuild() {

    }

    /**
     * @param <T>
     * @param <S> 默认为String类型
     */
    protected static class BaseTableColumnInit<T, S> {
        private Class<S> sClass;
        private String tipText;

        public BaseTableColumnInit() {
            this((Class<S>) String.class);
        }

        public BaseTableColumnInit(Class<S> sClass) {
            this(null, sClass);
        }

        public BaseTableColumnInit(String tipText) {
            this(tipText, (Class<S>) String.class);
        }

        public BaseTableColumnInit(String tipText, Class<S> sClass) {
            this.tipText = tipText;
            this.sClass = sClass;
        }

        public Callback<TableColumn<T, S>, TableCell<T, S>> getCellFactory() {
            return param -> new EditingTextTableCell<>(sClass, tipText);
        }

        public Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>> getCellValueFactory(TableColumn<T, S> column) {
            return new PropertyValueFactory<>(column.getId());
        }

        public EventHandler<TableColumn.CellEditEvent<T, S>> getOnEditCommit(TableColumn<T, S> column) {
            return event -> ReflectionUtil.setValue(event.getRowValue(), column.getId(), event.getNewValue());
        }
    }

    protected <S> void baseTableColumnInit(TableColumn<T, S> column) {
        baseTableColumnInit(column, new BaseTableColumnInit<>());
    }

    protected <S> void baseTableColumnInit(TableColumn<T, S> column, BaseTableColumnInit<T, S> baseTableColumnInit) {
        column.setCellFactory(baseTableColumnInit.getCellFactory());
        column.setCellValueFactory(baseTableColumnInit.getCellValueFactory(column));
        column.setOnEditCommit(baseTableColumnInit.getOnEditCommit(column));
    }

    protected <S extends Enum<S>> void choiceBoxColumnInit(TableColumn<T, String> column, Class<S> choices) {
        choiceBoxColumnInit(null, column, choices);
    }

    protected <S extends Enum<S>> void choiceBoxColumnInit(String tipText, TableColumn<T, String> column, Class<S> choices) {
        baseTableColumnInit(column, new BaseTableColumnInit<T, String>() {
            @Override
            public Callback<TableColumn<T, String>, TableCell<T, String>> getCellFactory() {
                return list -> new ChoiceBoxTableCell<>(tipText, new StringConverter<String>() {
                    @Override
                    public String toString(String value) {
                        return EnumUtils.getTitle(value, choices);
                    }

                    @Override
                    public String fromString(String title) {
                        return (String) EnumUtils.getValue(title, choices);
                    }
                }, EnumUtils.getValues(choices).toArray(new String[0]));
            }
        });
    }

    protected <S extends Enum<S>> void choiceBoxColumnInit(TableColumn<T, String> column, String... choices) {
        choiceBoxColumnInit(null, column, choices);
    }

    protected <S extends Enum<S>> void choiceBoxColumnInit(String tipText, TableColumn<T, String> column, String... choices) {
        baseTableColumnInit(column, new BaseTableColumnInit<T, String>() {
            @Override
            public Callback<TableColumn<T, String>, TableCell<T, String>> getCellFactory() {
                return list -> new ChoiceBoxTableCell<>(tipText, choices);
            }
        });
    }

    protected void choiceBoxColumnInit(TableColumn<T, String> column, String dictName) {
        choiceBoxColumnInit(null, column, dictName);
    }

    protected void choiceBoxColumnInit(String tipText, TableColumn<T, String> column, String dictName) {
        baseTableColumnInit(column, new BaseTableColumnInit<T, String>() {
            @Override
            public Callback<TableColumn<T, String>, TableCell<T, String>> getCellFactory() {
                return list -> new ChoiceBoxTableCell<>(tipText, new StringConverter<String>() {
                    @Override
                    public String toString(String object) {
                        if (StringUtils.isNotBlank(object)) {
                            return DictUtil.getKeyByValue(dictName, object);
                        }
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        if (StringUtils.isNotBlank(string)) {
                            return DictUtil.getDictByKey(dictName, string);
                        }
                        return string;
                    }
                }, FXCollections.observableArrayList(DictUtil.getDictValuesByType(dictName)));
            }
        });
    }

    protected <S extends Number> void numberColumnInit(TableColumn<T, S> column, Class<S> type) {
        numberColumnInit(column, type, null);
    }

    protected <S extends Number> void numberColumnInit(TableColumn<T, S> column, Class<S> type, Integer limit) {
        numberColumnInit(column, type, limit, null, null);
    }

    protected <S extends Number> void numberColumnInit(TableColumn<T, S> column, Class<S> type, Integer limit, S min, S max) {
        numberColumnInit(null, column, type, limit, min, max);
    }

    protected <S extends Number> void numberColumnInit(String tipText, TableColumn<T, S> column, Class<S> type, Integer limit, S min, S max) {
        baseTableColumnInit(column, new BaseTableColumnInit<T, S>(type) {
            @Override
            public Callback<TableColumn<T, S>, TableCell<T, S>> getCellFactory() {
                return param -> new EditingNumberTableCell<>(type, tipText, limit, min, max);
            }
        });
    }

    protected void checkBoxColumnInit(TableColumn<T, Boolean> column) {
        checkBoxColumnInit(null, column);
    }

    protected void checkBoxColumnInit(String tipText, TableColumn<T, Boolean> column) {
        baseTableColumnInit(column, new BaseTableColumnInit<T, Boolean>(Boolean.class) {
            @Override
            public Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> getCellFactory() {
                return list -> new CheckBoxTableCell<>(tipText);
            }
        });
    }


}
