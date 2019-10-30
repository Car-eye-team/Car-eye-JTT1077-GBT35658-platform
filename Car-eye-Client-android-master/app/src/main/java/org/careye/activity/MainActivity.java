/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.careye.CarApplication;
import org.careye.bll.PrefBiz;
import org.careye.fragment.LiveFragment;
import org.careye.fragment.MapFragment;
import org.careye.fragment.ReplayFragment;
import org.careye.fragment.TrackFragment;
import org.careye.fragment.WarnFragment;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.GsonUtil;
import org.careye.utils.MD5Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity
        implements OnTabSelectListener,
        MapFragment.OnFragmentInteractionListener,
        LiveFragment.OnFragmentInteractionListener,
        TrackFragment.OnFragmentInteractionListener,
        ReplayFragment.OnFragmentInteractionListener,
        WarnFragment.OnFragmentInteractionListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private BottomBar mBomMainBar;

    private MapFragment mMapFragment;
    private LiveFragment mLiveFragment;
    private TrackFragment mTrackFragment;
    private ReplayFragment mReplayFragment;
    private WarnFragment mWarnFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = MapFragment.newInstance();
        mLiveFragment = LiveFragment.newInstance();
        mTrackFragment = TrackFragment.newInstance();
        mReplayFragment = ReplayFragment.newInstance();
        mWarnFragment = WarnFragment.newInstance();

        mLiveFragment.setmListener(this);

        initView();
        initListener();

        getDeptAndCar();
    }

    private void initListener() {
        mBomMainBar.setOnTabSelectListener(this);
    }

    private void initView() {
        mBomMainBar = findViewById(R.id.bom_main_bar);
        mBomMainBar.selectTabAtPosition(0);
    }

    private void switchFragment(Fragment targetFragment) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_view, targetFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTabSelected(int tabId) {
        switch (tabId) {
            case R.id.tab_map:
                switchFragment(mMapFragment);
                break;
            case R.id.tab_live:
                switchFragment(mLiveFragment);
                break;
            case R.id.tab_track:
                switchFragment(mTrackFragment);
                break;
            case R.id.tab_replay:
                switchFragment(mReplayFragment);
                break;
            case R.id.tab_warn:
                switchFragment(mWarnFragment);
                break;
            default:
                break;
        }
    }

    private boolean is_closed = true;
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

            if (isLandscape()) {
                mLiveFragment.showToolBar(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                return true;
            }

            // 判断菜单是否关闭
            if (is_closed) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - touchTime) >= waitTime) {
                    Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    touchTime = currentTime;
                } else {
                    finish();
                }
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction");
    }

    @Override
    public void onFullscreen() {
        if (isLandscape()) {
            mLiveFragment.showToolBar(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        } else {
            mLiveFragment.showToolBar(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mBomMainBar.setVisibility(View.GONE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mBomMainBar.setVisibility(View.VISIBLE);
        }
    }

    // 是否横屏
    private boolean isLandscape() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == ORIENTATION_LANDSCAPE;
    }

    private void getDeptAndCar() {
        PrefBiz prefBiz = new PrefBiz(this);

        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.DeptTree request = retrofit.create(CmsRequest.DeptTree.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("username", name);
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);

        Call<JsonObject> call = request.deptTree(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();

                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");

                    List<DepartmentCar> allList = new ArrayList<>();
                    List<DepartmentCar> onlineList = new ArrayList<>();
                    HashMap<String, DepartmentCar> deptMap = new HashMap<>();

                    for (int i = 0; i < array.size(); i++) {
                        DepartmentCar departmentCar = GsonUtil.jsonToBean(array.get(i).toString(), DepartmentCar.class);

                        deptMap.put(departmentCar.getNodeId(), departmentCar);
                        allList.add(departmentCar);

                        if (departmentCar.getNodetype() == 1 ||
                                (departmentCar.getCarstatus() != 1
                                        && departmentCar.getCarstatus() != 2
                                        && departmentCar.getCarstatus() != 3)) {
                            onlineList.add(departmentCar);
                        }
                    }

                    CarApplication.deptMap.clear();
                    CarApplication.allList.clear();
                    CarApplication.onlineList.clear();

                    CarApplication.deptMap.putAll(deptMap);
                    CarApplication.allList.addAll(allList);
                    CarApplication.onlineList.addAll(onlineList);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}

