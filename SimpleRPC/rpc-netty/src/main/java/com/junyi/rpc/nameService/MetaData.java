package com.junyi.rpc.nameService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 * User: JY
 * Date: 2020/5/4 0004
 * Description:
 */
public class MetaData extends HashMap<String, List<URI>> {      //服务名称-服务提供者URI列表

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MetaData:\n");
        for(Entry<String, List<URI>> entry: entrySet()) {
            sb.append("\t").append("ClassName: ")
                    .append(entry.getKey()).append("\n");
            sb.append("\t").append("URI list: ").append("\n");
            for(URI uri: entry.getValue()) {
                sb.append("\t\t").append(uri).append("\n");
            }
        }
        return sb.toString();
    }
}
