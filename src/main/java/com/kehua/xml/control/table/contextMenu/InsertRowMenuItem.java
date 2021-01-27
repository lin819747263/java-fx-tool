package com.kehua.xml.control.table.contextMenu;

import com.kehua.xml.control.table.tool.InsertRow;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class InsertRowMenuItem<T> extends MenuItem<T> {

    public static class Data<T> {
        public T getData() throws Exception {
            return null;
        }
    }

    public InsertRowMenuItem(TableView<T> tableView, Class<T> dataClass) {
        super(tableView, dataClass, "插入行");
        this.setOnAction(event -> addRow(tableView, dataClass));
    }

    @SuppressWarnings({"rawtypes"})
    public static <T> int addRow(TableView<T> tableView, Class<T> dataClass) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("插入行");
        dialog.setContentText("行数");
        dialog.setHeaderText(null);

        //  传统的获取输入值的方法
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            int num;
            try {
                num = Integer.parseInt(result.get().toString());
                if (num < 1 || num > 10000) {
                    throw new Exception();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("插入行");
                alert.setContentText("只能输入1-10000之间的数字");
                alert.setHeaderText(null);
                alert.showAndWait();
                return addRow(tableView, dataClass);
            }
            List<T> tList = new ArrayList<>();
            for (int i = 0; i< num; i++){
                try {
                    tList.add(dataClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return InsertRow.insertRow(tableView, tList);
        } else {
            return 0;
        }
    }

}
