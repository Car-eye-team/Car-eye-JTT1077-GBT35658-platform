/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonObject;

import org.careye.activity.CarTreeActivity;
import org.careye.activity.SettingActivity;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.PicUtils;
import org.careye.widgets.MediaView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

/**
 * 视频tab
 * */
public class LiveFragment extends Fragment implements View.OnClickListener {
    private final String TAG = LiveFragment.class.getSimpleName();

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x10;

    private TextView mTvTitle;
    private ImageView mIvSearch;
    private ImageView iv_setting;
    private View mRoot;

    private MediaView mCurrPlayer;
    private MediaView mMvPlayer1;
    private MediaView mMvPlayer2;
    private MediaView mMvPlayer3;
    private MediaView mMvPlayer4;
    private LinearLayout mv_player_ll;
    private LinearLayout mv_player_ll1;
    private LinearLayout mv_player_ll2;
    private Toolbar toolbar;

    private ImageView mIvPlay;
    private ImageView mIvVoice;
    private ImageView mIvVideo;
    private ImageView mIvRec;
    private ImageView iv_full_screen;

    private String URL1;
    private String URL2;
    private String URL3;
    private String URL4;
    private String URL = "rtmp://202.69.69.180:443/webcast/bshdlive-pc";

    private DepartmentCar departmentCar;
    private String terminalCurr;

    private PrefBiz prefBiz;

    private OnFragmentInteractionListener mListener;

    public LiveFragment() {

    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mTvTitle = mRoot.findViewById(R.id.tv_title);
        mIvSearch = mRoot.findViewById(R.id.iv_search);
        iv_setting = mRoot.findViewById(R.id.iv_setting);
        mIvPlay = mRoot.findViewById(R.id.iv_play);
        mIvVoice = mRoot.findViewById(R.id.iv_voice);
        mIvVideo = mRoot.findViewById(R.id.iv_video);
        mIvRec = mRoot.findViewById(R.id.iv_rec);
        iv_full_screen = mRoot.findViewById(R.id.iv_full_screen);

        mMvPlayer1 = mRoot.findViewById(R.id.mv_player_1);
        mMvPlayer2 = mRoot.findViewById(R.id.mv_player_2);
        mMvPlayer3 = mRoot.findViewById(R.id.mv_player_3);
        mMvPlayer4 = mRoot.findViewById(R.id.mv_player_4);
        mv_player_ll = mRoot.findViewById(R.id.mv_player_ll);
        mv_player_ll1 = mRoot.findViewById(R.id.mv_player_ll1);
        mv_player_ll2 = mRoot.findViewById(R.id.mv_player_ll2);
        toolbar = mRoot.findViewById(R.id.toolbar);

        mMvPlayer1.setSelect(true);
        mCurrPlayer = mMvPlayer1;

        prefBiz = new PrefBiz(getActivity());
        mTvTitle.setText(prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARNUM, getResources().getString(R.string.video_preview)));

        terminalCurr = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        playOrStopSend(terminalCurr, "0", "1");
        playOrStopSend(terminalCurr, "0", "2");
        playOrStopSend(terminalCurr, "0", "3");
        playOrStopSend(terminalCurr, "0", "4");

//        play(mMvPlayer1, URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_live, container, false);

        initView();
        initListener();

