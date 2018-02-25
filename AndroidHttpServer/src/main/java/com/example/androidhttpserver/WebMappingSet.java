package com.example.androidhttpserver;


import com.example.androidhttpserver.webinfo.WebMapping;

import java.util.HashMap;
import java.util.Map;

public class WebMappingSet {

    private static Map<String,WebMapping> sSets = new HashMap<>();
    public static final String _404 = "404";
    public static final String INDEX = "/";
    static {
        sSets.put("404",new WebMapping("/404","html/base/404.html"));
    }

    public static WebMapping findMapping(String url_pattern){
        if(!sSets.containsKey(url_pattern)) return sSets.get(_404);

        return sSets.get(url_pattern);
    }

    public static void put(String key,WebMapping mapping){
        sSets.put(key, mapping);
    }

}
