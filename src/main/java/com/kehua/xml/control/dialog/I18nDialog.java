package com.kehua.xml.control.dialog;

import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.model.I18n;
import com.kehua.xml.utils.DictUtil;
import com.kehua.xml.utils.ReflectionUtil;
import com.kehua.xml.utils.UIFactory;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class I18nDialog {

    public static <T> void show(TableView<T> tableView) {
        try {
            buildDiaLog(tableView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static <T> void buildDiaLog(TableView<T> tableView) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane gridPane = UIFactory.createBaseGridPane();

        String name = getSelectCell(tableView);
        I18n i18n = getI18nByName(name);

        Properties properties = DictUtil.getDictByType("语言");
        I18n finalI18n = i18n;
        AtomicInteger index = new AtomicInteger();
        properties.forEach((k, v) -> {
            gridPane.add(new Label(k.toString()),0, index.get());

            TextField textField = new TextField();
            if(finalI18n != null){
                textField.appendText(getLocal(finalI18n, v.toString()));
            }
            if(finalI18n == null && "zh-CN".equals(v)){
                textField.appendText(name);
            }
            gridPane.add(textField,1, index.get());

            index.getAndIncrement();
        });

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        Optional result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ObservableList<Node> nodes = gridPane.getChildren();
            List<String> keys = new ArrayList<>();
            List<String> values = new ArrayList<>();

            int i = 0;
            for(Node node : nodes){
                if(i % 2 == 0){
                    Label label = (Label)node;
                    keys.add(label.getText() == null? "" : label.getText());
                }else {
                    TextField textField = (TextField)node;
                    values.add(textField.getText() == null? "" : textField.getText());
                }
                i++;
            }

            if(i18n == null){
                i18n = new I18n();
                CommonDataManager.I_18_NS.add(i18n);
            }

            for(int j = 0;j < keys.size();j++){
                setLocal(i18n, DictUtil.getDictByKey("语言", keys.get(j)),values.get(j));
            }
        }
    }

    private static I18n getI18nByName(String name){
        I18n i18n = null;
        for (I18n i18n1 : CommonDataManager.I_18_NS){
            if(i18n1.getRemark().equals(name)){
                i18n = i18n1;
            }
        }
        return i18n;
    }

    public static String getLocal(I18n remark, String local) {
        if ("zh-CN".equals(local)) {
            return remark.getRemark();
        }
        Object o = ReflectionUtil.getValue(remark, "remark" + upperCase(local));
        return o == null? "" : o.toString();
    }

    public static void setLocal(I18n i18n, String local, String value) {
        if ("zh-CN".equals(local)) {
            i18n.setRemark(value);
        }
        ReflectionUtil.setValue(i18n, "remark" + upperCase(local), value);
    }

    public static String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static <T> String getSelectCell(TableView<T> tableView) {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        if(selectedCells.size() == 0) return "";

        return getFirstValue(tableView, selectedCells);
    }

    public static String getFirstValue(TableView tableView, ObservableList<TablePosition> selectedCells){
        TablePosition tablePosition = selectedCells.get(0);
        TableColumn column = tableView.getVisibleLeafColumn(tablePosition.getColumn());
        Object value = column.getCellData(tablePosition.getRow());
        return value.toString();
    }
}
