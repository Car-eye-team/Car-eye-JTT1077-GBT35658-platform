package org.careye.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonObject;

import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.model.TerminalFile;
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

public class PlayFullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mRlTitle;
    private ImageView mIvBack;
    private MediaView mMvPlayer;

    private PrefBiz prefBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_full_screen);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvPlayer.stop();
    }

    private void initView() {
        mRlTitle = findViewById(R.id.lyPlayFullScreenVod_titleCtrl);
        mMvPlayer = findViewById(R.id.mv_player);
        mIvBack = findViewById(R.id.lyPlayFullScreenkVod_ivBack);
    }

    private void initData() {
        TerminalFile file = getIntent().getParcelableExtra("file");
        DepartmentCar departmentCar = getIntent().getParcelableExtra("departmentCar");
        prefBiz = new PrefBiz(this);
        playbackAppoint(departmentCar, file);
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
        mMvPlayer.setOnClickListener(this);
    }

    private void playbackAppoint(DepartmentCar departmentCar, TerminalFile file) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.PlaybackAppoint request = retrofit.create(CmsRequest.PlaybackAppoint.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("endTime", file.getEndTime());
        body.addProperty("id", file.getLogicChannel());
        body.addProperty("memoryType", 0);
        body.addProperty("startTime", file.getStartTime());
        body.addProperty("sign", sign);
        body.addProperty("streamType", 0);
        body.addProperty("terminal", departmentCar.getTerminal());  // 设备号
        body.addProperty("tradeno", tradeno);
        body.addProperty("username", name);
        body.addProperty("vedioType", 0);
        Call<JsonObject> call = request.playbackAppoint(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonObject object = resultObj.getAsJsonObject("resultData");
                    String url = object.get("url").getAsString();
                    mMvPlayer.play(url);

                    // 切换画面的显示模式
                    mMvPlayer.toggleAspectRatio();
                    mMvPlayer.toggleAspectRatio();
                    mMvPlayer.toggleAspectRatio();
//                    mMvPlayer.toggleAspectRatio();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lyPlayFullScreenkVod_ivBack:
                finish();
                break;
            case R.id.mv_player:
                if (mRlTitle.getVisibility() == View.VISIBLE) {
                    mRlTitle.setVisibility(View.GONE);
                } else {
                    mRlTitle.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
