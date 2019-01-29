/*Object工具类，可用于Object常用操作，如：
isEquals(Object actual, Object expected) 比较两个对象是否相等
compare(V v1, V v2) 比较两个对象大小
transformIntArray(int[] source)  Integer 数组转换为int数组
=================================================================================
*/
package org.careye.net;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * Object Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-10-24
 */
public class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError();
    }

    /**
     * compare two object
     * 
     * @param actual
     * @param expected
     * @return <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * null Object to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    /**
     * convert long array to Long array
     * 
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Long array to long array
     * 
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert int array to Integer array
     * 
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Integer array to int array
     * 
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     * 
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable)v1).compareTo(v2));
    }
    
    /**
	 * 判断数据是否为空 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("all")
	public static boolean isEmpty(Object obj){
		boolean flag = true;
		if(obj!=null){
			if(obj instanceof String){
				flag = (obj.toString().trim().length() == 0);
				
			}else if(obj instanceof Collection<?>){
				flag = ((Collection)obj).size() == 0;
				
			}else if(obj instanceof Map){
				flag = ((Map)obj).size() == 0;
				
			}else if(obj instanceof HttpResultItem){
				flag = ((HttpResultItem)obj).getValues().size() == 0;
				
			}else if(obj instanceof Object[]){
				flag = ((Object[])obj).length == 0;
				
			}else{
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 日期转换
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date,String format){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 字符串转换日期
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date parseDate(String dateStr,String formatStr){
		Date result = null;
		try{
			if(dateStr.length() < formatStr.length()){
				dateStr = "0"+dateStr;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			result = sdf.parse(dateStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}