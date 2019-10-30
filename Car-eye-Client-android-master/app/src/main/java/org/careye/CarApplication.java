package org.careye;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.careye.CarEyeClient.R;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.careye.activity.MainActivity;
import org.careye.model.DepartmentCar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarApplication extends Application {
    public static final String TAG ="CarApplication";

    private static Context mComContext = null;

    public static List<DepartmentCar> allList = new ArrayList<>();
    public static List<DepartmentCar> onlineList = new ArrayList<>();
    public static HashMap<String, DepartmentCar> deptMap = new HashMap<>();

    @Override
    public void onCreate() {

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        // 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        // 包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        // 初始化设置
        init();

        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    private void init() {
        mComContext = CarApplication.this;

        // 腾讯bugly版本升级,第二个参数就是你的appid
        Bugly.init(mComContext, "2e10f4bf6f", false);
        setBuglyInit();
    }

    public static Context getContext() {
        return mComContext;
    }

    public static Resources getResource() {
        return mComContext.getResources();
    }

    public void setBuglyInit(){
        // 添加可显示弹窗的Activity
        Beta.canShowUpgradeActs.add(MainActivity.class);

//        例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
//        设置是否显示消息通知
        Beta.enableNotification = true;

//        如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
//        设置Wifi下自动下载
        Beta.autoDownloadOnWifi = false;

//        如果你想在Wifi网络下自动下载，可以将这个接口设置为true，默认值为false。
//        设置是否显示弹窗中的apk信息
        Beta.canShowApkInfo = true;

//        如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。
//        关闭热更新能力
        Beta.enableHotfix = true;

        Beta.largeIconId = R.drawable.ic_launcher;
        Beta.smallIconId = R.drawable.ic_launcher;

        // 设置是否显示消息通知
        Beta.enableNotification = true;
    }
}