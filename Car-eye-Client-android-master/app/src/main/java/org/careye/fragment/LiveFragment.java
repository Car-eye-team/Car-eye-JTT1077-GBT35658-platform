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
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

/**
 * 视频tab
 */
public class LiveFragment extends Fragment implements View.OnClickListener {
    private final String TAG = LiveFragment.class.getSimpleName();

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x10;

    private TextView mTvTitle;
    private ImageView mIvSearch;
    private ImageView iv_setting;
    private View mRoot;

    private RelativeLayout mv_player_rl;
    private MediaView mCurrPlayer;
    private List<MediaView> mMvPlayers = new ArrayList<>();
    private String[] urls = {"", "", "", "", "", "", "", ""};

    private Toolbar toolbar;

    private ImageView mIvPlay;
    private ImageView mIvVoice;
    private ImageView mIvVideo;
    private ImageView mIvRec;
    private ImageView iv_full_screen;

<<<<<<< HEAD
    private String currentURL;// = "rtmp://202.69.69.180:443/webcast/bshdlive-pc";
=======
    private String URL1;
    private String URL2;
    private String URL3;
    private String URL4;
    private String URL = "rtmp://202.69.69.180:443/webcast/bshdlive-pc";
>>>>>>> parent of d1a9b28... fix bug

    private DepartmentCar departmentCar;
    private String terminalCurr;
    private int totalChannels;

    private PrefBiz prefBiz;

    private OnFragmentInteractionListener mListener;
    private boolean isLandscape = false;

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
    public void onResume() {
        super.onResume();

        firstPlay();
    }

    public void firstPlay() {
        String terminal = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        if (TextUtils.isEmpty(terminalCurr) || !terminalCurr.equals(terminal)) {
            terminalCurr = terminal;
            totalChannels = prefBiz.getIntInfo(Constants.PREF_THIS_CURREN_CHANNEL, 0);
            if (totalChannels == 0) {
                totalChannels = 4;
            }
            if (totalChannels > 8) {
                totalChannels = 8;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initMvPlayer();

                    for (int i = 1; i < totalChannels; i++) {
                        playOrStopSend(terminalCurr, "0", String.valueOf(i));
                    }
                }
            }, 400);

//        play(mMvPlayer1, URL);
        }
    }

    public void closeVolume() {
        for (MediaView mediaView : mMvPlayers) {
            mediaView.enableVolume(true);
        }

        if (mCurrPlayer != null) {
            updateMuteView(mCurrPlayer.getMuteState());
        }
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

        mv_player_rl = mRoot.findViewById(R.id.mv_player_rl);

        toolbar = mRoot.findViewById(R.id.live_toolbar);

        prefBiz = new PrefBiz(getActivity());
//        mTvTitle.setText(prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARNUM, getResources().getString(R.string.video_preview)));
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
                isLandscape = !isLandscape;
                if (isLandscape) {
                    showToolBar(false);
                } else {
                    showToolBar(true);
                }

                this.mListener.onFullscreen();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMvPlayers.size() > 0) {
                            // 布局
                            mv_player_rl.removeAllViews();
                            layoutMediaView(null, true);
                        }
                    }
                }, 300);
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
        if (toolbar == null) {
            return;
        }

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
                    for (MediaView mediaView : mMvPlayers) {
                        mediaView.play("");
                        mediaView.stop();
                    }

                    departmentCar = data.getParcelableExtra("device");
                    terminalCurr = departmentCar.getTerminal();

                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARID, terminalCurr);
                    prefBiz.putIntInfo(Constants.PREF_THIS_CURREN_CHANNEL, departmentCar.getChanneltotals());
                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARNUM, departmentCar.getNodeName());
