package com.kehua.xml.service;

import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.utils.PathUtil;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class HelpService {


    public void initFile() throws IOException {
        copyFileToSystem("软件手册.docx");
        copyFileToSystem("如何配置.docx");
    }

    public void copyFileToSystem(String filename)
          throws IOException {
        InputStream path = this.getClass().getResourceAsStream("/" + filename);
        File file = new File(PathUtil.getBasePath(GloableSetting.workPath) + filename);
        if(!file.exists()){
            OutputStream output = null;
            try {
                output = new FileOutputStream(PathUtil.getBasePath(GloableSetting.workPath) + filename);
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = path.read(buf)) != -1) {
                    output.write(buf, 0, bytesRead);
                }
            } finally {
                path.close();
                output.close();
            }
        }
    }
}
