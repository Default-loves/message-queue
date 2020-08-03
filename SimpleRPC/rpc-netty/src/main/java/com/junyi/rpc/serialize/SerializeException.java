package com.junyi.rpc.serialize;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) { super(msg);}
    public SerializeException(Throwable throwable) { super(throwable);}
}
