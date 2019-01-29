package org.careye.utils;

public class Tools {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        if(str == null || str.equals("") || str.length()==0 || str.equals("null")){
            return true;
        }
        return false;
    }
    private double getStrTodouble( String a){
        double d= 0.0;
        try {
            d=Double.parseDouble(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }
    private int getStrToInt( String a){
        int d= 0;
        try {
            d=Integer.parseInt(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }
}
