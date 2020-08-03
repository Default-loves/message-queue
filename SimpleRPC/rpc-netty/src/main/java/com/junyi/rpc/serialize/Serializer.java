package com.junyi.rpc.serialize;

/**
 * 序列化接口
 */
public interface Serializer<T> {
    int size(T entity);

    void serialize(T entity, byte[] bytes, int offset, int length);

    T parse(byte[] bytes, int offset, int length);

    byte type();

    Class<T> getClassType();
}
