package com.kehua.xml.utils;

import com.kehua.xml.analysis.modbus.ModbusMasterFactory;
import com.kehua.xml.enumerate.ModbusFunCodeEnum;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.*;

public class ModbusUtil {

    /**
     * 读线圈，对应功能码01
     * @param slaveId 从机地址
     * @param start   开始地址
     * @param len     读取数量
     */
    public static byte[] readCoilStatus(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, start, len);
        ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);

        return response.getData();
    }

    /**
     * 对应功能码02
     * @param slaveId 从机地址
     * @param start   开始地址
     * @param len     读取数量
     */
    public static byte[] readInputStatus(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {

        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);

        return response.getData();
    }

    /**
     * // 读取[03 Holding Register类型 2x]模拟量数据
     * @param slaveId 从机地址
     * @param start   起始地址
     * @param len     读取数量
     */
    public static byte[] readHoldingRegister(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {

        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);

        return response.getData();
    }

    /**
     * 读取[04 Input Registers 3x]类型 模拟量数据
     * @param slaveId 从机地址
     * @param start   起始地址
     * @param len     读取数量
     */
    public static byte[] readInputRegisters(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);

        return response.getData();
    }

    /**
     * 将上面的合起来
     * @param funCode 类型
     * @param master
     * @param slaveId 从机地址
     * @param start   起始地址
     * @param len     读取数量
     * @return
     * @throws ModbusTransportException
     */
    @SuppressWarnings("ConstantConditions")
    public static byte[] readRegisters(int funCode, ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ModbusRequest request;
        switch (EnumUtils.getByValue(funCode, ModbusFunCodeEnum.class)) {
            case Coil_Status:
                request = new ReadCoilsRequest(slaveId, start, len);
                break;
            case Input_Status:
                request = new ReadDiscreteInputsRequest(slaveId, start, len);
                break;
            case Holding_Register:
                request = new ReadHoldingRegistersRequest(slaveId, start, len);
                break;
            case Input_Register:
                request = new ReadInputRegistersRequest(slaveId, start, len);
                break;
            default:
                return null;
        }
        ReadResponse response = (ReadResponse) master.send(request);
        return response.getData();
    }

    /**
     * 写单个类型 写线圈 对应功能码05
     * @param master
     * @param slaveId 从机地址
     * @param offset  写入位置
     * @param value   写入值
     * @throws ModbusTransportException
     */
    public static Boolean writeCoil(ModbusMaster master, int slaveId, int offset, Boolean value) throws ModbusTransportException {
        WriteCoilRequest request = new WriteCoilRequest(slaveId, offset, value);
        WriteCoilResponse response = (WriteCoilResponse) master.send(request);
        return response.isException();
    }

    /**
     * 写多个类型 写线圈
     * 对应功能码05
     * @param master
     * @param slaveId 从机地址
     * @param offset  写入位置
     * @param values  写入值
     * @throws ModbusTransportException
     */
    public static Boolean writeCoils(ModbusMaster master, int slaveId, int offset, boolean[] values) throws ModbusTransportException {
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, offset, values);
        WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
        return response.isException();
    }

    /**
     * 写单个寄存器 对应功能码 06
     * @param master
     * @param slaveId 从机地址
     * @param offset  写入位置
     * @param value   写入值
     */
    public static Boolean writeRegister(ModbusMaster master, int slaveId, int offset, int value) throws ModbusTransportException {
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
        WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
        return response.isException();
    }

    /**
     * 写多个寄存器 对应功能码 16
     * @param master
     * @param slaveId 从机地址
     * @param start   起始地址
     * @param values  值
     */
    public static Boolean writeRegisters(ModbusMaster master, int slaveId, int start, short[] values) throws ModbusTransportException {
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);
        WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
        return response.isException();
    }


    public static void main(String[] args) throws ModbusTransportException, ModbusInitException {
        //        ModbusMaster master = ModbusMasterFactory.createSerialMaster("COM2");

        short[] a = new short[]{2, 1, 3, 5};
        ModbusMaster master = ModbusMasterFactory.createTcpMaster("127.0.0.1", 9999);

        Boolean bytes = ModbusUtil.writeRegisters(master, 1, 1001, a);
        System.out.println(bytes);
    }
}
