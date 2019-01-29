/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.careye.fragment.LiveFragment;
import org.careye.fragment.MapFragment;
import org.careye.fragment.ReplayFragment;
import org.careye.fragment.SettingFragment;
import org.careye.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity
        implements OnTabSelectListener,
        MapFragment.OnFragmentInteractionListener,
        LiveFragment.OnFragmentInteractionListener,
        TrackFragment.OnFragmentInteractionListener,
        ReplayFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener
{

    private final String TAG = MainActivity.class.getSimpleName();

    private FrameLayout mFlView;
    private BottomBar mBomMainBar;

    private Fragment mCurrentFragment;
    private MapFragment mMapFragment;
    private LiveFragment mLiveFragment;
    private TrackFragment mTrackFragment;
    private ReplayFragment mReplayFragment;
    private SettingFragment mSettingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = MapFragment.newInstance();
        mLiveFragment = LiveFragment.newInstance();
        mTrackFragment = TrackFragment.newInstance();
        mReplayFragment = ReplayFragment.newInstance();
        mSettingFragment = SettingFragment.newInstance();
        initView();
        initListener();

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
            case R.id.tab_setting:
                switchFragment(mSettingFragment);
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
            // 判断菜单是否关闭
            if (is_closed) {
                long currentTime = System.currentTimeMillis();
                if((currentTime - touchTime) >= waitTime) {

                    Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    touchTime = currentTime;
                }else {
                  //  Constants.BOO_PULL_LOAD = true;
                    finish();
                }

            }else {
               // resideMenu.closeMenu();
            }
            return true;


        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction");
    }
}

