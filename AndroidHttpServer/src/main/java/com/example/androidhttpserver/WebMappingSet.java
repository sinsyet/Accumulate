package com.example.androidhttpserver;


import com.example.androidhttpserver.webinfo.WebMapping;

import java.util.HashMap;
import java.util.Map;

public class WebMappingSet {
    private static final String TAG = "WebMappingSet";
    private static Map<String,WebMapping> sSets = new HashMap<>();
    static final String _404 = "/404";
    private static final String INDEX = "/";

    private static Map<Class<? extends AndroidHttpServlet>,AndroidHttpServlet>
        servletMap = new HashMap<>();


    public static WebMapping findMapping(String url_pattern){
        return sSets.get(url_pattern);
    }

    public static void put(String key,WebMapping mapping){
        sSets.put(key, mapping);
    }

    public static AndroidHttpServlet getServlet(Class<? extends AndroidHttpServlet> clazz){
        return servletMap.get(clazz);
    }
}
