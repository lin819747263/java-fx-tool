package com.kehua.xml.utils;

import com.kehua.xml.data.GloableSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class DictUtil {

    public static Properties getDictByType(String type){
        return FileUtil.readProperties(PathUtil.getDictPath(GloableSetting.workPath) + type + ".properties");
    }

    public static List<String> getDictKeysByType(String type){
        Properties properties = FileUtil.readProperties(PathUtil.getDictPath(GloableSetting.workPath) + type + ".properties");
        return properties.keySet().stream().map(Object::toString).collect(Collectors.toList());
    }

    public static List<String> getDictValuesByType(String type){
        Properties properties = FileUtil.readProperties(PathUtil.getDictPath(GloableSetting.workPath) + type + ".properties");
        return properties.values().stream().map(Object::toString).collect(Collectors.toList());
    }

    public static String getKeyByValue(String type, String value){
        Properties properties = FileUtil.readProperties(PathUtil.getDictPath(GloableSetting.workPath) + type + ".properties");
        Map<String,String> map = new HashMap<>();
        properties.forEach((k,v) -> map.put(v.toString(),k.toString()));
        return map.get(value);
    }

    public static String getDictByKey(String type, String key){
        Object o = getDictByType(type).get(key);
        return o == null? null : o.toString();
    }
}
