package com.kehua.xml.service;


import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.*;
import com.kehua.xml.utils.DataTransformUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebugService {

    @Autowired
    private FileService fileService;


    public List<DebugCollect> getCollectData(){
        List<YxPoint> yxData = new ArrayList<>(TableDataManager.YX_POINTS);
        List<YcPoint> ycData = new ArrayList<>(TableDataManager.YC_POINTS);
        List<YkPoint> ykData = new ArrayList<>(TableDataManager.YK_POINTS);

        List<YxPoint> yxPoints = fileService.collectYx(yxData,ycData,ykData);
        List<YcPoint> ycPoints = fileService.collectYc(ycData,ykData);

        List<DebugCollect> res = new ArrayList<>(DataTransformUtils.transformList(yxPoints, DebugCollect.class));
        res.addAll(DataTransformUtils.transformList(ycPoints,DebugCollect.class));
        return res;
    }

    public List<DebugYk> getYkData() {
        List<DebugYk> res = new ArrayList<>();

        List<YkPoint> ykData = new ArrayList<>(TableDataManager.YK_POINTS);
        Collections.sort(ykData);

        List<YkPoint> singledata = ykData.stream().filter(x -> !"16".equals(x.getFunCode()) && StringUtils.isNotBlank(x.getFunCode()))
                .collect(Collectors.toList());
        res.addAll(DataTransformUtils.transformList(singledata, DebugYk.class));

        int index = 0;
        for(YkPoint ykPoint : ykData){
            if(StringUtils.isNotBlank(ykPoint.getRegNum())){
                List<YkPoint>  blockList = ykData.subList(index + 1, index + Integer.parseInt(ykPoint.getPointNum())+1);
                DebugYk debugYk = DataTransformUtils.transformEntity(ykPoint, DebugYk.class);
                debugYk.setIsBlock(true);
                debugYk.setPoints(FXCollections.observableArrayList(DataTransformUtils.transformList(blockList, DebugYk.class)));
                res.add(debugYk);
            }
            index++;
        }
        return res;
    }

    public void initData() {
        TableDataManager.DEBUG_COLLECT.addAll(getCollectData());
        TableDataManager.DEBUG_COLLECT_YK.addAll(getYkData());
    }

}
