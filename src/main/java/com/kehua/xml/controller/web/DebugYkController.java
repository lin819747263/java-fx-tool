package com.kehua.xml.controller.web;


import com.kehua.xml.analysis.modbus.ModbusMasterFactory;
import com.kehua.xml.control.table.tablecell.EditingTextTableCell;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.DebugYk;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.DataUtils;
import com.kehua.xml.utils.ModbusUtil;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@FXMLController
public class DebugYkController extends BaseController<DebugYk> {

    @FXML
    public BorderPane borderPane;
    @FXML
    public TableColumn<DebugYk, String> funCode;
    @FXML
    public TableColumn<DebugYk, String> addr;
    @FXML
    public TableColumn<DebugYk, String> remark;
    @FXML
    public TableColumn<DebugYk, String> regType;
    @FXML
    public TableColumn<DebugYk, String> regParam;
    @FXML
    public TableColumn<DebugYk, String> ratio;
    @FXML
    public TableColumn<DebugYk, String> rangeData;
    @FXML
    public TableColumn<DebugYk, String> sendValue;
    @FXML
    public TableColumn<DebugYk, String> action;


    public void initialize() {
        initializeBase(TableDataManager.DEBUG_COLLECT_YK);
        initTable();
    }

    private void initTable() {
        baseTableColumnInit(sendValue, new BaseTableColumnInit<DebugYk, String>() {
            @Override
            public Callback<TableColumn<DebugYk, String>, TableCell<DebugYk, String>> getCellFactory() {
                return param -> new EditingTextTableCell<DebugYk, String>(String.class) {
                    @Override
                    protected boolean check(Change<String> change) {
                        if (super.check(change) && getIndex() >= 0) {
                            ObservableList<DebugYk> items = getTableView().getItems();
                            if (items.size() <= getIndex() || getIndex() < 0) {
                                return false;
                            }
                            DebugYk debugYk = items.get(getIndex());
                            if (!debugYk.isIsBlock()) {
                                String funCode = debugYk.getFunCode();
                                if (funCode.equals("5")) {
                                    String newValueStr = change.getNewValue();
                                    Integer newValue = DataUtils.cast(newValueStr, Integer.class);
                                    if (DataUtils.stringNotEqual(newValue, newValueStr)) {
                                        return false;
                                    }
                                    if (newValue != null) {
                                        return newValue <= 1 && newValue >= 0;
                                    }
                                }else if (funCode.equals("6")) {
                                }
                            }else if (debugYk.isIsBlock()) {
                                setEditable(false);
                            }
                            return true;
                        }
                        return false;
                    }
                };
            }
        });

        action.setCellFactory((col) -> {
            TableCell<DebugYk, String> cell = new TableCell<DebugYk, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button = new Button("调试");
                    button.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                    button.setOnMouseClicked((MouseEvent col) -> {
                        DebugYk debugYk = (DebugYk) this.getTableRow().getItem();
                        if (checkYkTableDate(debugYk)) {
                            return;
                        }
                        ModbusMaster master;
                        try {
                            master = ModbusMasterFactory.createMaster();
                            if (master == null) {
                                AlertFactory.createAlert("请先设置正确的连接设置");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        if (debugYk.isIsBlock()) {
                            Dialog<Pair<String, String>> dialog = createBlockPane(debugYk, master);
                            dialog.showAndWait();
                        }else {
                            try {
                                writeNotBlock(debugYk, master);
                                AlertFactory.createAlert("下发成功!");
                            } catch (Exception e) {
                                AlertFactory.createAlert("下发失败，请检查连接");
                            }
                        }
                    });
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    }else {
                        this.setGraphic(button);
                    }
                }
            };
            return cell;
        });

