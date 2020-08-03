package com.junyi.rpc.nameService;

import com.junyi.rpc.NameService;
import com.junyi.rpc.serialize.SerializeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class LocalFileNameService implements NameService {
    private static final Logger logger =  LoggerFactory.getLogger(LocalFileNameService.class);
    private static final Collection<String> supportSchema = Collections.singleton("file");
    private File file;
    @Override
    public Collection<String> supportSchema() {
        return supportSchema;
    }

    @Override
    public void connect(URI nameServiceURI) {
        if (supportSchema.contains(nameServiceURI.getScheme()))
            file = new File(nameServiceURI);
        else {
            throw new RuntimeException(String.format("Un support schema: {}", nameServiceURI.getScheme()));
        }
    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {
        logger.info("Register service : {}, URI: {}", serviceName, uri);
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel()) {
            FileLock lock = channel.lock();
            try {
                int len = (int) raf.length();
                byte[] bytes;
                MetaData metaData;
                if (len > 0) {
                    bytes = new byte[(int) raf.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        channel.read(buffer);
                    }
                    metaData = SerializeSupport.parse(bytes);
                } else {
                    metaData = new MetaData();
                }
                List<URI> uris = metaData.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(uri))
                    uris.add(uri);
                logger.info(metaData.toString());
                bytes = SerializeSupport.serialize(metaData);
                channel.truncate(bytes.length);
                channel.position(0L);
                channel.write(ByteBuffer.wrap(bytes));
                channel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        MetaData metaData;
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining())
                    fileChannel.read(buffer);
                metaData = bytes.length == 0 ? new MetaData() : SerializeSupport.parse(bytes);
                logger.info(metaData.toString());
            } finally {
                lock.release();
            }
        }
        List<URI> uris = metaData.get(serviceName);
        if (null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }
}
