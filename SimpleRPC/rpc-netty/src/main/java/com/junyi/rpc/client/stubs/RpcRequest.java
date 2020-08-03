package com.junyi.rpc.client.stubs;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class RpcRequest {
    private final String interfaceName;
    private final String methodName;
    private final byte[] serializedArgs;
    public RpcRequest(String interfaceName, String methodName, byte[] serializedArgs) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArgs = serializedArgs;
    }
    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getSerializedArgs() {
        return serializedArgs;
    }
}
