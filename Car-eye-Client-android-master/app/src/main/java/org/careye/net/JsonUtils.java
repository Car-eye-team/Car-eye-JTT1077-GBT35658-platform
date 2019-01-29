/*
JSONUtils工具类，可用于方便的向Json中读取和写入相关类型数据，如：
String getString(JSONObject jsonObject, String key, String defaultValue) 得到string类型value
String getString(String jsonData, String key, String defaultValue) 得到string类型value
表示从json中读取某个String类型key的值

getMap(JSONObject jsonObject, String key) 得到map
getMap(String jsonData, String key) 得到map
表示从json中读取某个Map类型key的值
==============================================================*/
package org.careye.net;



import org.careye.utils.ArrayUtils;
import org.careye.utils.MapUtils;
import org.careye.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Json Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class JsonUtils {

    public static boolean isPrintException = true;

    private JsonUtils() {
        throw new AssertionError();
    }

    /**
     * get Long from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getLong(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getLong(String)}</li>
     *         </ul>
     */
    public static Long getLong(JSONObject jsonObject, String key, Long defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Long from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>

     */
    public static Long getLong(String jsonData, String key, Long defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getLong(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getLong(JSONObject, String, Long)
     */
    public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
        return getLong(jsonObject, key, (Long)defaultValue);
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getLong(String, String, Long)
     */
    public static long getLong(String jsonData, String key, long defaultValue) {
        return getLong(jsonData, key, (Long)defaultValue);
    }

    /**
     * get Int from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getInt(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getInt(String)}</li>
     *         </ul>
     */
    public static Integer getInt(JSONObject jsonObject, String key, Integer defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Int from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     */
    public static Integer getInt(String jsonData, String key, Integer defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getInt(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getInt(JSONObject, String, Integer)
     */
    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        return getInt(jsonObject, key, (Integer)defaultValue);
    }

    /**
     * @param
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getInt(String, String, Integer)
     */
    public static int getInt(String jsonData, String key, int defaultValue) {
        return getInt(jsonData, key, (Integer)defaultValue);
    }

    /**
     * get Double from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getDouble(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getDouble(String)}</li>
     *         </ul>
     */
    public static Double getDouble(JSONObject jsonObject, String key, Double defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Double from jsonData
     * 
     */
    public static Double getDouble(String jsonData, String key, Double defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getDouble(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getDouble(JSONObject, String, Double)
     */
    public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
        return getDouble(jsonObject, key, (Double)defaultValue);
    }

    /**
     * @param
     * @param key
     * @param defaultValue
     * @return
     * @see JsonUtils#getDouble(String, String, Double)
     */
    public static double getDouble(String jsonData, String key, double defaultValue) {
        return getDouble(jsonData, key, (Double)defaultValue);
    }

    /**
     * get String from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getString(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getString(String)}</li>
     *         </ul>
     */
    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     */
    public static String getString(String jsonData, String key, String defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getString(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String from jsonObject
     * 
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if keyArray is null or empty, return defaultValue</li>
     *         <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by recursion, return it. if anyone is
     *         null, return directly</li>
     *         </ul>
     */
    public static String getStringCascade(JSONObject jsonObject, String defaultValue, String... keyArray) {
        if (jsonObject == null || ArrayUtils.isEmpty(keyArray)) {
            return defaultValue;
        }

        String data = jsonObject.toString();
        for (String key : keyArray) {
            data = getStringCascade(data, key, defaultValue);
            if (data == null) {
                return defaultValue;
            }
        }
        return data;
    }

    /**
     * get String from jsonData
     * 
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     *         <li>if jsonData is null, return defaultValue</li>
     *         <li>if keyArray is null or empty, return defaultValue</li>
     *         <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by recursion, return it. if anyone is
     *         null, return directly</li>
     *         </ul>
     */
    public static String getStringCascade(String jsonData, String defaultValue, String... keyArray) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        String data = jsonData;
        for (String key : keyArray) {
            data = getString(data, key, defaultValue);
            if (data == null) {
                return defaultValue;
            }
        }
        return data;
    }

    /**
     * get String array from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getJSONArray(String)} exception, return defaultValue</li>
     *         <li>if {@link JSONArray#getString(int)} exception, return defaultValue</li>
     *         <li>return string array</li>
     *         </ul>
     */
    public static String[] getStringArray(JSONObject jsonObject, String key, String[] defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                String[] value = new String[statusArray.length()];
                for (int i = 0; i < statusArray.length(); i++) {
                    value[i] = statusArray.getString(i);
                }
                return value;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * get String array from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     */
    public static String[] getStringArray(String jsonData, String key, String[] defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringArray(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String list from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getJSONArray(String)} exception, return defaultValue</li>
     *         <li>if {@link JSONArray#getString(int)} exception, return defaultValue</li>
     *         <li>return string array</li>
     *         </ul>
     */
    public static List<String> getStringList(JSONObject jsonObject, String key, List<String> defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < statusArray.length(); i++) {
                    list.add(statusArray.getString(i));
                }
                return list;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * get String list from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception, return defaultValue</li>
     *         <li>return {@link JsonUtils#getStringList(JSONObject, String, List)}</li>
     *         </ul>
     */
    public static List<String> getStringList(String jsonData, String key, List<String> defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringList(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getJSONObject(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getJSONObject(String)}</li>
     *         </ul>
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonData is null, return defaultValue</li>
     *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception, return defaultValue</li>
     *         <li>return {@link JsonUtils#getJSONObject(JSONObject, String, JSONObject)}</li>
     *         </ul>
     */
    public static JSONObject getJSONObject(String jsonData, String key, JSONObject defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONObject(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonObject
     * 
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if keyArray is null or empty, return defaultValue</li>
     *         <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by recursion, return it. if anyone is
     *         null, return directly</li>
     *         </ul>
     */
    public static JSONObject getJSONObjectCascade(JSONObject jsonObject, JSONObject defaultValue, String... keyArray) {
        if (jsonObject == null || ArrayUtils.isEmpty(keyArray)) {
            return defaultValue;
        }

        JSONObject js = jsonObject;
        for (String key : keyArray) {
            js = getJSONObject(js, key, defaultValue);
            if (js == null) {
                return defaultValue;
            }
        }
        return js;
    }

    /**
     * get JSONObject from jsonData
     * 
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     *         <li>if jsonData is null, return defaultValue</li>
     *         <li>if keyArray is null or empty, return defaultValue</li>
     *         <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by recursion, return it. if anyone is
     *         null, return directly</li>
     *         </ul>
     */
    public static JSONObject getJSONObjectCascade(String jsonData, JSONObject defaultValue, String... keyArray) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONObjectCascade(jsonObject, defaultValue, keyArray);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONArray from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getJSONArray(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getJSONArray(String)}</li>
     *         </ul>
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String key, JSONArray defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONArray from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     */
    public static JSONArray getJSONArray(String jsonData, String key, JSONArray defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONArray(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Boolean from jsonObject
     * 
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>return {@link JSONObject#getBoolean(String)}</li>
     *         </ul>
     */
    public static boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Boolean from jsonData
     * 
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception, return defaultValue</li>
     *         <li>return {@link JsonUtils#getBoolean(JSONObject, String, Boolean)}</li>
     *         </ul>
     */
    public static boolean getBoolean(String jsonData, String key, Boolean defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getBoolean(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get map from jsonObject.
     * 
     * @param jsonObject key-value pairs json
     * @param key
     * @return <ul>
     *         <li>if jsonObject is null, return null</li>
     *         <li>return {@link JsonUtils#parseKeyAndValueToMap(String)}</li>
     *         </ul>
     */
    public static Map<String, String> getMap(JSONObject jsonObject, String key) {
        return JsonUtils.parseKeyAndValueToMap(JsonUtils.getString(jsonObject, key, null));
    }

    /**
     * get map from jsonData.
     * 
     * @param jsonData key-value pairs string
     * @param key
     * @return <ul>
     *         <li>if jsonData is null, return null</li>
     *         <li>if jsonData length is 0, return empty map</li>
     *         <li>if jsonData {@link JSONObject#JSONObject(String)} exception, return null</li>
     *         <li>return {@link JsonUtils#getMap(JSONObject, String)}</li>
     *         </ul>
     */
    public static Map<String, String> getMap(String jsonData, String key) {

        if (jsonData == null) {
            return null;
        }
        if (jsonData.length() == 0) {
            return new HashMap<String, String>();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getMap(jsonObject, key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception, put empty value
     * 
     * @param sourceObj key-value pairs json
     * @return <ul>
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if (sourceObj == null) {
            return null;
        }

        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        for (Iterator iter = sourceObj.keys(); iter.hasNext();) {
            String key = (String)iter.next();
            MapUtils.putMapNotEmptyKey(keyAndValueMap, key, getString(sourceObj, key, ""));

        }
        return keyAndValueMap;
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception, put empty value
     * 
     * @param source key-value pairs json
     * @return <ul>
     *         <li>if source is null or source's length is 0, return empty map</li>
     *         <li>if source {@link JSONObject#JSONObject(String)} exception, return null</li>
     *         <li>return {@link JsonUtils#parseKeyAndValueToMap(JSONObject)}</li>
     *         </ul>
     */
    public static Map<String, String> parseKeyAndValueToMap(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(source);
            return parseKeyAndValueToMap(jsonObject);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    /**将JsonObject转换成ResultItem*/
	@SuppressWarnings("unchecked")
	public static HttpResultItem convertJSONObject(JSONObject jsonObj){
		HttpResultItem httpResultItem = new HttpResultItem();
	    if(jsonObj!=null){
	    	//遍历所有的KEY值
    	    Iterator<String> keys = jsonObj.keys();
            while(keys.hasNext()){
                String key = keys.next();
                try{
                	//获取具体对象
                	Object obj = jsonObj.get(key);
                	if(obj!=null){
                		if(obj instanceof JSONObject){
                			//添加属性(递归添加)
                			httpResultItem.addValue(key,convertJSONObject((JSONObject)obj));
                			
                		}else if(obj instanceof JSONArray){
                			//列表对象
                			List<Object> listItems = new ArrayList<Object>();
                			//将JSONArray足个解析
                			JSONArray tempArray = (JSONArray) obj;
                			for(int i = 0; i < tempArray.length() ; i++){
                				Object itempObj = tempArray.get(i); 
                				if(itempObj instanceof JSONObject){
                					//递归添加
                					listItems.add(convertJSONObject(tempArray.getJSONObject(i)));
                				}else {
                					listItems.add(itempObj);
                				}
                			}
                			httpResultItem.addValue(key,listItems);
                			
                		}else{
                			httpResultItem.addValue(key,obj.toString());
                		}
                	}
                }catch(Exception e){
                	e.printStackTrace();
                }
            }
	    }
        return httpResultItem;
	}
	
	/**根据json对象生成ResultItem对象*/
	public static HttpResultItem getResultItemByJson(String context){
        if(null == context) return null;
		HttpResultItem item = new HttpResultItem();
		try {
			//生成jsonObject
			JSONObject jsonObj = new JSONObject(context);
			//转换为统一的ResultItem
			item = convertJSONObject(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
}