package org.project;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    private String cmd;
    private Map<String, String> params = new HashMap<>();

    Rq(String input){
        String[] parts = input.split("\\?");
        this.cmd = parts[0];

        if(parts.length > 1){
            for (String param : parts[1].split("&")){
                String[] keyValue = param.split("=");
                if(keyValue.length == 2){
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    public String getCmd() {
        return cmd;
    }

    public String getParams(String key, String defaultValue) {
        return params.getOrDefault(key, defaultValue);
    }

    public int getIntParam(String key, String defaultValue){
        return Integer.parseInt(params.getOrDefault(key, defaultValue));
    }
}