//                    mTvTitle.setText(departmentCar.getNodeName());

                    if (departmentCar.getCarstatus() == 1 || departmentCar.getCarstatus() == 2) {
                        Toast.makeText(getActivity(), "当前车辆不在线", Toast.LENGTH_SHORT).show();
                    } else {
                        totalChannels = departmentCar.getChanneltotals();
                        totalChannels = (totalChannels > 8 ? 8 : totalChannels);
                        initMvPlayer();

                        for (int i = 1; i < totalChannels; i++) {
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
<<<<<<< HEAD
                                urls[0] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(0), urls[0]);
                                currentURL = urls[0];
=======
                                URL1 = dataObj.get("url").getAsString();
                                play(mMvPlayer1, URL1);
>>>>>>> parent of d1a9b28... fix bug
                                break;
                            case "2":
                                urls[1] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(1), urls[1]);
                                break;
                            case "3":
                                urls[2] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(2), urls[2]);
                                break;
                            case "4":
                                urls[3] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(3), urls[3]);
                                break;
                            case "5":
                                urls[4] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(4), urls[4]);
                                break;
                            case "6":
                                urls[5] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(5), urls[5]);
                                break;
                            case "7":
                                urls[6] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(6), urls[6]);
                                break;
                            case "8":
                                urls[7] = dataObj.get("url").getAsString();
                                play(mMvPlayers.get(7), urls[7]);
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
     *
     * @param mCurrPlayer 当前活动窗口
     */
    private void setRecEnable(MediaView mCurrPlayer) {
        boolean isRec = mCurrPlayer.getRecState();

        if (isRec) {
            mCurrPlayer.enableRec(false, "", "");
            Toast.makeText(getActivity(), "录制停止！", LENGTH_LONG).show();
        } else {
            String fileName = SystemClock.uptimeMillis() + ".mp4";
            mCurrPlayer.enableRec(true, PicUtils.getVideoPath(terminalCurr), fileName);
            Toast.makeText(getActivity(), "录制开始！", LENGTH_LONG).show();
        }

        updateRecView(!isRec);
    }

    /**
     * 设置视频使能
     *
     * @param mCurrPlayer 当前活动窗口
     */
    private void setVideoEnable(MediaView mCurrPlayer) {
        boolean isShow = mCurrPlayer.getShowVideoState();
        mCurrPlayer.enableVideo(!isShow);
        updateVideoView(!isShow);
    }

    /**
     * 设置静音使能
     *
     * @param mCurrPlayer 当前活动窗口
     */
    private void setMuteEnable(MediaView mCurrPlayer) {
        boolean isMute = mCurrPlayer.getMuteState();
        mCurrPlayer.enableVolume(!isMute);
        updateMuteView(!isMute);
    }

    private void release() {
        for (MediaView mediaView : mMvPlayers) {
            mediaView.play("");
            mediaView.stop();
            mediaView.enableRec(false, "", "");
        }

        for (int i = 1; i < totalChannels; i++) {
            playOrStopSend(terminalCurr, "1", String.valueOf(i));
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

<<<<<<< HEAD
    private void initMvPlayer() {
        mMvPlayers.clear();

        for (int i = 0; i < totalChannels; i++) {
            final MediaView mediaView = new MediaView(getActivity());
            mMvPlayers.add(mediaView);

            final int index = i;
            mediaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchSelect(mediaView, index);
                }
            });

            if (i == 0) {
                mediaView.setSelect(true);
                mCurrPlayer = mediaView;
            }
        }

        if (mMvPlayers.size() > 0) {
            // 布局
            mv_player_rl.removeAllViews();
            layoutMediaView(null, true);
        }
    }

    private void layoutMediaView(MediaView mMvPlayer, boolean first) {
        if (mMvPlayer != null) {
            for (int i = 0; i < mMvPlayers.size(); i++) {
                MediaView mediaView = mMvPlayers.get(i);
                mediaView.setVisibility(View.GONE);
            }

            mMvPlayer.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mMvPlayer.setLayoutParams(params);
=======
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
>>>>>>> parent of d1a9b28... fix bug
        } else {
            int size = 10;
            int row = mMvPlayers.size() / 2 + mMvPlayers.size() % 2;

            int w = (mv_player_rl.getMeasuredWidth() - size) / 2;
            int h = (mv_player_rl.getMeasuredHeight() - size * (row - 1)) / row;

            for (int i = 0; i < mMvPlayers.size(); i++) {
                MediaView mediaView = mMvPlayers.get(i);
                mediaView.setVisibility(View.VISIBLE);
                mediaView.setId((i + 10) * 10000);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
                params.topMargin = (h + size) * (i / 2);

                if (i % 2 == 1) {
                    params.addRule(RelativeLayout.RIGHT_OF, (i + 9) * 10000);
                }

                if (first) {
                    mv_player_rl.addView(mediaView, params);
                } else {
                    mediaView.setLayoutParams(params);
                }
            }
<<<<<<< HEAD
        }
    }

    /*
     * 切换播放窗口对应的功能区状态
     * */
    private void switchSelect(MediaView mMvPlayer, int i) {
        currentURL = urls[i];

        if (mMvPlayer.isExpand()) {
            mMvPlayer.setExpand(false);
            layoutMediaView(null, false);
        } else {
            mMvPlayer.setExpand(true);
            layoutMediaView(mMvPlayer, false);
=======
>>>>>>> parent of d1a9b28... fix bug
        }

        mCurrPlayer.setSelect(false);
        mCurrPlayer.enableVolume(true);
        mCurrPlayer = mMvPlayer;
        mCurrPlayer.setSelect(true);
        mCurrPlayer.enableVolume(false);

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
