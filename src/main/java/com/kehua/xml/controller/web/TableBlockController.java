package com.kehua.xml.controller.web;

import com.kehua.xml.control.table.TableContextMenu;
import com.kehua.xml.controller.BaseController;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.Block;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;


@FXMLController
public class TableBlockController extends BaseController<Block> {

    @FXML
    public TableColumn<Block, String> funcode;
    
    @FXML
    public TableColumn<Block, String> addr;
    
    @FXML
    public TableColumn<Block, String> regCount;
    
    @FXML
    public TableColumn<Block, String> queryType;

    @FXML
    public TableColumn<Block, Boolean> isLocked;


    public void initialize() {
        super.initialize(TableDataManager.BLOCKS);
        baseTableColumnInit(funcode);
        baseTableColumnInit(addr);
        baseTableColumnInit(regCount);
        baseTableColumnInit(queryType);
        checkBoxColumnInit(isLocked);

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setContextMenu(TableContextMenu.getMenuBox(table, Block.class));
    }

    @Override
    protected void cellFactoryBuild() {
        super.cellFactoryBuild();
    }

    @Override
    protected void cellValueFactoryBuild() {
        super.cellValueFactoryBuild();
    }
}
