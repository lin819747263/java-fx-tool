package com.kehua.xml.service;

import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.model.Device;
import com.kehua.xml.model.I18n;
import com.kehua.xml.model.app.AppYcPoint;
import com.kehua.xml.model.app.AppYkPoint;
import com.kehua.xml.model.app.AppYxPoint;
import com.kehua.xml.model.app.RecordPoint;
import com.kehua.xml.utils.DictUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppI18nService {

    private final static Map<String,I18n> I18N = new HashMap<>();

    public void extractAppI18n() {
        List<Device> device = new ArrayList<>(AppTableDataManager.DEVICES);
        List<AppYxPoint> yx = new ArrayList<>(AppTableDataManager.YX_POINTS);
        List<AppYcPoint> yc = new ArrayList<>(AppTableDataManager.YC_POINTS);
        List<AppYkPoint> yk = new ArrayList<>(AppTableDataManager.YK_POINTS);
        List<RecordPoint> records = new ArrayList<>(AppTableDataManager.RECORDS);

        //提取国际化
        Set<String> i18n = new HashSet<>();

        if(device.size() > 0){
            i18n.add(device.get(0).getProtocolName());
        }
        yx.forEach(x -> {
            if("83".equals(x.getRegType())){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });
        yc.forEach(x -> {
            if(StringUtils.isNotBlank(x.getUserDefined1()) && x.getUserDefined1().contains("=")){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });
        yk.forEach(x -> {
            if(StringUtils.isNotBlank(x.getUserDefined1()) && x.getUserDefined1().contains("=")){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });

        i18n.addAll(DictUtil.getDictKeysByType("APP遥信分组"));
        i18n.addAll(DictUtil.getDictKeysByType("APP遥测分组"));
        i18n.addAll(DictUtil.getDictKeysByType("APP遥控分组"));

        records.forEach(x -> {
            if(StringUtils.isNotBlank(x.getUserDefined1()) && x.getUserDefined1().contains("=")){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });

        backupI18n();
        CommonDataManager.I_18_NS.clear();

        for(String s : i18n){
            if(I18N.get(s) != null){
                CommonDataManager.I_18_NS.add(I18N.get(s));
            }else {
                I18n i18n1 = new I18n();
                i18n1.setRemark(s);
                if(AppTableDataManager.GROUP_I18N.get(s) != null){
                    i18n1.setRemarkEn(AppTableDataManager.GROUP_I18N.get(s));
                }
                CommonDataManager.I_18_NS.add(i18n1);
            }
        }

        I18N.clear();
    }

    private void backupI18n() {
        List<I18n> yx = new ArrayList<>(CommonDataManager.I_18_NS);
        yx.forEach(x -> I18N.put(x.getRemark(),x));
    }
}
