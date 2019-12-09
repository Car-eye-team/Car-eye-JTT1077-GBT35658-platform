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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.careye.CarEyeClient.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.activity.SettingActivity;
import org.careye.activity.TrackSettingActivity;
import org.careye.adapter.WarnListAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.Alarm;
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

public class WarnFragment extends Fragment implements View.OnClickListener {
    private final String TAG = TrackFragment.class.getSimpleName();
    public final static int REQUEST_SELECT_DEVICE_CODE = 0x10;

    private ImageView mImageView;
    private ImageView iv_setting;
    private ListView mLvReplayList;
    private View footer;

    private WarnFragment.OnFragmentInteractionListener mListener;

    private String[] departmentCar;
    private PrefBiz prefBiz;
    private int currentPage = 1;

    private WarnListAdapter adapter;
    private List<Alarm> alarms;
    private boolean loadFinishFlag = true;

    public WarnFragment() {

    }

    public static WarnFragment newInstance() {
        WarnFragment fragment = new WarnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false);
        View view  = inflater.inflate(R.layout.fragment_warn, container, false);

        mLvReplayList = view.findViewById(R.id.lv_replay_file_list);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        mImageView = (ImageView) view.findViewById(R.id.iv_search);

        footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null);

        mLvReplayList.addFooterView(footer);
        mLvReplayList.setOnScrollListener(new ScrollListener());
        mLvReplayList.removeFooterView(footer);

        iv_setting.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        initData();

        return view;
    }

    private void initData() {
        prefBiz = new PrefBiz(getActivity());

        if (alarms == null) {
            alarms = new ArrayList<>();
            adapter = new WarnListAdapter(getActivity(), alarms);
            mLvReplayList.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof WarnFragment.OnFragmentInteractionListener) {
            mListener = (WarnFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.iv_search:
                Intent intent = new Intent(getActivity(), TrackSettingActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
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
                    departmentCar = data.getStringArrayExtra("extra_trackcar");

                    if (departmentCar != null && departmentCar.length == 4) {
                        currentPage = 1;
                        alarms.clear();
                        showWaitDialog();
                        queryAlarmList(departmentCar);
                    } else {
                        Log.i(TAG,"extra_trackcan  null:");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void queryAlarmList(String[] departmentCar) {
        if (departmentCar == null) {
            loadFinishFlag = true;
            mLvReplayList.removeFooterView(footer);
            return;
        }

        String terminal = departmentCar[0];   //	设备号	String
        final String carNumber = departmentCar[1];  //	车牌号	String
        String startTime = departmentCar[2];  //	开始时间	String
        String endTime = departmentCar[3];    //	结束时间	String

        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.QueryAlarmList request = retrofit.create(CmsRequest.QueryAlarmList.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("terminal", terminal);
        body.addProperty("carnumber", carNumber);
        body.addProperty("startTime", startTime);
        body.addProperty("endTime", endTime);
        body.addProperty("tradeno", tradeno);
        body.addProperty("username", name);
        body.addProperty("sign", sign);
        body.addProperty("everyPage", 10);
        body.addProperty("currentPage", currentPage++);

        Call<JsonObject> call = request.queryAlarmList(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonObject object = resultObj.getAsJsonObject("resultData");
                    JsonArray jsonArray = object.getAsJsonArray("list");

                    if (jsonArray != null && jsonArray.size() != 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            Alarm alarm = GsonUtil.jsonToBean(jsonArray.get(i).toString(), Alarm.class);
                            alarm.setCarNumber(carNumber);

                            alarms.add(alarm);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    dismissWaitDialog();
                    loadFinishFlag = true;
                    mLvReplayList.removeFooterView(footer);
                } else {
                    adapter.notifyDataSetChanged();
                    dismissWaitDialog();
                    loadFinishFlag = true;
                    mLvReplayList.removeFooterView(footer);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissWaitDialog();
                loadFinishFlag = true;
                mLvReplayList.removeFooterView(footer);
            }
        });
    }

    protected ProgressDialog pd;

    public void dismissWaitDialog() {
        if ((getActivity() != null) && (!getActivity().isFinishing())) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if ((WarnFragment.this.pd != null) && (WarnFragment.this.pd.isShowing())) {
                        WarnFragment.this.pd.dismiss();
                        WarnFragment.this.pd = null;
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
                    if ((WarnFragment.this.pd == null) || (!WarnFragment.this.pd.isShowing())) {
                        WarnFragment.this.pd = new ProgressDialog(WarnFragment.this.getActivity());
                        WarnFragment.this.pd.setMessage(paramString);
                        WarnFragment.this.pd.setCancelable(paramBoolean);
                        WarnFragment.this.pd.show();
                    }
                }
            });
            return;
        }
    }

    public final class ScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.i(TAG, "---->" + scrollState);
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    break;
                case SCROLL_STATE_TOUCH_SCROLL:
                    break;
                case SCROLL_STATE_FLING:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //获取屏幕最后Item的ID
            int lastVisibleItem = mLvReplayList.getLastVisiblePosition();
            if (lastVisibleItem < 9) {
                return;
            }

            if (lastVisibleItem + 1 == totalItemCount) {
                if (loadFinishFlag) {
                    loadFinishFlag = false;//标志位，防止多次加载
                    mLvReplayList.addFooterView(footer);

                    queryAlarmList(departmentCar);
                }
            }
        }
    }
}
