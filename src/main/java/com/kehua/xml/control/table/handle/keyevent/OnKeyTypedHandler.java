package com.kehua.xml.control.table.handle.keyevent;

import com.kehua.xml.control.table.TableUtil;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * HuangTengfei
 * 2020/12/2
 * description：
 */
public class OnKeyTypedHandler implements EventHandler<KeyEvent> {

    public static List<String> IGNORE_TABLE_COLUMN_ID = new ArrayList<String>() {{
        add("property");
        add("regType");
        add("collectRegType");
        add("funCode");
        add("collectFuncode");
        add("noCollect");
        add("noRealTime");
        add("oneTime");
    }};

    @Override
    public void handle(KeyEvent event) {
        String character = event.getCharacter();
        if (event.getSource() instanceof TableView && StringUtils.isNotEmpty(character)) {
            TableView<?> tableView = (TableView<?>) event.getSource();
            tableView.getSelectionModel().getSelectedCells().forEach(tablePosition -> {
                int row = tablePosition.getRow();
                int column = tablePosition.getColumn();
                TableColumn<?, ?> tableColumn = tableView.getVisibleLeafColumn(column);

                if (!tableColumn.isEditable() || IGNORE_TABLE_COLUMN_ID.contains(tableColumn.getId())) {
                    return;
                }
                TableUtil.setItemValue(tableView, row, column, inputValue(tableColumn.getCellData(row), character));
            });
        }
    }

    public static String inputValue(Object data, String character){
        if (data == null){
            data = "";
        }
        String value;
        if (character.equals("\b")){
            character = "";
            value = backspaceValue(data.toString());
        }else if (character.equals(String.valueOf('\u007F'))){
            character = "";
            value = "";
        }else {
            value = data.toString();
        }
        return value + character.trim();
    }

    private static String backspaceValue(String value) {
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

}
