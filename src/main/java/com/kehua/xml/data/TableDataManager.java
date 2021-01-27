package com.kehua.xml.data;

import com.kehua.xml.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.LinkedList;

public class TableDataManager {

    public final static ObservableList<Device> DEVICES =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<Block> BLOCKS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<YcPoint> YC_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<YxPoint> YX_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<YkPoint> YK_POINTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<Event> EVENTS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<Store> STORES =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<DebugCollect> DEBUG_COLLECT =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<DebugYk> DEBUG_COLLECT_YK =
            FXCollections.observableArrayList(
            );
}
