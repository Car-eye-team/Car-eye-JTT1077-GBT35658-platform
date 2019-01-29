package org.careye.net;

import android.content.res.TypedArray;
import android.os.Handler;
import android.util.Log;

import org.careye.DemoApplication;

import org.careye.utils.Constants;
import org.careye.utils.MD5Util;
import org.careye.utils.SortList;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkhttpNET {

    public static final String TAG = "OkhttpNET";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void postNetgetHttp(String url, String bodyParams, final Handler handler ) {
        Log.i(TAG,TAG+"postNetgetHttp handler:"+handler);
        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Log.i(TAG,TAG+"postNetgetHttp0:"+url+"---bodyParams----:"+bodyParams);
        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = RequestBody.create(JSON, bodyParams);
        Log.i(TAG,TAG+"postNetgetHttp1:"+url+"-body:"+body.toString());
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();
        Log.i(TAG,TAG+"postNetgetHttp2:"+request.toString());

        //创建一个能处理请求数据的操作类
        Call call = client.newCall(request);
        Log.i(TAG,TAG+"postNetgetHttp3:"+url);
        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr =  response.body().string();
                Log.e(TAG, "Call-----:"+responseStr);
                try {
                    String json ="";//  {"errCode":0,"resultMsg":"成功","resultData":"1"} ;
                    JSONObject objStr = new JSONObject(responseStr);
                    int errCode = objStr.getInt("errCode");
                    Log.e(TAG, "Call-----errCode:"+errCode);
                    if(errCode==0){
                        //	成功
                        if(handler != null &&objStr != null){
                            try {
                                handler .obtainMessage(Constants.WHAT_HTTPOK, objStr).sendToTarget();
                                HttpResultItem httpResultItem = JsonUtils.getResultItemByJson(responseStr);
                                System.out.println("后台返回数据:"+httpResultItem.toString());
                                Log.e(TAG, "后台返回数据-----:"+responseStr);
                            } catch ( Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "后台返回数据-----Exception:"+e.toString());
                            }
                        }

                    }else{
                        //	失败
                        if(handler != null){
                            handler .obtainMessage(Constants.WHAT_HTTPEOOOR, response.body().string()).sendToTarget();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(handler != null){
                        handler .obtainMessage(Constants.WHAT_JSON_PARSE_ERROR_B,null).sendToTarget();
                    }
                }
                response.close();


            }
        });
        Log.i(TAG,TAG+"postNetgetHttp4:"+url);
    }

    private static final String[] REQUEST_FLAG = {"#usrId"};
    public static String getRequestJson(Map<String, String> externalMap, int arrayId) {
        TypedArray tArray = DemoApplication.getResource().obtainTypedArray(arrayId);

        Map<String, String> map = new HashMap<String, String>();
        JSONObject jsonobject = new JSONObject(); // 新建一个JSONObject对象
        try {
            for (int i = 0; i < tArray.length(); i++) {
                String str = tArray.getString(i);

                String[] strs = str.split("=");
                Log.i(TAG, "error getRequestJson json md5Str Arrays =  " + Arrays.toString(strs));
                if (strs.length != 2) {
                    return null;
                }
                if (strs[1].charAt(0) == '#') {
                   /* for (int j = 0; j < REQUEST_FLAG.length; j++) {
                        if (REQUEST_FLAG[j].equals(strs[1])) {
                            strs[1] = getRequestFlag(j,modlueType);
                            break;
                        }
                    }*/
                    if (null != externalMap && null != externalMap.get(strs[0])) {
                        strs[1] = externalMap.get(strs[0]);
                    }
                }

                map.put(strs[0], strs[1]);
                jsonobject.put(strs[0], strs[1]);
            }
            //username+password+tradeno

            String md5Str = SortList.sortMap(map,false);//"admin12345620180908180001"
            Log.i(TAG, "error getRequestJson json md5Str =  " + md5Str);
            jsonobject.put("sign", MD5Util.MD5Encode(md5Str, "UTF-8"));
            Log.i(TAG, "error getRequestJson json jsonobject =  " + jsonobject.toString());
        } catch (Exception e) {
            Log.e(TAG, "error getRequestJson json =  " + jsonobject.toString());
            return null;
        }

        Log.e(TAG, "网络请求数据: " + jsonobject.toString());
      //  System.out.println("网络请求数据: " + jsonobject.toString());
        return jsonobject.toString();
    }

    public static void getNetgetHttp() {
        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS)
                .build();
        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url("https://www.baidu.com").build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);

        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e("TAG", Thread.currentThread().getName() + "结果  " + e.toString());
            }

            /**
             * Called when the HTTP response was successfully returned by the remote server. The callback may
             * proceed to read the response body with {@link Response#body}. The response is still live until
             * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
             * consume the response body on another thread.
             * <p>
             * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
             * not necessarily indicate application-layer success: {@code response} may still indicate an
             * unhappy HTTP response code like 404 or 500.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功 //子线程
                // main' thread1
                Log.e("TAG", Thread.currentThread().getName() + "结果  " + response.body().string());


            }
        });

    }

}
