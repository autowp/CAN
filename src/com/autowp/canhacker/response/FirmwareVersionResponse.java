package com.autowp.canhacker.response;

public class FirmwareVersionResponse extends Response {
    final public static char CODE = 'v';
    
    protected String version;

    public FirmwareVersionResponse(byte[] bytes) throws ResponseException
    {
        if (bytes.length != 5) {
            throw new ResponseException("Version response must be 5 bytes long");
        }
        
        this.version = (new String(bytes)).substring(1);
    }
    
    @Override
    public String toString() {
        return CODE + this.version;
    }

    public String getVersion()
    {
        return this.version;
    }
}
