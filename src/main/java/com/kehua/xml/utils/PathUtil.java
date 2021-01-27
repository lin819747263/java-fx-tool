package com.kehua.xml.utils;

import java.io.File;

public class PathUtil {

    public static String getLogPath(String baseUrl){
        String path = baseUrl + "/necp-xml/logs/";
        createDir(path);
        return path;
    }

    public static String getSettingPath(String baseUrl){
        String path = baseUrl + "/necp-xml/setting/";
        createDir(path);
        return path;
    }

    public static String getDictPath(String baseUrl){
        String path = baseUrl + "/necp-xml/config/";
        createDir(path);
        return path;
    }

    public static String getBackPath(String baseUrl){
        String path = baseUrl + "/necp-xml/backup/";
        createDir(path);
        return path;
    }

    public static String getBasePath(String baseUrl){
        String path = baseUrl + "/necp-xml/";
        createDir(path);
        return path;
    }

    private static void createDir(String path){
        if(!new File(path).exists()){
            FileUtil.mkdirsIfNotExist(path);
        }
    }
}
