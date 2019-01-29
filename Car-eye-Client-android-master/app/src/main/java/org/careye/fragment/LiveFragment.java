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
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonObject;

import org.careye.activity.CarTreeActivity;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.widgets.MediaView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class LiveFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "LiveFragment";

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x10;

    private TextView mTvTitle;
    private ImageView mIvSearch;
    private View mRoot;

    private MediaView mCurrPlayer;
    private MediaView mMvPlayer1;
    private MediaView mMvPlayer2;
    private MediaView mMvPlayer3;
    private MediaView mMvPlayer4;

    private ImageView mIvPlay;
    private ImageView mIvVoice;
    private ImageView mIvVideo;
    private ImageView mIvRec;

    private String URL1;
    private String URL2;
    private String URL3;
    private String URL4;
    //    private String URL = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
//    private String URL = "rtmp://www.car-eye.cn:10077/live/15922222222&channel=4";
    private DepartmentCar departmentCar;

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

        mIvPlay = mRoot.findViewById(R.id.iv_play);
        mIvVoice = mRoot.findViewById(R.id.iv_voice);
        mIvVideo = mRoot.findViewById(R.id.iv_video);
        mIvRec = mRoot.findViewById(R.id.iv_rec);

        mMvPlayer1 = mRoot.findViewById(R.id.mv_player_1);
        mMvPlayer2 = mRoot.findViewById(R.id.mv_player_2);
        mMvPlayer3 = mRoot.findViewById(R.id.mv_player_3);
        mMvPlayer4 = mRoot.findViewById(R.id.mv_player_4);

        mMvPlayer1.setSelect(true);
        mCurrPlayer = mMvPlayer1;

        prefBiz = new PrefBiz(getActivity());
        mTvTitle.setText(prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARNUM, getResources().getString(R.string.video_preview)));
        String terminalCurr = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        playOrStopSend(terminalCurr, "0", "1");
        playOrStopSend(terminalCurr, "0", "2");
        playOrStopSend(terminalCurr, "0", "3");
        playOrStopSend(terminalCurr, "0", "4");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        mIvPlay.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mIvVideo.setOnClickListener(this);
        mIvRec.setOnClickListener(this);

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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        mListener = null;
        release();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_search:
                Intent intent = new Intent(getActivity(), CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
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
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    departmentCar = data.getParcelableExtra("device");
                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARID, departmentCar.getTerminalList().get(0));
                    prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARNUM, departmentCar.getNodeName());
                    mTvTitle.setText(departmentCar.getNodeName());
                    if (departmentCar.getCarstatus() == 1 || departmentCar.getCarstatus() == 2) {
                        Toast.makeText(getActivity(), "当前车辆不在线", Toast.LENGTH_SHORT).show();
                    } else {
                        playOrStopSend(departmentCar.getTerminalList().get(0), "0", "1");
                        playOrStopSend(departmentCar.getTerminalList().get(0), "0", "2");
                        playOrStopSend(departmentCar.getTerminalList().get(0), "0", "3");
                        playOrStopSend(departmentCar.getTerminalList().get(0), "0", "4");
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
        String sign = MD5Util.MD5Encode(prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "") + prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "") + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("username", prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, ""));
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);
        body.addProperty("terminal", terminal);
        body.addProperty("type", type);
        body.addProperty("id", id);
        body.addProperty("protocol", "1");
        body.addProperty("vedioType", 0);
        body.addProperty("streamType", 1);
        Call<JsonObject> call = request.playOrStopSend(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
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
                String message = t.getMessage();
            }
        });
    }

    /**
     * 设置Rec使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setRecEnable(MediaView mCurrPlayer) {
        boolean isRec = mCurrPlayer.getRecState();
        if (isRec) {
            mCurrPlayer.enableRec(false, "", "");
            Toast.makeText(getActivity(), "录制停止！", LENGTH_LONG).show();
        } else {
            String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/careye/video/";
            String fileName =  SystemClock.uptimeMillis() + ".mp4";
            mCurrPlayer.enableRec(true, filePath, fileName);
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
        if (departmentCar != null) {
            playOrStopSend(departmentCar.getTerminalList().get(0), "1", "1");
            playOrStopSend(departmentCar.getTerminalList().get(0), "1", "2");
            playOrStopSend(departmentCar.getTerminalList().get(0), "1", "3");
            playOrStopSend(departmentCar.getTerminalList().get(0), "1", "4");
        }
    }

    private void play(MediaView player, String url) {
        boolean isPlaying = player.isPlaying();
        if (isPlaying) {
            player.stop();
        } else {
            player.stop();
            player.play(url);
        }
        updatePlayView(!isPlaying);

        //音量状态
        updateMuteView(player.getMuteState());
        //视频显示状态
        updateVideoView(player.getShowVideoState());
        //更新录制状态
        updateRecView(player.getRecState());
    }

    /**
     * 切换播放窗口对应的功能区状态
     * @param mMvPlayer     切换窗口
     */
    private void switchSelect(MediaView mMvPlayer) {
        mCurrPlayer.setSelect(false);
        mCurrPlayer = mMvPlayer;
        mCurrPlayer.setSelect(true);

        //播放状态
        updatePlayView(mCurrPlayer.isPlaying());
        //音量状态
        updateMuteView(mCurrPlayer.getMuteState());
        //视频显示状态
        updateVideoView(mCurrPlayer.getShowVideoState());
        //更新录制状态
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
    }
}
