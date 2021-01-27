package com.kehua.xml.analysis.modbus.serial;

import com.kehua.xml.utils.ByteUtil;
import com.serotonin.modbus4j.sero.log.BaseIOLog;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;
import java.io.OutputStream;

public class SerialOutputStream extends OutputStream {

    private BaseIOLog baseIOLog;

    SerialPort serialPort;

    /**
     * Instantiates a SerialOutputStream for the given {@link SerialPort} Do not
     * create multiple streams for the same serial port unless you implement
     * your own synchronization.
     *
     * @param sp The serial port to stream.
     */
    public SerialOutputStream(SerialPort sp, BaseIOLog baseIOLog) {
        serialPort = sp;
        this.baseIOLog = baseIOLog;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            serialPort.writeInt(b);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byte[] buffer = new byte[len];
        System.arraycopy(b, off, buffer, 0, len);
        try {
            serialPort.writeBytes(buffer);
            if(baseIOLog != null){
                baseIOLog.log(" Send " + ByteUtil.bytesToHexStringSplitBySpace(buffer));
            }

        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }
}
