package org.careye.utils;

import android.util.Log;

import org.careye.model.DepartmentCar;

import java.util.HashMap;

/**
 * Created by Yearn on 2018/7/27.
 */

public class TreeUtils {
    //第一级别为0
    public static int getLevel(DepartmentCar departmentCar, HashMap<String,DepartmentCar> map){
        if(!map.containsKey(departmentCar.getParentId())){
            return 0;
        }else{
            return 1+getLevel(getDeptCar(departmentCar.getParentId(),map),map);
        }
    }

    public static DepartmentCar getDeptCar(String ID, HashMap<String,DepartmentCar> map){
        if(map.containsKey(ID)){
            return map.get(ID);
        }
        Log.e("xlc","ID:" + ID);
        return null;
    }
}
