package com.plx.android.open;

import java.util.HashMap;
import java.util.Map;

public class PluginManager {

    private Map<String, Object> mPlugins = new HashMap<>();

    private volatile static PluginManager instance;

    public static PluginManager get() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    public <T> void put(Class<T> tClass, T object){
        mPlugins.put(tClass.getName(), object);
    }

    public <T> T getInterface(Class<T> tClass){
        return (T) mPlugins.get(tClass.getName());
    }

    private PluginManager() {

    }
}
