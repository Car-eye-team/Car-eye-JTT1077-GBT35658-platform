package org.careye.request;

import org.careye.CarApplication;
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
        if (retrofit == null) {
            PrefBiz prefBiz = new PrefBiz(CarApplication.getContext());
            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://" + prefBiz.getStringInfo(Constants.PREF_LOGIN_IP, "www.liveoss.com") + ":8902/cmsapi/")
                    .baseUrl("http://" + prefBiz.getStringInfo(Constants.PREF_LOGIN_IP, "www.liveoss.com") + ":8088/cmsapi/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 根据获取retrofit请求类
     *
     * @return
     */
    public synchronized Retrofit getRetrofit(String ip) {
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + ip + ":8902/cmsapi/")
                .baseUrl("http://" + ip + ":8088/cmsapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
