package com.kehua.xml.controller.web;

import com.kehua.xml.analysis.ParserFactory;
import com.kehua.xml.analysis.modbus.ModbusMasterFactory;
import com.kehua.xml.control.table.callback.SortCallBack;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.DebugCollect;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.ModbusUtil;
import com.kehua.xml.view.web.DebugCollectSettingView;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class DebugCollectorController extends BaseController<DebugCollect> {

    private static final Logger logger = LoggerFactory.getLogger(DebugCollectSettingController.class);

    private Scene scene;

    @Autowired
    private DebugCollectSettingView debugCollectSettingView;

    @Autowired
    private DebugCollectSettingController setting;

    @FXML
    public BorderPane borderPane;
    @FXML
    public TableColumn<DebugCollect, String> funCode;
    @FXML
    public TableColumn<DebugCollect, Integer> addr;
    @FXML
    public TableColumn<DebugCollect, String> remark;
    @FXML
    public TableColumn<DebugCollect, String> regType;
    @FXML
    public TableColumn<DebugCollect, String> regParam;
    @FXML
    public TableColumn<DebugCollect, String> ratio;
    @FXML
    public TableColumn<DebugCollect, String> offset;
    @FXML
    public TableColumn<DebugCollect, String> collectValue;

    public void initTable() {
        funCode.setCellValueFactory(new PropertyValueFactory<>("funCode"));
        addr.setCellValueFactory(new PropertyValueFactory<>("addr"));
        remark.setCellValueFactory(new PropertyValueFactory<>("remark"));
        regType.setCellValueFactory(new PropertyValueFactory<>("regType"));
        regParam.setCellValueFactory(new PropertyValueFactory<>("regParam"));
        ratio.setCellValueFactory(new PropertyValueFactory<>("ratio"));
        offset.setCellValueFactory(new PropertyValueFactory<>("offset"));
        collectValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    public void initialize() {
        initializeBase(TableDataManager.DEBUG_COLLECT);
        table.setSortPolicy(new SortCallBack<>(table));
        initTable();
    }

    public void collect(ActionEvent actionEvent) throws ModbusInitException {
        DebugCollect collect;
        ModbusMaster master = ModbusMasterFactory.createMaster();
        if (master == null){
            AlertFactory.createAlert("请先设置正确的连接设置");
            return;
        }
        for (int i = 0; i < TableDataManager.DEBUG_COLLECT.size(); i++) {
            collect = table.getItems().get(i);
            if (checkTableData(collect)) {
                return;
            }
            int len = 1;
            if ("9".equals(collect.getRegType())) {
                len = Integer.parseInt(collect.getRegParam().split(",")[0]) / 2;
            }else if ("5".equals(collect.getRegType()) || "6".equals(collect.getRegType())) {
                len = 2;
            }
            try {
                byte[] res = ModbusUtil.readRegisters(Integer.parseInt(collect.getFunCode()), master, setting.getAddr(), Integer.parseInt(collect.getAddr()), len);
                if (res == null) {
                    collect.setValue("--");
                }else {
                    collect.setValue(ParserFactory.getInstance().getParser(collect.getRegType(), collect.getRegParam(), Double.parseDouble(collect.getRatio()), collect.getFunCode()).parser(res));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                AlertFactory.createAlert("采集失败,请检查连接");
                return;
            }
        } AlertFactory.createAlert("采集成功！");
    }

    public void connectionSetting(ActionEvent actionEvent) {
        if (scene == null || scene.getRoot() == null) {
            scene = new Scene(debugCollectSettingView.getView());
        }
        //  初始化设置
        debugCollectSettingView.init();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    public boolean checkTableData(DebugCollect collect) {
        if (StringUtils.isBlank(collect.getFunCode())) {
            AlertFactory.createAlert("功能码不能为空！");
            return true;
        }
        if (StringUtils.isBlank(collect.getAddr())) {
            AlertFactory.createAlert("点位地址不能为空！");
            return true;
        }
        if (StringUtils.isBlank(collect.getRegType())) {
            AlertFactory.createAlert("寄存器类型不能为空！");
            return true;
        }
        return false;
    }
}
