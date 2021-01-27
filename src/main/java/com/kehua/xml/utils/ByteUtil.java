package com.kehua.xml.utils;

import java.nio.ByteOrder;

public class ByteUtil {
	public static byte[] intTo4Byte(int value) {
		return intTo4Byte(value, ByteOrder.BIG_ENDIAN);
	}
	
	
	public static byte[] intTo4Byte(int value, ByteOrder order) {
		byte[] result = new byte[4];
		
		if (ByteOrder.BIG_ENDIAN.equals(order)) {
			result[0] = (byte)((value >>> 24) & 0xff);
	        result[1] = (byte)((value >>> 16) & 0xff);
	        result[2] = (byte)((value >>>  8) & 0xff);
	        result[3] = (byte)((value >>>  0) & 0xff);
		} else {
			result[3] = (byte)((value >>> 24) & 0xff);
	        result[2] = (byte)((value >>> 16) & 0xff);
	        result[1] = (byte)((value >>>  8) & 0xff);
	        result[0] = (byte)((value >>>  0) & 0xff);
		}
		
		return result;
	}
	
	
	public static byte[] intTo2Byte(int value) {
		return intTo2Byte(value, ByteOrder.BIG_ENDIAN);
	}
	
	public static byte[] intTo2Byte(int value, ByteOrder order) {
		byte[] result = new byte[4];
		
		if (ByteOrder.BIG_ENDIAN.equals(order)) {
	        result[0] = (byte)((value >>>  8) & 0xff);
	        result[1] = (byte)((value >>>  0) & 0xff);
		} else {
	        result[1] = (byte)((value >>>  8) & 0xff);
	        result[0] = (byte)((value >>>  0) & 0xff);
		}
		
		return result;
	}

	public static int fourByteToint(byte[] bytes) {
		return (bytes[0]&0xff)<<24
				| (bytes[1]&0xff)<<16
				| (bytes[2]&0xff)<<8
				| (bytes[3]&0xff);
	}
	
	public static int intFromBytes(byte[] data) {
		int result = 0;
		if (data == null || data.length == 0)
			return result;
		
		if (data.length == 1) {
			result = data[0] & 0xff;
		} else if(data.length == 2) {
			result = (((0xff & data[1]) 
					| ((0xff & data[0]) << 8)
					) & 0xffff);
		} else if(data.length == 3) {
			result = (((0xff & 0x00) 
					| ((0xff & data[2]) << 8)
					| ((0xff & data[1]) << 16)
					| ((0xff & data[0]) << 24)
					) & 0xffffffff);
		} else if(data.length >= 4) {
			result = (((0xff & data[3]) 
					| ((0xff & data[2]) << 8)
					| ((0xff & data[1]) << 16)
					| ((0xff & data[0]) << 24)
					) & 0xffffffff);
		}
		
		return result;
	}
	
	public static long longFromBytes(byte[] data) {
		long result = 0;
		if (data == null || data.length == 0)
			return result;
		
		if (data.length == 1) {
			result = data[0] & 0xff;
		} else if(data.length == 2) {
			result = (((0xff & data[1]) 
					| ((0xff & data[0]) << 8)
					) & 0xffff);
		} else if(data.length == 3) {
			result = (((0xff & 0x00) 
					| ((0xff & data[2]) << 8)
					| ((0xff & data[1]) << 16)
					| ((0xff & data[0]) << 24)
					) & 0xffffffffL);
		} else if(data.length == 4) {
			result = (((0xff & data[3]) 
					| ((0xff & data[2]) << 8)
					| ((0xff & data[1]) << 16)
					| ((0xff & data[0]) << 24)
					) & 0xffffffffL);
		} else if(data.length >= 8) {
			result =  ((0xff & data[7]) 
					| ((0xff & data[6]) << 8)
					| ((0xff & data[5]) << 16)
					| ((0xff & data[4]) << 24)
					| ((0xff & data[3]) << 32)
					| ((0xff & data[2]) << 40)
					| ((0xff & data[1]) << 48)
					| ((0xff & data[0]) << 56)
					);
		}
		
		return result;
	}
	
	
	public static String bytesToAscii(byte[] data) {
		String result = null;
		try {
			result = new String(data, "US-ASCII");
		} catch(Throwable ignore) {
			ignore.printStackTrace();
		}
		return result;
	}
	
	public static byte[] asciiToBytes(String value) {
		byte[] result = new byte[0];
		if (value == null)
			return result;
		
		try {
			result = value.getBytes("US-ASCII");
		} catch(Throwable ignore) {
			ignore.printStackTrace();
		}
		return result;
	}

	public static byte[] asciiToBytes(String value, int length) {
		if (length <= 0)
			return new byte[0];
		
		byte[] byteValue = asciiToBytes(value);
		
		byte[] result = new byte[length];
		for (int i = 0, lenOfValue = byteValue.length; i < length; i++) {
			if (i < lenOfValue)
				result[i] = byteValue[i];
			else
				result[i] = (byte)0x00;
		}
		
		return result;
	}
	
	
	public static String byteToHexString(byte data) {
		StringBuilder result = new StringBuilder("");
		int v = data & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2) {
			result.append(0);
		}
		result.append(hv);

		return result.toString().toUpperCase();
	}
	
	public static String bytesToHexString(byte[] datas) {
		if (datas == null || datas.length <= 0)
			return null;

		StringBuilder result = new StringBuilder("");
		for (byte data : datas) {
			result.append(byteToHexString(data));
		}

		return result.toString();
	}
	
	public static String bytesToHexStringSplitBySpace(byte[] datas) {
		if (datas == null || datas.length <= 0)
			return null;

		StringBuilder result = null;
		for (byte data : datas) {
			if (result == null) {
				result = new StringBuilder("");
			} else {
				result.append(" ");
			}

			result.append(byteToHexString(data));
		}

		return result.toString();
	}
	
	
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		// 去除空格并转为大写
		hexString = hexString.replaceAll(" ", "").toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	
	public static byte[] union(byte[]... array) {
		if (array == null)
			return new byte[0];
		
		int length = 0;
		for (byte[] datas : array) {
			length += (datas == null ? 0 : datas.length);
		}
		
		byte[] result = new byte[length];
		int index = 0;
		for (byte[] datas : array) {
			if (datas == null)
				continue;
			
			System.arraycopy(datas, 0, result, index, datas.length);
			index += datas.length;
		}
		
		return result;
	}
}
