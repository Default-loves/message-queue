package com.junyi.rpc.serialize.impl;

import com.junyi.rpc.client.stubs.RpcRequest;
import com.junyi.rpc.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest entity) {
        return Integer.BYTES + entity.getInterfaceName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + entity.getMethodName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + entity.getSerializedArgs().length;
    }

    @Override
    public void serialize(RpcRequest entity, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] tmpByte = entity.getInterfaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpByte.length);
        buffer.put(tmpByte);

        tmpByte = entity.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpByte.length);
        buffer.put(tmpByte);

        tmpByte = entity.getSerializedArgs();
        buffer.putInt(tmpByte.length);
        buffer.put(tmpByte);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmpByte = new byte[len];
        buffer.get(tmpByte);
        String interfaceName = new String(tmpByte, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpByte = new byte[len];
        buffer.get(tmpByte);
        String methodName = new String(tmpByte, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpByte = new byte[len];
        buffer.get(tmpByte);
        byte[] serializedArg = tmpByte;

        return new RpcRequest(interfaceName, methodName, serializedArg);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getClassType() {
        return RpcRequest.class;
    }
}