        funCode.setCellValueFactory(new PropertyValueFactory<>("funCode"));
        addr.setCellValueFactory(new PropertyValueFactory<>("addr"));
        remark.setCellValueFactory(new PropertyValueFactory<>("remark"));
        regType.setCellValueFactory(new PropertyValueFactory<>("regType"));
        regParam.setCellValueFactory(new PropertyValueFactory<>("regParam"));
        ratio.setCellValueFactory(new PropertyValueFactory<>("ratio"));
        rangeData.setCellValueFactory(new PropertyValueFactory<>("rangeData"));
    }

    /**
     * //写入单个线圈,单个寄存器
     * @param debugYk
     * @param master
     * @throws ModbusTransportException
     */
    public void writeNotBlock(DebugYk debugYk, ModbusMaster master) throws ModbusTransportException {
        if (Integer.parseInt(debugYk.getFunCode()) == 5) {
            ModbusUtil.writeCoil(master, 1, Integer.parseInt(debugYk.getAddr()), debugYk.getSendValue().equals("1"));
        }
        if (Integer.parseInt(debugYk.getFunCode()) == 6) {
            ModbusUtil.writeRegister(master, 1, Integer.parseInt(debugYk.getAddr()), Integer.parseInt(debugYk.getSendValue()));
        }
    }

    public Dialog<Pair<String, String>> createBlockPane(DebugYk debugYk, ModbusMaster master) {
        VBox box = new VBox();

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add("/css/debug.css");
        dialogPane.getStyleClass().add("debug-debug");
        dialogPane.setContent(box);

        ButtonType loginButtonType = new ButtonType("下发", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, loginButtonType);
        Button commit = (Button) dialogPane.lookupButton(loginButtonType);

        List<TextField> textFields = new ArrayList<>();
        String sendValue = debugYk.getSendValue();
        String[] split = null;
        if (sendValue != null) {
            split = debugYk.getSendValue().split(", ");
        }
        int len = 0;
        for (DebugYk yk : debugYk.getPoints()){
            len = Math.max(yk.getRemark().length() + 1, len);
        }
        for (int i = 0; i < debugYk.getPoints().size(); i++) {
            DebugYk yk = debugYk.getPoints().get(i);
            TextField textField = new TextField();
            textField.setPromptText(yk.getRangeData());
            HBox hBox = new HBox();
            Label label = new Label(yk.getRemark());
            label.setPrefWidth(len * label.getFont().getSize());
            hBox.getChildren().addAll(label, textField);
            textFields.add(textField);
            box.getChildren().add(hBox);
            try {
                textField.setText(split != null ? split[i] : null);
            }catch (Exception ignored){}
        }

        commit.addEventFilter(ActionEvent.ACTION, event -> {
            short[] shorts;
            try {
                shorts = getValues(box);
            } catch (Exception e) {
                AlertFactory.createAlert(e.getMessage());
                event.consume();
                return;
            }
            //写入03模拟量数据
            if (Integer.parseInt(debugYk.getFunCode()) == 16) {
                try {
                    ModbusUtil.writeRegisters(master, 1, Integer.parseInt(debugYk.getAddr()), shorts);
                    AlertFactory.createAlert("下发成功!");
                    List<String> values = new ArrayList<>();
                    for (TextField textField : textFields) {
                        values.add(textField.getText());
                    }
                    debugYk.setSendValue(String.join(", ", values.toArray(new String[]{})));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertFactory.createAlert("下发失败，请检查连接");
                }
            }
        });



        return dialog;
    }

    private short[] getValues(VBox box) {
        //获取textField数据,以数组存储
        List<Short> data = new ArrayList<>();
        for (Node node : box.getChildren()) {
            ObservableList<Node> children = ((HBox) node).getChildren();
            Label label = (Label) children.get(0);
            TextField textField = (TextField) children.get(1);
            String d = textField.getText();
            if (StringUtils.isEmpty(d)){
                throw new RuntimeException(label.getText() + "不能为空");
            }
            try {
                short dShort = Short.parseShort(d);
                String promptText = textField.getPromptText();
                if (StringUtils.isEmpty(promptText)){
                    continue;
                }
                String[] range = promptText.split("-");
                try {
                    short start = Short.parseShort(range[0]);
                    short end = Short.parseShort(range[1]);
                    if (dShort < start || dShort > end){
                        throw new RuntimeException(label.getText() + "的取值范围为" + promptText);
                    }
                }catch (NumberFormatException | IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                data.add(dShort);
            }catch (NumberFormatException e){
                throw new RuntimeException(label.getText() + "必须为数字");
            }
        }

        return ArrayUtils.toPrimitive(data.toArray(new Short[]{}));
    }

    /**
     * 调试前校验遥控表格数据
     * @param debugYk
     * @return
     */
    public boolean checkYkTableDate(DebugYk debugYk) {
        if (StringUtils.isBlank(debugYk.getFunCode())) {
            AlertFactory.createAlert("功能码不能为空！");
            return true;
        }
        if (StringUtils.isBlank(debugYk.getAddr())) {
            AlertFactory.createAlert("点位地址不能为空！");
            return true;
        }
        if (StringUtils.isBlank(debugYk.getRegType())) {
            AlertFactory.createAlert("寄存器类型不能为空！");
            return true;
        }
        if (debugYk.getFunCode().equals("16") == false && StringUtils.isBlank(debugYk.getSendValue())) {
            AlertFactory.createAlert("下发值不能为空！");
            return true;
        }

        return false;
    }

}
