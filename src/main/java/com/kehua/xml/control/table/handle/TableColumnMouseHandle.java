package com.kehua.xml.control.table.handle;

import com.kehua.xml.enumerate.RegTypeEnum;
import com.kehua.xml.utils.EnumUtils;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * HuangTengfei
 * 2020/12/26
 * description：
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TableColumnMouseHandle {

    static final List<String> IGNORE_TABLE_COLUMN_ID = new ArrayList<String>() {{
        add("num");
    }};
    static final Map<String, Class<?>> DATA_IS_ENUM = new HashMap<String, Class<?>>() {{
        put("regType", RegTypeEnum.class);
        put("collectRegType", RegTypeEnum.class);
    }};

    private static ObservableList items;
    private static TableView table;
    private static TableColumn column;
    private static final TextField textField = new TextField();
    private static final Label label = new Label();
    public static final Tooltip tooltip = new Tooltip();

    static {
        Button button = new Button("×");
        button.setStyle("-fx-cursor: hand");
        button.setOnAction(e -> cancel());

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(label, textField, button);
        box.setOnMouseEntered(event -> {
            textField.setDisable(false);
            textField.requestFocus();
        });
        box.setOnMouseExited(event -> textField.setDisable(true));

        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                cancel();
            }
        });
        textField.setOnKeyReleased(event -> commit());

        tooltip.setFont(new Font(12));
        tooltip.setGraphic(box);
        tooltip.setOnHidden(event -> reset());
    }

    public static <T> void initialize(TableView<T> table) {
        table.skinProperty().addListener((observable, oldValue, newValue) -> {
            TableHeaderRow headerRow;
            try {
                headerRow = ((TableViewSkinBase) newValue).getTableHeaderRow();
            } catch (Exception ignored) {
                return;
            }

            headerRow.setOnContextMenuRequested(Event::consume);
            headerRow.setOnMouseClicked(event -> {
                if (table.getItems().size() == 0 || !event.getButton().equals(MouseButton.SECONDARY)) {
                    event.consume();
                    return;
                }

                items = table.getItems();
                TableColumnMouseHandle.table = table;

                Node target = (Node) event.getTarget();
                double size;
                if (target instanceof Text) {
                    target = target.getParent();
                }
                if (target instanceof Label) {
                    column = (TableColumn) ((TableColumnHeader) target.getParent()).getTableColumn();
                }else {
                    event.consume();
                    return;
                }

                if (IGNORE_TABLE_COLUMN_ID.contains(column.getId())) {
                    event.consume();
                    return;
                }

                size = ((Label) target).getFont().getSize();
                show(target, size);
            });
        });
    }

    private static void show(Node target, double size) {
        label.setText(column.getText() + ": ");
        textField.clear();
        Bounds layoutBounds = target.getLayoutBounds();
        Point2D point2D = target.localToScreen(layoutBounds.getMinX(), layoutBounds.getMinY());
        tooltip.show(target, point2D.getX() - 4, point2D.getY() - size * 3.5);
        textField.setDisable(true);
    }

    private static void commit() {
        String text = textField.getText().trim();
        ObservableList newItems = FXCollections.observableArrayList();
        Class<?> cls = DATA_IS_ENUM.get(column.getId());
        for (Object item : items) {
            Object data = column.getCellData(item);
            if (data == null) {
                return;
            }

            if (cls != null && Enum.class.isAssignableFrom(cls)) {
                data = EnumUtils.getTitle(data, (Class<Enum>) cls);
            }
            if (data.toString().contains(text)) {
                newItems.add(item);
            }
        }

        table.setItems(newItems);
    }

    private static void cancel() {
        tooltip.hide();
    }

    private static void reset() {
        table.setItems(items);
        textField.clear();
        column = null;
        table = null;
        items = null;
    }

}
