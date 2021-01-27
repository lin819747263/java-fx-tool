package com.kehua.xml.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static void writeToProperties(Properties properties,String path){
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter opw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            properties.store(opw,"保存数据");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties readProperties(String path){
        Properties properties = new Properties();;
        try {
            FileUtil.createFileIfNotExist(path);
            Reader inStream = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            properties.load(inStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void mkdirsIfNotExist(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void createFileIfNotExist(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
    }

    public static List<String> getAllFile(String directoryPath) {
        List<String> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            list.add(file.getAbsolutePath());
        }
        return list;
    }

    /**
     * 删除文件.
     *
     * @param fileDir 文件路径
     */
    public static boolean deleteFile(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        // 判断目录或文件是否存在
        if (!file.exists()) {
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {
                file.delete();
                flag = true;
            }
        }
        return flag;
    }


    public static void zipFiles(File zipFile, List<String> excelUrls) {
        // 判断压缩后的文件存在不，不存在则创建
        List<File> srcFiles = new ArrayList<>();
        for (String fileUrl : excelUrls) {
            File file = new File(fileUrl);
            srcFiles.add(file);
        }

        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组
            for (File file : srcFiles) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                fileInputStream = new FileInputStream(file);
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry("necp/" + file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
                fileInputStream.close();
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
