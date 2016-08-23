package com.mbv.persist.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 07/03/16.
 */
public class StringUtil {

    public static boolean isNullOrEmpty(Object obj){
        return (obj == null || "".equals(obj));
    }

    public static Map<String, List<Long>> stringToMap(String str){
        if(isNullOrEmpty(str)){
            return new HashMap<String, List<Long>>();
        }
        str = str.substring(1, str.length()-1);
        if(isNullOrEmpty(str)){
            return new HashMap<String, List<Long>>();
        }
        String split[] = str.split("],");
        split[split.length-1] = split[split.length-1].substring(0, split[split.length-1].length()-1);
        Map<String, List<Long>> map = new HashMap<String, List<Long>>();
        for(String splitValue : split){
            String[] values = splitValue.split(":");
            String key = values[0];
            List<Long> vals = new ArrayList<Long>();
            values[1] = values[1].substring(1, values[1].length());
            String[] valSplit = values[1].split(",");
            for(String indVal : valSplit){
                if(!StringUtil.isNullOrEmpty(indVal)){
                    vals.add(Long.valueOf(indVal));
                }
            }
            map.put(key.replaceAll("\"", ""), vals);
        }
        return map;
    }
}
