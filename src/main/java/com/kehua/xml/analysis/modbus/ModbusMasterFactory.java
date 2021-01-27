package com.kehua.xml.analysis.modbus;

import com.kehua.xml.analysis.modbus.serial.SerialPortWrapperImpl;
import com.kehua.xml.data.SettingConfig;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.serial.SerialPortWrapper;

public class ModbusMasterFactory {

    private static final ModbusFactory MODBUS_FACTORY;

    static {
        MODBUS_FACTORY = new ModbusFactory();
    }

    public static ModbusMaster createSerialMaster(String commPortId) throws ModbusInitException {
        int baudRate = 9600;
        int flowControlIn = 0;
        int flowControlOut = 0;
        int dataBits = 8;
        int stopBits = 1;
        int parity = 0;

        SerialPortWrapper wrapper = new SerialPortWrapperImpl(commPortId, baudRate, dataBits, stopBits, parity, flowControlIn, flowControlOut);

        ModbusMaster master = MODBUS_FACTORY.createRtuMaster(wrapper);
        master.init();
        return master;
    }

    public static ModbusMaster createTcpMaster(String host, Integer port) throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost(host);
        params.setPort(port);

        ModbusMaster master = MODBUS_FACTORY.createTcpMaster(params, false);
        master.init();
        master.setIoLog(CustomIOLog.getIOLogInstance());
        return master;
    }

    public static ModbusMaster createMaster() throws ModbusInitException {
        SettingConfig config = SettingConfig.getDefault();
        if (!config.getConnectWay().check()){
            return null;
        }
        return createMaster(config);
    }

    public static ModbusMaster createMaster(SettingConfig config) throws ModbusInitException {
        SettingConfig.ConnectWay connect = config.getConnectWay();
        if (connect instanceof SettingConfig.TCPConnect){
            return ModbusMasterFactory.createTcpMaster(((SettingConfig.TCPConnect) connect).getHost(), ((SettingConfig.TCPConnect) connect).getPort());
        }
        if (connect instanceof SettingConfig.SERIALConnect){
            return ModbusMasterFactory.createSerialMaster(((SettingConfig.SERIALConnect) connect).getCommPort());
        }
        return null;
    }

}
