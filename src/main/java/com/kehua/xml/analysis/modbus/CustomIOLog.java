package com.kehua.xml.analysis.modbus;

import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.data.LogQueue;
import com.kehua.xml.utils.PathUtil;
import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.log.BaseIOLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomIOLog extends BaseIOLog {

    private static final CustomIOLog INSTANCE = new CustomIOLog(new File(PathUtil.getLogPath(GloableSetting.workPath) + "log.txt"), LogQueue.logQueue);

    public static CustomIOLog getIOLogInstance(){
        return INSTANCE;
    }

    protected static final String DATE_FORMAT = "HH:mm:ss";
    protected final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    private LinkedBlockingQueue<String> queue = null;

    private CustomIOLog(File logFile, LinkedBlockingQueue<String> linkedBlockingQueue) {
        super(logFile);
        this.queue = linkedBlockingQueue;
    }

    @Override
    protected void sizeCheck() {

    }

    @Override
    public synchronized void log(String message) {
        this.sizeCheck();
        this.sb.delete(0, this.sb.length());
        this.date.setTime(System.currentTimeMillis());
        this.sb.append(this.sdf.format(this.date)).append(" ");
        this.sb.append(message);
        this.out.println(this.sb.toString());
        this.out.flush();
        if(queue != null){
            queue.add(this.sb.toString());
        }
    }

    @Override
    public synchronized void log(boolean input, byte[] b, int pos, int len) {
        this.sizeCheck();
        this.sb.delete(0, this.sb.length());
        this.date.setTime(System.currentTimeMillis());
        this.sb.append(this.sdf.format(this.date)).append(" ");
        this.sb.append(input ? "Recv" : "Send").append(" ");
        this.sb.append(getFileAddSpace(StreamUtils.dumpHex(b, pos, len)));
        this.out.println(this.sb.toString());
        this.out.flush();
        if(queue != null){
            queue.add(this.sb.toString());
        }
    }


    private String getFileAddSpace(String replace) {
        replace = replace.trim();
        String regex = "(.{2})";
        replace = replace.replaceAll(regex, "$1 ");
        return replace;
    }

}
