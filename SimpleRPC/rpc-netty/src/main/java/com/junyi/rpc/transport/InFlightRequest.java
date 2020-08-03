package com.junyi.rpc.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class InFlightRequest implements Closeable {
    private final static long TIME_OUT = 10L;
    private Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final Semaphore semaphore = new Semaphore(10);
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture scheduledFuture;

    public InFlightRequest() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeFutureTimeout, TIME_OUT, TIME_OUT, TimeUnit.SECONDS);
    }

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIME_OUT, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    private void removeFutureTimeout() {
        futureMap.entrySet().removeIf(entry -> {
           if ( System.nanoTime() - entry.getValue().getTimestamp() > TIME_OUT * 1000000000L) {
               semaphore.release();
               return true;
           } else {
               return false;
           }
        });
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture responseFuture = futureMap.remove(requestId);
        if (null != responseFuture) {
            semaphore.release();
        }
        return responseFuture;
    }

    @Override
    public void close() {
        scheduledExecutorService.shutdown();
        scheduledFuture.cancel(true);

    }
}
