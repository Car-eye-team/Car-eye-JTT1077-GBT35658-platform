package org.careye.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import org.careye.adapter.PlaybackListAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.model.TerminalFile;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReplayFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final static int REQUEST_REPLAY_SEARCH = 0x20;

    private TextView mTvTitle;
    private ImageView mIvSearch;
    private View mRoot;
    private ListView mLvReplayList;

    private PrefBiz prefBiz;
    private List<TerminalFile> fileList;
    private PlaybackListAdapter adapter;

    private DepartmentCar departmentCar;

    private OnFragmentInteractionListener mListener;

    public ReplayFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ReplayFragment newInstance() {
        ReplayFragment fragment = new ReplayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_replay, container, false);
        initView();
        initListener();
        initData();
        return mRoot;
    }

    private void initView() {
        mTvTitle = mRoot.findViewById(R.id.tv_title);
        mIvSearch = mRoot.findViewById(R.id.iv_search);
        mLvReplayList = mRoot.findViewById(R.id.lv_replay_file_list);
        prefBiz = new PrefBiz(getActivity());
    }

    private void initListener() {
        mIvSearch.setOnClickListener(this);
        mLvReplayList.setOnItemClickListener(this);
    }

    private void initData() {
        fileList = new ArrayList<>();
        adapter = new PlaybackListAdapter(getActivity(), fileList);
        mLvReplayList.setAdapter(adapter);
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
            case R.id.iv_search:
                Intent intent = new Intent(getActivity(), ReplaySearchActivity.class);
                startActivityForResult(intent, REQUEST_REPLAY_SEARCH);
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
                    int location = data.getIntExtra("location", 0);
                    int channel = data.getIntExtra("channel", 0);
                    String startTime = data.getStringExtra("begTime");
                    String endTime = data.getStringExtra("endTime");
                    fileList.clear();
                    if (channel == 0) {
                        for (int i = 1; i < departmentCar.getChanneltotals() + 1; i++) {
                            queryTerminalFileList(departmentCar.getTerminal(), String.valueOf(i), startTime, endTime);
                        }
                    } else {
                        queryTerminalFileList(departmentCar.getTerminal(), channel + "", startTime, endTime);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void queryTerminalFileList(String terminal, String id, String startTime, String endTime) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.QueryTerminalFileList request = retrofit.create(CmsRequest.QueryTerminalFileList.class);
        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String sign = MD5Util.MD5Encode(prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "") + prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "") + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("endTime", endTime);
        body.addProperty("id", id);
        body.addProperty("memoryType", 0);
        body.addProperty("startTime", startTime);
        body.addProperty("sign", sign);
        body.addProperty("streamType", 0);
        body.addProperty("terminal", terminal);
        body.addProperty("tradeno", tradeno);
        body.addProperty("username", prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, ""));
        body.addProperty("vedioType", 0);
        Call<JsonObject> call = request.queryTerminalFileList(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");
                    JsonObject object;
                    TerminalFile file;
                    for (int i = 0; i < array.size(); i++) {
                        file = new TerminalFile();
                        object = array.get(i).getAsJsonObject();
                        file.setLogicChannel(object.get("logicChannel").getAsString());
                        file.setStartTime(object.get("startTime").getAsString());
                        file.setEndTime(object.get("endTime").getAsString());
                        file.setSize(object.get("size").getAsLong());
                        file.setMediaType(object.get("mediaType").getAsInt());
                        file.setStreamType(object.get("streamType").getAsInt());
                        file.setMemoryType(object.get("memoryType").getAsInt());
                        addFile(file);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
            }
        });
    }

    private synchronized void addFile(TerminalFile file){
        fileList.add(file);
        adapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}