package com.kehua.xml.controller;


import com.kehua.xml.control.dialog.UserDefined1Dialog;
import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.control.table.tablecell.EditingTextTableCell;
import com.kehua.xml.enumerate.EndianEnum;
import com.kehua.xml.enumerate.RegTypeEnum;
import com.kehua.xml.model.BasePoint;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


/**
 * HuangTengfei
 * 2020/12/3
 * description：
 */
public class BasePointController<T extends BasePoint> extends BaseController<T> {

    @FXML
    public TableColumn<T, String> funCode;
    @FXML
    public TableColumn<T, Integer> addr;
    @FXML
    public TableColumn<T, String> regType;
    @FXML
    public TableColumn<T, String> regParam;
    @FXML
    public TableColumn<T, String> endian;
    @FXML
    public TableColumn<T, String> ratio;
    @FXML
    public TableColumn<T, String> offset;
//    @FXML
//    public TableColumn<T, String> property;
    @FXML
    public TableColumn<T, String> remark;
    @FXML
    public TableColumn<T, String> userDefined1;
    @FXML
    public TableColumn<T, String> userDefined2;

    protected void initialize(ObservableList<T> items, Class<T> cls) {
        super.initialize(items);

        table.setContextMenu(TableContextMenu.getMenuBox(table, cls));
        table.setTableMenuButtonVisible(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);

        baseTableColumnInit();
    }

    @Override
    protected void setVisible() {
        super.setVisible();

        endian.setVisible(false);
        offset.setVisible(false);
        userDefined2.setVisible(false);
    }

    protected void baseTableColumnInit() {
        choiceBoxColumnInit(funCode, "", "1", "2", "3", "4", "5", "6", "16");
        choiceBoxColumnInit(regType, RegTypeEnum.class);
        numberColumnInit(addr, Integer.class, 9, 1, null);
        baseTableColumnInit(regParam, new BaseTableColumnInit<>("告警格式: 比特位起始位置,正常值.eg:0,0\n状态量格式: 比特位起始位置,比特位数.eg:0,16\n遥测字符串格式: 字节数,解析器类型.eg:30,2"));
        choiceBoxColumnInit(endian, EndianEnum.class);
        baseTableColumnInit(ratio);
        baseTableColumnInit(offset);
//        baseTableColumnInit(property);
//        property.setEditable(false);
        baseTableColumnInit(remark);
        baseTableColumnInit(userDefined1);
        baseTableColumnInit(userDefined2);
    }


}