        return mRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initListener() {
        mIvSearch.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mIvVideo.setOnClickListener(this);
        mIvRec.setOnClickListener(this);
        iv_full_screen.setOnClickListener(this);

        mMvPlayer1.setOnClickListener(this);
        mMvPlayer2.setOnClickListener(this);
        mMvPlayer3.setOnClickListener(this);
        mMvPlayer4.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
        release();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_search: {
                Intent intent = new Intent(getActivity(), CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
            }
                break;
            case R.id.mv_player_1:
                switchSelect(mMvPlayer1);
                break;
            case R.id.mv_player_2:
                switchSelect(mMvPlayer2);
                break;
            case R.id.mv_player_3:
                switchSelect(mMvPlayer3);
                break;
            case R.id.mv_player_4:
                switchSelect(mMvPlayer4);
                break;
            case R.id.iv_play:
                play(mCurrPlayer, URL1);
                break;
            case R.id.iv_voice:
                setMuteEnable(mCurrPlayer);
                break;
            case R.id.iv_video:
                setVideoEnable(mCurrPlayer);
                break;
            case R.id.iv_rec:
                setRecEnable(mCurrPlayer);
                break;
            case R.id.iv_full_screen: {
                this.mListener.onFullscreen();
            }
                break;
            case R.id.iv_setting: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    public void showToolBar(boolean v) {
        if (v) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mMvPlayer1.play("");
                    mMvPlayer2.play("");
                    mMvPlayer3.play("");
                    mMvPlayer4.play("");

                    mMvPlayer1.stop();
                    mMvPlayer2.stop();
                    mMvPlayer3.stop();
                    mMvPlayer4.stop();

                    departmentCar = data.getParcelableExtra("device");
                    terminalCurr = departmentCar.getTerminal();
                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARID, terminalCurr);
                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARNUM, departmentCar.getNodeName());

                    mTvTitle.setText(departmentCar.getNodeName());

                    if (departmentCar.getCarstatus() == 1 || departmentCar.getCarstatus() == 2) {
                        Toast.makeText(getActivity(), "当前车辆不在线", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 1; i < (departmentCar.getChanneltotals() > 4 ? 4 : departmentCar.getChanneltotals()) + 1; i++) {
                            playOrStopSend(terminalCurr, "0", String.valueOf(i));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void playOrStopSend(String terminal, final String type, final String id) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.PlayOrStopSend request = retrofit.create(CmsRequest.PlayOrStopSend.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("username", name);
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);
        body.addProperty("type", type);
        body.addProperty("terminal", terminal);     // 设备号
        body.addProperty("id", id);                 // 通道号 1至32
        body.addProperty("protocol", "1");  // 0：RTSP 1: RTMP 2:RTP 3： 其他
        body.addProperty("vedioType", 0);   // 0：音视频 1视频 2 双向对讲 3 监听 4 中心广播5 透传
        body.addProperty("streamType", 1);  // 0:主码流 1：子码流

        Call<JsonObject> call = request.playOrStopSend(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                Log.e(TAG, ">>>> " + resultObj.toString());

                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonObject dataObj = resultObj.getAsJsonObject("resultData");
                    if (type.equals("0")) {
                        switch (id) {
                            case "1":
                                URL1 = dataObj.get("url").getAsString();
                                play(mMvPlayer1, URL1);
                                break;
                            case "2":
                                URL2 = dataObj.get("url").getAsString();
                                play(mMvPlayer2, URL2);
                                break;
                            case "3":
                                URL3 = dataObj.get("url").getAsString();
                                play(mMvPlayer3, URL3);
                                break;
                            case "4":
                                URL4 = dataObj.get("url").getAsString();
                                play(mMvPlayer4, URL4);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    /**
     * 设置Rec使能
     * @param mCurrPlayer 当前活动窗口
     */
    private void setRecEnable(MediaView mCurrPlayer) {
        boolean isRec = mCurrPlayer.getRecState();

        if (isRec) {
            mCurrPlayer.enableRec(false, "", "");
            Toast.makeText(getActivity(), "录制停止！", LENGTH_LONG).show();
        } else {
            String fileName =  SystemClock.uptimeMillis() + ".mp4";
            mCurrPlayer.enableRec(true, PicUtils.getVideoPath(terminalCurr), fileName);
            Toast.makeText(getActivity(), "录制开始！", LENGTH_LONG).show();
        }

        updateRecView(!isRec);
    }

    /**
     * 设置视频使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setVideoEnable(MediaView mCurrPlayer) {
        boolean isShow = mCurrPlayer.getShowVideoState();
        mCurrPlayer.enableVideo(!isShow);
        updateVideoView(!isShow);
    }

    /**
     * 设置静音使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setMuteEnable(MediaView mCurrPlayer) {
        boolean isMute = mCurrPlayer.getMuteState();
        mCurrPlayer.enableVolume(!isMute);
        updateMuteView(!isMute);
    }

    private void release() {
        mMvPlayer1.play("");
        mMvPlayer2.play("");
        mMvPlayer3.play("");
        mMvPlayer4.play("");

        mMvPlayer1.stop();
        mMvPlayer2.stop();
        mMvPlayer3.stop();
        mMvPlayer4.stop();

        if (mMvPlayer1.getRecState()) {
            mMvPlayer1.enableRec(false, "", "");
        }
        if (mMvPlayer2.getRecState()) {
            mMvPlayer2.enableRec(false, "", "");
        }
        if (mMvPlayer3.getRecState()) {
            mMvPlayer3.enableRec(false, "", "");
        }
        if (mMvPlayer4.getRecState()) {
            mMvPlayer4.enableRec(false, "", "");
        }

        if (departmentCar != null) {
            for (int i = 1; i < (departmentCar.getChanneltotals() > 4 ? 4 : departmentCar.getChanneltotals()) + 1; i++) {
                playOrStopSend(terminalCurr, "1", String.valueOf(i));
            }
        }
    }

    private void play(MediaView player, String url) {
        boolean isPlaying = player.isPlaying();

        if (isPlaying) {
            player.stop();
        } else {
//            // TODO
//            url = URL;

            player.stop();
            player.play(url);

            // 默认关闭声音
            player.enableVolume(true);
        }

        updatePlayView(!isPlaying);

        //音量状态
        updateMuteView(player.getMuteState());
        //视频显示状态
        updateVideoView(player.getShowVideoState());
        //更新录制状态
        updateRecView(player.getRecState());
    }

    /*
    * 切换播放窗口对应的功能区状态
    * */
    private void switchSelect(MediaView mMvPlayer) {
        if (mMvPlayer == mMvPlayer1) {
            if (mv_player_ll2.getVisibility() == View.VISIBLE) {
                mv_player_ll2.setVisibility(View.GONE);
                mMvPlayer2.setVisibility(View.GONE);
            } else {
                mv_player_ll2.setVisibility(View.VISIBLE);
                mMvPlayer2.setVisibility(View.VISIBLE);
            }
        } else if (mMvPlayer == mMvPlayer2) {
            if (mv_player_ll2.getVisibility() == View.VISIBLE) {
                mv_player_ll2.setVisibility(View.GONE);
                mMvPlayer1.setVisibility(View.GONE);
            } else {
                mv_player_ll2.setVisibility(View.VISIBLE);
                mMvPlayer1.setVisibility(View.VISIBLE);
            }
        } else if (mMvPlayer == mMvPlayer3) {
            if (mv_player_ll1.getVisibility() == View.VISIBLE) {
                mv_player_ll1.setVisibility(View.GONE);
                mMvPlayer4.setVisibility(View.GONE);
            } else {
                mv_player_ll1.setVisibility(View.VISIBLE);
                mMvPlayer4.setVisibility(View.VISIBLE);
            }
        } else {
            if (mv_player_ll1.getVisibility() == View.VISIBLE) {
                mv_player_ll1.setVisibility(View.GONE);
                mMvPlayer3.setVisibility(View.GONE);
            } else {
                mv_player_ll1.setVisibility(View.VISIBLE);
                mMvPlayer3.setVisibility(View.VISIBLE);
            }
        }

        mCurrPlayer.setSelect(false);
        mCurrPlayer = mMvPlayer;
        mCurrPlayer.setSelect(true);

        // 播放状态
        updatePlayView(mCurrPlayer.isPlaying());
        // 音量状态
        updateMuteView(mCurrPlayer.getMuteState());
        // 视频显示状态
        updateVideoView(mCurrPlayer.getShowVideoState());
        // 更新录制状态
        updateRecView(mCurrPlayer.getRecState());
    }

    public void updatePlayView(boolean isPlaying) {
        if (isPlaying) {
            mIvPlay.setImageResource(R.drawable.ic_pause);
        } else {
            mIvPlay.setImageResource(R.drawable.ic_play);
        }
    }

    private void updateVideoView(boolean isVideo) {
        if (isVideo) {
            mIvVideo.setImageResource(R.drawable.ic_video_show);
        } else {
            mIvVideo.setImageResource(R.drawable.ic_video_hide);
        }
    }

    private void updateMuteView(boolean isMute) {
        if (isMute) {
            mIvVoice.setImageResource(R.drawable.ic_no_voice);
        } else {
            mIvVoice.setImageResource(R.drawable.ic_voice);
        }
    }

    private void updateRecView(boolean isRec) {
        if (isRec) {
            mIvRec.setImageResource(R.drawable.ic_recording);
        } else {
            mIvRec.setImageResource(R.drawable.ic_record);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFullscreen();
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }
}
