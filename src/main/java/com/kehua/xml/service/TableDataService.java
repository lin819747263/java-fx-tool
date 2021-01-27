package com.kehua.xml.service;

import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.TableDataManager;
import org.springframework.stereotype.Service;

@Service
public class TableDataService {

    public void clearAllData(){
        TableDataManager.DEVICES.clear();
        TableDataManager.BLOCKS.clear();
        TableDataManager.YX_POINTS.clear();
        TableDataManager.YC_POINTS.clear();
        TableDataManager.YK_POINTS.clear();
        TableDataManager.EVENTS.clear();
        CommonDataManager.I_18_NS.clear();
        TableDataManager.STORES.clear();
    }

    public void clearAllAppData(){
        AppTableDataManager.DEVICES.clear();
        AppTableDataManager.BLOCKS.clear();
        AppTableDataManager.YX_POINTS.clear();
        AppTableDataManager.YC_POINTS.clear();
        AppTableDataManager.YK_POINTS.clear();
        AppTableDataManager.RECORDS.clear();
        CommonDataManager.I_18_NS.clear();
    }
}
