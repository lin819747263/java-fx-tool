package com.kehua.xml.data;

import com.kehua.xml.model.Dict;
import com.kehua.xml.model.I18n;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class CommonDataManager {

    public static File currentFile;

    public final static ObservableList<I18n> I_18_NS =
            FXCollections.observableArrayList(
            );

    public final static ObservableList<Dict> DICT =
            FXCollections.observableArrayList(
            );

    public final static LinkedList<Integer> BLOCK_QUEUE = new LinkedList<>();

    public final static LinkedBlockingQueue<File> BACK_MANAGER = new LinkedBlockingQueue<>();

}
