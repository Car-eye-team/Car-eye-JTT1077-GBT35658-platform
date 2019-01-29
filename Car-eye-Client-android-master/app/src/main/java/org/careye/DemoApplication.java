package org.careye;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;


public class DemoApplication extends Application {
    public static final String TAG ="DemoApplication";
    private static Context mComContext = null;
    @Override
    public void onCreate() {

//        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//        SDKInitializer.initialize(this);
        SDKInitializer.initialize(this);

//        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        // 初始化设置
        init();
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    private void init() {
        mComContext = DemoApplication.this;
    }
    public static Context getContext() {
        return mComContext;
    }
    public static Resources getResource() {
        return mComContext.getResources();
    }

}