package org.careye.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.activity.PlayFullScreenActivity;
import org.careye.activity.ReplaySearchActivity;
import org.careye.activity.SettingActivity;
import org.careye.adapter.PlaybackListAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.model.TerminalFile;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.GsonUtil;
import org.careye.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 回放tab
 * */
public class ReplayFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final static int REQUEST_REPLAY_SEARCH = 0x20;

    private TextView mTvTitle;
    private ImageView mIvSearch;
    private ImageView iv_setting;
    private View mRoot;
    private ListView mLvReplayList;

    private PrefBiz prefBiz;
    private List<TerminalFile> fileList;
    private PlaybackListAdapter adapter;

    private DepartmentCar departmentCar;
    private String terminalCurr;
    private String startTime;
    private String endTime;
    private int channelID;
    private int channel;

    private OnFragmentInteractionListener mListener;

    public ReplayFragment() {

    }

    public static ReplayFragment newInstance() {
        ReplayFragment fragment = new ReplayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_replay, container, false);

        initView();
        initListener();
        initData();

        return mRoot;
    }

    private void initView() {
        mTvTitle = mRoot.findViewById(R.id.tv_title);
        mIvSearch = mRoot.findViewById(R.id.iv_search);
        iv_setting = mRoot.findViewById(R.id.iv_setting);
        mLvReplayList = mRoot.findViewById(R.id.lv_replay_file_list);
        prefBiz = new PrefBiz(getActivity());
    }

    private void initListener() {
        mIvSearch.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        mLvReplayList.setOnItemClickListener(this);
    }

    private void initData() {
        if (fileList == null) {
            fileList = new ArrayList<>();
            adapter = new PlaybackListAdapter(getActivity(), fileList);
            mLvReplayList.setAdapter(adapter);

            terminalCurr = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        }
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
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_search: {
                Intent intent = new Intent(getActivity(), ReplaySearchActivity.class);
                startActivityForResult(intent, REQUEST_REPLAY_SEARCH);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), PlayFullScreenActivity.class);
        intent.putExtra("file", fileList.get(i));
        intent.putExtra("departmentCar", departmentCar);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_REPLAY_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    departmentCar = data.getParcelableExtra("departmentCar");
                    terminalCurr = departmentCar.getTerminal();
                    channel = data.getIntExtra("channel", 0);
                    startTime = data.getStringExtra("begTime");
                    endTime = data.getStringExtra("endTime");
//                    int location = data.getIntExtra("location", 0);

                    fileList.clear();
                    adapter.notifyDataSetChanged();

                    showWaitDialog();

                    if (channel == 0) {
                        channelID = 1;
                        queryTerminalFileList(terminalCurr, String.valueOf(channelID));
                    } else {
                        queryTerminalFileList(terminalCurr, String.valueOf(channel));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void queryTerminalFileList(String terminal, String id) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.QueryTerminalFileList request = retrofit.create(CmsRequest.QueryTerminalFileList.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("tradeno", tradeno);
        body.addProperty("username", name);
        body.addProperty("sign", sign);
        body.addProperty("streamType", 0);    //0:主码流 1：子码流
        body.addProperty("terminal", terminal);     // 设备号
        body.addProperty("vedioType", 0);     // 0：音视频 1视频 2 双向对讲 3 监听 4 中心广播 5 透传
        body.addProperty("id", id);                 // 1 通道一 2 通道二 3 通道三 4 通道四
        body.addProperty("memoryType", 0);    // 0：所有存储器 1：主存储器 2：灾备服务器
        body.addProperty("startTime", startTime);  // 开始时间 如：2018-03-17 11:49:51
        body.addProperty("endTime", endTime);

        Call<JsonObject> call = request.queryTerminalFileList(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");

                    for (int i = 0; i < array.size(); i++) {
                        TerminalFile file = GsonUtil.jsonToBean(array.get(i).toString(), TerminalFile.class);
                        fileList.add(file);
                    }

                    if (channel == 0) {
                        int max = departmentCar.getChanneltotals() + 1;
                        if (channelID < max) {
                            channelID++;
                            queryTerminalFileList(terminalCurr, String.valueOf(channelID));
                        } else {
                            adapter.notifyDataSetChanged();
                            dismissWaitDialog();
                        }
                    } else {
                        adapter.notifyDataSetChanged();
                        dismissWaitDialog();
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    dismissWaitDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("ReplayFragment", t.getMessage());

//                adapter.notifyDataSetChanged();
                dismissWaitDialog();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    protected ProgressDialog pd;

    public void dismissWaitDialog() {
        if ((getActivity() != null) && (!getActivity().isFinishing())) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if ((ReplayFragment.this.pd != null) && (ReplayFragment.this.pd.isShowing())) {
                        ReplayFragment.this.pd.dismiss();
                        ReplayFragment.this.pd = null;
                    }
                }
            });
            return;
        }
    }

    public void showWaitDialog() {
        showWaitDialog("查询中", true);
    }

    public void showWaitDialog(String paramString) {
        showWaitDialog(paramString, true);
    }

    public void showWaitDialog(final String paramString, final boolean paramBoolean) {
        if ((getActivity() != null) && (!getActivity().isFinishing())) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if ((ReplayFragment.this.pd == null) || (!ReplayFragment.this.pd.isShowing())) {
                        ReplayFragment.this.pd = new ProgressDialog(ReplayFragment.this.getActivity());
                        ReplayFragment.this.pd.setMessage(paramString);
                        ReplayFragment.this.pd.setCancelable(paramBoolean);
                        ReplayFragment.this.pd.show();
                    }
                }
            });
            return;
        }
    }
}