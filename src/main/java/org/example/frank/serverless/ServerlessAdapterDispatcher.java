package org.example.frank.serverless;

import java.util.HashMap;
import java.util.Map;

public class ServerlessAdapterDispatcher {
    private static Map<String, ServerlessAdapter> adapters = new HashMap();

    public static void addAdapter(ServerlessAdapter adapter){
        adapters.put(adapter.getName(), adapter);
    }

    public static ServerlessAdapter getAdapter(String adapterName){
        return adapters.get(adapterName);
    }
}
