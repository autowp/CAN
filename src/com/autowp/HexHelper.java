package com.autowp;

import javax.xml.bind.DatatypeConverter;

public class HexHelper {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public static class HexException extends Exception {

        private static final long serialVersionUID = 1L;
        
        public HexException(String message) {
            super(message);
        }
    }
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static byte hexCharToByte(byte hex) throws HexException
    {
        byte result = 0;
        if (hex >= 0x30 && hex <= 0x39) {
            result = (byte) (hex - 0x30);
        } else if (hex >= 0x41 && hex <= 0x46) {
            result = (byte) (hex - 0x41 + 0x0A);
        } else if (hex >= 0x61 && hex <= 0x66) {
            result = (byte) (hex - 0x61 + 0x0A);
        } else {
            throw new HexException("Unexpected hex character");
        }
        return result;
    }
    
    public static byte[] bytesFromHex(byte[] hexBytes) throws HexException {
        
        if (hexBytes.length % 2 != 0) {
            throw new HexException("Not even bytes count: " + hexBytes.length);
        }
        /*String str = new String(Hex.encodeHex(hexBytes));
        System.out.println("Hex: " + str);*/
        
        //int bytesCount = hexBytes.length / 2;
        
        String str2 = new String(hexBytes);
        
        byte[] bytes = DatatypeConverter.parseHexBinary(str2);
        
        
        
        /*for (int i=0; i<bytesCount; i++) {
            byte high = hexBytes[i];
            byte low = hexBytes[i+1]; 
            bytes[i] = (byte) (hexCharToByte(high) << 4 + hexCharToByte(low));
        }*/
        return bytes;
    }
    
    public static String canIdToHex(int id) {
        return String.format("%03X", id);
    }
    
    public static int canIdFromHex(byte bytes[]) {
        return Integer.parseInt(new String(bytes), 16);
    }
}
