package org.careye.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 2015-8-19
 * 排序算法  全转成小写排序
 */
public class SortList {

    /**
     *  数组排序
     */
    public static String sortMap(Map<String,String> map,boolean issort) {
        if(map.isEmpty())
            return null;

        Iterator iter = (Iterator)map.keySet().iterator();
        List<String> list = new ArrayList<String>();
        Map<String,String> newMap = new HashMap<>();

        while (iter.hasNext()) {
            String key = iter.next().toString();
            list.add(key.toLowerCase());
            newMap.put(key.toLowerCase(),map.get(key));
        }

        if (issort) {
            Collections.sort(list);
        }

        StringBuffer buffer = new StringBuffer();

        for (String string : list) {
            buffer.append(newMap.get(string));
        }

        return buffer.toString();
    }
}

