package org.careye.request;

import org.careye.DemoApplication;
import org.careye.bll.PrefBiz;
import org.careye.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求基类
 */
public class BaseRequest {
    private static BaseRequest instance;
    private static Retrofit retrofit;


    private BaseRequest() {
        PrefBiz prefBiz = new PrefBiz(DemoApplication.getContext());
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + prefBiz.getStringInfo(Constants.PREF_LOGIN_IP, "www.liveoss.com") + ":8088/cmsapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized BaseRequest getInstance() {
        if (instance == null) {
            instance = new BaseRequest();
        }
        return instance;
    }

    /**
     * 获取retrofit请求类
     *
     * @return
     */
    public synchronized Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 根据获取retrofit请求类
     *
     * @return
     */
    public synchronized Retrofit getRetrofit(String ip) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + ":8088/cmsapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
