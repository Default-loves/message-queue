package com.junyi.rpc.serialize.impl;

import com.junyi.rpc.nameService.MetaData;
import com.junyi.rpc.serialize.Serializer;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class MetaDataSerializer implements Serializer<MetaData> {
    @Override
    public int size(MetaData entity) {
        return Short.BYTES +
                entity.entrySet().stream().mapToInt(this::entrySize).sum();
    }

    private int entrySize(Map.Entry<String, List<URI>> entry) {
        return Short.BYTES +    // Key length
                entry.getKey().getBytes().length +
                Short.BYTES +   // Value length
                entry.getValue().stream().mapToInt(uri -> {
                    return Short.BYTES +
                            uri.toASCIIString().getBytes(StandardCharsets.UTF_8).length;
                }).sum();
    }

    @Override
    public void serialize(MetaData entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putShort((short) entry.size());
        entry.forEach((k, v) -> {
            byte[] tmpByte = k.getBytes(StandardCharsets.UTF_8);
            buffer.putShort((short) tmpByte.length);
            buffer.put(tmpByte);

            buffer.putShort((short) v.size());
            for(URI uri : v) {
                tmpByte = uri.toASCIIString().getBytes(StandardCharsets.UTF_8);
                buffer.putShort((short) tmpByte.length);
                buffer.put(tmpByte);
            }
        });
    }

    @Override
    public MetaData parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        short sizeOfMap = buffer.getShort();
        MetaData metaData = new MetaData();
        for (int i = 0; i < sizeOfMap; i++) {
            short sizeOfKey = buffer.getShort();
            byte[] keyByte = new byte[sizeOfKey];
            buffer.get(keyByte);
            String key = new String(keyByte, StandardCharsets.UTF_8);

            short sizeOfValue = buffer.getShort();
            List<URI> list = new ArrayList<>(sizeOfValue);
            for (int j = 0; j < sizeOfValue; j++) {
                short sizeOfURI = buffer.getShort();
                byte[] uriByte = new byte[sizeOfURI];
                buffer.get(uriByte);
                URI uri = URI.create(new String(uriByte, StandardCharsets.UTF_8));
                list.add(uri);
            }
            metaData.put(key, list);
        }
        return metaData;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<MetaData> getClassType() {
        return MetaData.class;
    }
}
