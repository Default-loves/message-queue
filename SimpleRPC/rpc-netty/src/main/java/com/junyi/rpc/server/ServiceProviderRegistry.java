package com.junyi.rpc.server;

public interface ServiceProviderRegistry {
    <T> void addServiceProvider(Class<T> serviceClass, T service);
}
