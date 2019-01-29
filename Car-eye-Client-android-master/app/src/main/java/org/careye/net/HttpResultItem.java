package org.careye.net;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 请求取到的数据
 * @author
 *
 */
public class HttpResultItem implements Serializable{

    private static final long serialVersionUID = 4288348598571503031L;

    /**基本的原始数据*/
    protected Map<String,Object> values = new HashMap<String,Object>();

    public HttpResultItem(){
    }

    public HttpResultItem(Map<String,Object> valuetemp){
        setValues(valuetemp);
    }

    /**注入数据*/
    public void addValue(String key,Object obj){
        values.put(key.toUpperCase(), obj);
    }

    /**覆盖内容*/
    public void setValues(Map<String,Object> valuetemp){
        values = valuetemp;
    }

    /**追加相关属性*/
    public void appendValues(HttpResultItem item){
        if(item!=null){
            appendValues(item.getValues());
        }
    }

    /**追加相关属性*/
    public void appendValues(Map<String,Object> valuetemp){
        if(!ObjectUtils.isEmpty(valuetemp)){
            for(Entry<String,Object> temp: valuetemp.entrySet()){
                addValue(temp.getKey(), temp.getValue());
            }
        }
    }

    public Map<String,Object> getValues(){
        return values;
    }

    public String getString(String key){
        Object value = getValue(key.toUpperCase());
        String message = "";
        if(value != null){
            message = value.toString();
            if("null".equals(message.trim())){
                message = "";
            }
        }
        return message;
    }

    /**支持类似XPATH进行子元素查找路径获取结果集*/
    public Object getValue(String key){
        Object value = null;
        try{
            if(!ObjectUtils.isEmpty(key)){
                String[] pathNames = key.split("\\|");
                if(pathNames.length == 1){
                    value = values.get(key.toUpperCase());
                }else{
                    value = getValueByPath(key);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return value;
    }

    /**直接返回集合对象*/
    @SuppressWarnings("unchecked")
    public List<HttpResultItem> getItems(String key){
        List<HttpResultItem> items = null;
        try {
            Object value = getValue(key);
            if(value instanceof List){
                items = (List<HttpResultItem>) value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**直接返回元素对象*/
    public HttpResultItem getItem(String key){
        HttpResultItem item = null;
        try {
            item = (HttpResultItem)getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 返回日期对象
     * @return
     */
    public Date getDate(String key){
        return getDate(key, "yyyy-MM-dd");
    }

    /**
     * 返回日期对象
     * @return
     */
    public Date getDate(String key,String formatstr){
        formatstr = ObjectUtils.isEmpty(formatstr) ? "yyyy-MM-dd" : formatstr;
        return ObjectUtils.parseDate(getString(key), formatstr);
    }

    /**
     * 数据为空时返回零
     * @return
     */
    public int getIntValue(String key){
        int i = 0;
        try{
            Number number = new BigDecimal(getString(key));
            i = number.intValue();
        }catch(Exception e){
        }
        return i;
    }

    /**
     * 数据为空时犯返回零
     * @return
     */
    public float getFloatValue(String key){
        float f = 0;
        try{
            Number number = new BigDecimal(getString(key));
            f = number.floatValue();
        }catch(Exception e){
        }
        return f;
    }

    /**
     * 数据为空时犯返回零
     * @return
     */
    public double getDoubleValue(String key){
        double d = 0;
        try{
            d = Double.valueOf(getString(key));
        }catch(Exception e){
        }
        return d;
    }

    /**
     * 获取boolean值
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key){
        boolean b = false;
        try{
            b = Boolean.valueOf(getString(key));
        }catch(Exception e){

        }
        return b;
    }

    /***
     * 将key对应的值直接与传入的值做比较
     * @param key
     * @param compareValue
     * @return
     */
    public boolean getBooleanValue(String key,int compareValue){
        boolean b = false;
        try{
            int a = getIntValue(key);
            b = a == compareValue;
        }catch(Exception e){

        }
        return b;
    }
    /***
     * 将key对应的值直接与传入的值做比较
     * @param key
     * @param compareValue
     * @return
     */
    public boolean getBooleanValue(String key,float compareValue){
        boolean b = false;
        try{
            float a = getFloatValue(key);
            b = a == compareValue;
        }catch(Exception e){

        }
        return b;
    }
    /***
     * 将key对应的值直接与传入的值做比较
     * @param key
     * @param compareValue
     * @return
     */
    public boolean getBooleanValue(String key,double compareValue){
        boolean b = false;
        try{
            double a = getDoubleValue(key);
            b = a == compareValue;
        }catch(Exception e){

        }
        return b;
    }
    /***
     * 将key对应的值直接与传入的值做比较
     * @param key
     * @param compareValue
     * @return
     */
    public boolean getBooleanValue(String key,String compareValue){
        boolean b = false;
        try{
            String a = getString(key);
            b = a.equals(compareValue);
        }catch(Exception e){

        }
        return b;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public void clear(){
        values.clear();
    }

    /**根据类似XPATH进行子元素查找路径获取结果集*/
    protected Object getValueByPath(String paths) {
        Object resulitems = null;
        if(!ObjectUtils.isEmpty(paths)){
            String[] pathNames = paths.split("\\|");
            if(!ObjectUtils.isEmpty(pathNames)){
                HttpResultItem currentItem = this;
                for(int i = 0 ;i<pathNames.length;i++){
                    resulitems = currentItem.getValue(pathNames[i]);
                    if(i!=pathNames.length-1){
                        if(resulitems instanceof HttpResultItem){
                            currentItem = (HttpResultItem) resulitems;
                        }else{
                            break;
                        }
                    }
                }
            }
        }
        return resulitems;
    }

    /**搜索一个对象*/
    public HttpResultItem searchItem(String rootKey, String queryKey, String value){
        HttpResultItem item = null;
        try{
            Object obj = getValue(rootKey);
            if(obj!=null){
                if(obj instanceof HttpResultItem){
                    String temp = ((HttpResultItem) obj).getString(queryKey);
                    if(!ObjectUtils.isEmpty(temp) && temp.equals(value)){
                        item = (HttpResultItem) obj;
                    }
                }else if(obj instanceof List<?>){
                    List<?> resuls = (List<?>) obj;
                    if(!ObjectUtils.isEmpty(resuls)){
                        for(Object tempObj :resuls){
                            HttpResultItem tempItem = (HttpResultItem) tempObj;
                            String temp = tempItem.getString(queryKey);
                            if(!ObjectUtils.isEmpty(temp) && temp.equals(value)){
                                item = (HttpResultItem) tempItem;
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
        }
        return item;
    }
}

