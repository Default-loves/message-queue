package com.junyi.rpc.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
//public class ServiceSupport {
//    private static final Map<String, Object> singletonServices = new HashMap<>();
//
//    public synchronized static <S> S load(Class<S> service) {
//        return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
//                .map(ServiceSupport::singletonFilter)
//                .findFirst()
//                .orElseThrow(ServiceLoadException::new);
//    }
//
//    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
//        return StreamSupport.stream(ServiceLoader.load(service).spliterator(), false)
//                .map(ServiceSupport::singletonFilter).collect(Collectors.toList());
//    }
//
//    @SuppressWarnings("unchecked")
//    //TODO unknow
//    private static <S>  S singletonFilter(S service) {
//        if(service.getClass().isAnnotationPresent(Singleton.class)) {
//            String className = service.getClass().getCanonicalName();
//            Object singletonInstance = singletonServices.putIfAbsent(className, service);
//            return singletonInstance == null ? service : (S) singletonInstance;
//        } else {
//            return service;
//        }
//    }
//}
public class ServiceSupport {
    private static final Logger logger = LoggerFactory.getLogger(ServiceSupport.class);
    private final static Map<String, Object> singletonServices = new HashMap<>();
    public synchronized static <S> S load(Class<S> service) {
        return StreamSupport.
                stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSupport::singletonFilter)
                .findFirst().orElseThrow(ServiceLoadException::new);
    }
    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport.
                stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSupport::singletonFilter).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <S>  S singletonFilter(S service) {

        if(service.getClass().isAnnotationPresent(Singleton.class)) {
            String className = service.getClass().getCanonicalName();
            Object singletonInstance = singletonServices.putIfAbsent(className, service);
            return singletonInstance == null ? service : (S) singletonInstance;
        } else {
            return service;
        }
    }
}

