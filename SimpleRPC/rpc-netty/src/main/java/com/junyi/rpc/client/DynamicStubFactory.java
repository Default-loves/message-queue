package com.junyi.rpc.client;

import com.itranswarp.compiler.JavaStringCompiler;
import com.junyi.rpc.transport.Transport;

import java.util.Map;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class DynamicStubFactory implements StubFactory {
    private static final String STUB_SOURCE_TEMPLATE =
            "package com.junyi.rpc.client.stubs;\n" +
            "import com.junyi.rpc.client.stubs.AbstractStub;\n" +
            "import com.junyi.rpc.client.stubs.RpcRequest;\n" +
            "import com.junyi.rpc.serialize.SerializeSupport;\n" +
            "public class %s extends AbstractStub implements %s {\n" +
            "@Override\n" +
            "public String %s (String arg) {\n" +
            "return SerializeSupport.parse(\n" +
            "invokeRemote(\n" +
            "new RpcRequest(\"%s\", \"%s\", SerializeSupport.serialize(arg))));\n" +
            "}\n" +
            "}\n";

    @Override
    public <T> T createStub(Transport transport, Class<T> service) {
        try {
            //填充模板
            String stubSimpleName = service.getSimpleName() + "Stub";
            String stubFullName = "com.junyi.rpc.client.stubs." + stubSimpleName;
            String classFullName = service.getName();
            String methodName = service.getMethods()[0].getName();
            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName, methodName, classFullName, methodName);
            //编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);
            Class<?> aClass = compiler.loadClass(stubFullName, results);
            ServiceStub serviceStub = (ServiceStub) aClass.newInstance();
            serviceStub.setTransport(transport);
            return (T) serviceStub;
        } catch(Throwable r) {
            throw new RuntimeException(r);
        }
    }
}
