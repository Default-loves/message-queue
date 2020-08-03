package com.junyi.rpc.transport.command;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class Header {
    private int requestID;
    private int version;
    private int type;

    public Header(int requestID, int version, int type) {
        this.requestID = requestID;
        this.version = version;
        this.type = type;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }
}
