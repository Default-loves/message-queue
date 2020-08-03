package com.junyi.rpc.transport.command;

import java.nio.charset.StandardCharsets;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class ResponseHeader extends Header {
    private int code;
    private String error;

    public ResponseHeader(int requestID, int version, int type, Throwable throwable) {
        this(requestID, version, type, Code.UNKNOWN_ERROR.getCode(), throwable.toString());
    }
    public ResponseHeader(int requestID, int version, int type) {
        this(requestID, version, type, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader(int requestID, int version, int type, int code, String error) {
        super(requestID, version, type);
        this.code = code;
        this.error = error;
    }
    @Override
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                Integer.BYTES +
                (error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
