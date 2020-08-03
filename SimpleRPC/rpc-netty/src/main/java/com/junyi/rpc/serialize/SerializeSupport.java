package com.junyi.rpc.serialize;


import com.junyi.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class SerializeSupport {
    private static final Logger logger = LoggerFactory.getLogger(SerializeSupport.class);
    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();    //Key：数据类型；Value：序列化实现类
    private static Map<Byte, Class<?>> typeMap = new HashMap<>();       //Key：字节流的数据类型；V：数据类型

    static {
        for (Serializer serializer : ServiceSupport.loadAll(Serializer.class)) {
            serializerMap.put(serializer.getClassType(), serializer);
            typeMap.put(serializer.type(), serializer.getClassType());
            logger.info("Found serializer, Class: {}, Type: {}",
                    serializer.getClassType().getCanonicalName(), serializer.type());
        }
    }
    public static <E> E parse(byte[] bytes) {
        return parse(bytes, 0, bytes.length);
    }

    private static <E> E parse(byte[] bytes, int offset, int length) {
        byte type = parseType(bytes);
        @SuppressWarnings("unchecked")
        Class<E> eClass = (Class<E>) typeMap.get(type);
        if (null == eClass) {
            throw new SerializeException(String.format("Don't have type %s", type));
        } else {
            return parse(bytes, offset+1, length-1, eClass);
        }
    }
    @SuppressWarnings("unchecked")
    private static <E> E parse(byte[] bytes, int offset, int length, Class<?> eClass) {
        Object object = serializerMap.get(eClass).parse(bytes, offset, length);
        if (eClass.isAssignableFrom(object.getClass())) {
            return (E) object;
        } else {
            throw new SerializeException("Type mismatch");
        }
    }

    private static byte parseType(byte[] bytes) {
        return bytes[0];
    }

    @SuppressWarnings("unchecked")
    public static <E> byte[] serialize(E entry) {
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if (null == serializer) {
            throw new SerializeException(String.format("Unknow type in serialize: %s", entry.getClass().toString()));
        }
        byte[] bytes = new byte[serializer.size(entry)+1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length-1);
        return bytes;
    }
}
