package org.careye.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.adapter.TreeAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.TreeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 车辆树界面
 */
public class TrackSettingActivity extends AppCompatActivity implements OnClickListener , DatePicker.OnDateChangedListener{

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x11;

    private PrefBiz prefBiz;

    private ImageView iv_back;
    private TextView tv_title;


    private Calendar mBegTime;

    private Calendar mDate;
    private AlertDialog mDlgChannel;
    private AlertDialog mDlgLocation;
    private Calendar mEndTime;
    private LinearLayout mLayoutBegTime;
    private LinearLayout mLayoutChannel;
    private LinearLayout mLayoutDate;
    private LinearLayout mLayoutDevice;
    private LinearLayout mLayoutEndTime;
    private LinearLayout mLayoutLocation;
    private RelativeLayout mLayoutSearch;

    private TextView mTvBegTime;
    private TextView mTvChannel;
    private TextView mTvDate;
    private TextView mTvDevice;
    private TextView mTvEndTime;
    private TextView mTvLocation;

    private int year, month, day;

    private DepartmentCar departmentCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_search);
        initView();
        addListener();
        prefBiz = new PrefBiz(this);
    }

    private void initView() {
        iv_back = findViewById(R.id.device_list_iv_back);
        tv_title = findViewById(R.id.tv_title);

        mLayoutDevice = findViewById(R.id.playback_layout_device);
        tv_title.setText("轨迹搜索");


        mLayoutLocation = findViewById(R.id.playback_layout_location);
        mLayoutChannel = findViewById(R.id.playback_layout_channel);
        mLayoutDate = findViewById(R.id.playback_layout_date);
        mLayoutBegTime = findViewById(R.id.playback_layout_begin_time);
        mLayoutEndTime = findViewById(R.id.playback_layout_end_time);
        mLayoutSearch = findViewById(R.id.playback_lySearch);

        mTvDevice = findViewById(R.id.playback_dev_value);
        mTvLocation = findViewById(R.id.playback_loc_value);
        mTvChannel = findViewById(R.id.playback_file_chn_value);
        mTvDate = findViewById(R.id.playback_date_value);
        mTvBegTime = findViewById(R.id.playback_begin_time_value);
        mTvEndTime = findViewById(R.id.playback_end_time_value);
        mDate = Calendar.getInstance();
        mTvDate.setText(DateUtil.dateSwitchDateString(mDate.getTime()));
        (mBegTime = Calendar.getInstance()).set(Calendar.HOUR_OF_DAY, 0);
        mBegTime.set(Calendar.MINUTE, 0);
        mBegTime.set(Calendar.SECOND, 0);
        mTvBegTime.setText(DateUtil.dateSwitchTimeString(mBegTime.getTime()));
        (mEndTime = Calendar.getInstance()).set(Calendar.HOUR_OF_DAY, 23);
        mEndTime.set(Calendar.MINUTE, 59);
        mEndTime.set(Calendar.SECOND, 59);
        mTvEndTime.setText(DateUtil.dateSwitchTimeString(mEndTime.getTime()));


        mLayoutLocation.setVisibility(View.GONE);
        mLayoutChannel.setVisibility(View.GONE);
    }


    private void addListener(){

        mLayoutDevice.setOnClickListener(this);
        mLayoutLocation.setOnClickListener(this);
        mLayoutChannel.setOnClickListener(this);
        mLayoutDate.setOnClickListener(this);
        mLayoutBegTime.setOnClickListener(this);
        mLayoutEndTime.setOnClickListener(this);
        mLayoutSearch.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }
    private void updateData() {

    }

    private void getDeptAndCar() {

    }

    protected void getDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDate.set(year, month, day);
                mTvDate.setText(DateUtil.dateSwitchDateString(mDate.getTime()));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH), this);
    }

    protected void getBeginTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_time, null);
        final TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setCurrentHour(mBegTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(mBegTime.get(Calendar.MINUTE));
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBegTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                mBegTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                mTvBegTime.setText(DateUtil.dateSwitchTimeString(mBegTime.getTime()));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("设置开始时间");
        dialog.setView(dialogView);
        dialog.show();
    }

    protected void getEndTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_time, null);
        final TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setCurrentHour(mEndTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(mEndTime.get(Calendar.MINUTE));
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEndTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                mEndTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                mTvEndTime.setText(DateUtil.dateSwitchTimeString(mEndTime.getTime()));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("设置结束时间");
        dialog.setView(dialogView);
        dialog.show();
    }
    protected void searchTrack() {
        if (departmentCar == null) {
            Toast.makeText(this, R.string.select_terminal_tip, Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.mBegTime.getTimeInMillis() >= this.mEndTime.getTimeInMillis()) {
            Toast.makeText(this, R.string.select_time_tip, Toast.LENGTH_SHORT).show();
            return;
        }


        String  terminal="";//	设备号	String  //13700000007
        String carnumber="粤B12345";//	车牌号	String
         carnumber=departmentCar.getNodeName();//	车牌号	String

        String  startTime="2019-01-27 08:49:51";//	开始时间	String
        startTime=mTvDate.getText().toString()+" "+mTvBegTime.getText().toString();//	开始时间	String

        String  endTime=mTvDate.getText().toString()+" 16:49:51";//	结束时间	String
        endTime=mTvDate.getText().toString()+" "+mTvEndTime.getText().toString();//	结束时间	String

        Intent data = new Intent();
        data.putExtra("extra_trackcar", new String[]{terminal,carnumber,startTime,endTime});
        setResult(RESULT_OK, data);
        TrackSettingActivity.this.finish();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.playback_layout_device:
                Intent intent = new Intent(this, CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
                break;
            case R.id.playback_layout_location:

                break;
            case R.id.playback_layout_channel:

                break;
            case R.id.playback_layout_date:
                getDate();
                break;
            case R.id.playback_layout_begin_time:
                getBeginTime();
                break;
            case R.id.playback_layout_end_time:
                getEndTime();
                break;
            case R.id.device_list_iv_back:
                finish();
                break;
             case R.id.playback_lySearch:
                 searchTrack();


                break;

        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        this.year = i;
        this.month = i1;
        this.day = i2;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    departmentCar = data.getParcelableExtra("device");
                    mTvDevice.setText(departmentCar.getNodeName());
                }
                break;
            default:
                break;
        }
    }
}
