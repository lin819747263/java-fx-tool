package com.kehua.xml.control.dialog;

import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.control.table.tool.Paste;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * HuangTengfei
 * 2020/12/15
 * description：
 */
public class AutoFillDialog<T> {

    private static class Data {
        private static final Data data = new Data();
        private String attr;
        private Integer begin;
        private Integer num;
        private Integer step;
        private Integer span;

        public Data() {
        }

        public Data(String attr, Integer begin, Integer num, Integer step, Integer span) {
            data.attr = attr;
            data.begin = begin;
            data.num = num;
            data.step = step;
            data.span = span;
        }

        public static void clear(){
            data.attr = null;
            data.begin = null;
            data.num = null;
            data.step = null;
            data.span = null;
        }

        public static Data get(){
            return data;
        }

        public static String value(){
            StringBuilder builder = new StringBuilder();
            int begin = data.begin;
            int end = data.begin + data.num;
            int value = begin;
            int spanIndex = 0;
            while (begin < end){
                builder.append("\n");
                builder.append(data.attr.replace("$$", Integer.toString(value)));
                if (++spanIndex >= data.span){
                    value += data.step;
                    spanIndex = 0;
                }
                begin++;
            }
            return builder.toString().substring(1);
        }
    }

    @SuppressWarnings("rawtypes")
    public static <T> Dialog build(TableView<T> tableView) {
        Data.clear();
        Dialog dialog = getDialog();
        Optional optional = dialog.showAndWait();
        if (optional.isPresent()){
            String values = Data.value();
            Paste.paste(tableView, values);
        }
        return dialog;
    }

    private static Dialog<Data> getDialog() {
        Dialog<Data> dialog = new Dialog<>();
        dialog.setTitle("自定义填充");
        dialog.setHeaderText(null);
        DialogPane dialogPane = dialog.getDialogPane();
        VBox vBox = new VBox();
        dialogPane.setContent(vBox);
        ButtonType close = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().add(close);

        TextField attr = getTextField("$$");
        TextField begin = getIntField(1, 0);
        TextField num = getIntField(1, 0);
        TextField step = getIntField(1, 0);
        TextField span = getIntField(0, 0);
        HBox attrHBox = getHBox("地址模板", attr);
        HBox beginHBox = getHBox("起始值", begin);
        HBox numHBox = getHBox("填充数量", num);
        HBox stepHBox = getHBox("步长", step);
        HBox spanHBox = getHBox("跨行数", span);

        Button commit = (Button) dialog.getDialogPane().lookupButton(close);
        commit.addEventFilter(ActionEvent.ACTION, event -> {
            String attrText = attr.getText();
            if (!attrText.contains("$$")){
                AlertFactory.createAlert("地址模板格式错误，必须包含替换模板$$");
                event.consume();
                return;
            }

            String beginText = begin.getText();
            if (!beginText.matches("^[\\d]+$")){
                AlertFactory.createAlert("起始值必须为大于等于1的数字");
                event.consume();
                return;
            }

            String stepText = step.getText();
            if (!stepText.matches("^[\\d]+$")){
                AlertFactory.createAlert("步长必须为大于等于1的数字");
                event.consume();
                return;
            }

            String spanText = span.getText();
            if (!spanText.matches("^[\\d]+$")){
                AlertFactory.createAlert("跨行数必须为大于等于0的数字");
                event.consume();
                return;
            }

            new Data(attr.getText(), Integer.parseInt(begin.getText()), Integer.parseInt(num.getText()), Integer.parseInt(step.getText()), Integer.parseInt(span.getText()));
        });

        vBox.getChildren().add(attrHBox);
        vBox.getChildren().add(beginHBox);
        vBox.getChildren().add(numHBox);
        vBox.getChildren().add(stepHBox);
        vBox.getChildren().add(spanHBox);

        return dialog;

    }

    private static HBox getHBox(String labelStr, TextField textField) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 1, 5, 1));
        hBox.setAlignment(Pos.CENTER);
        Label label = new Label(labelStr);
        label.setPrefWidth(60);
        hBox.getChildren().add(label);
        hBox.getChildren().add(textField);
        return hBox;
    }

    private static TextField getTextField(String defaultValue) {
        TextField textField = new TextField(defaultValue);
        textField.setAlignment(Pos.BASELINE_CENTER);
        return textField;
    }

    private static TextField getIntField(Integer defaultValue, int min) {
        TextField textField = getTextField(defaultValue.toString());
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getControlNewText();
            try {
                int newText = Integer.parseInt(text);
                if (newText < min) {
                    throw new Exception();
                }
            } catch (Exception ignored) {
                if (StringUtils.isEmpty(text)) {
                    return change;
                } else {
                    return null;
                }
            }
            return change;
        }));
        return textField;
    }

}
