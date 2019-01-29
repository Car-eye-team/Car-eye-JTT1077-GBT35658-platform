package org.careye.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.careye.CarEyeClient.R;

import org.careye.model.DepartmentCar;
import org.careye.utils.DateUtil;

import java.util.Calendar;

public class ReplaySearchActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x10;

    private Calendar mBegTime;
    private int mChannel = -1;
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
    private int mLocation = -1;
    private TextView mTvBegTime;
    private TextView mTvChannel;
    private TextView mTvDate;
    private TextView mTvDevice;
    private TextView mTvEndTime;
    private TextView mTvLocation;
    private ImageView device_list_iv_back;

    private int year, month, day;

    private DepartmentCar departmentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_search);
        initView();
        initListener();
    }

    private void initView() {
        mLayoutDevice = findViewById(R.id.playback_layout_device);
        device_list_iv_back = findViewById(R.id.device_list_iv_back);
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
    }

    private void initListener() {
        mLayoutDevice.setOnClickListener(this);
        mLayoutLocation.setOnClickListener(this);
        mLayoutChannel.setOnClickListener(this);
        mLayoutDate.setOnClickListener(this);
        mLayoutBegTime.setOnClickListener(this);
        mLayoutEndTime.setOnClickListener(this);
        mLayoutSearch.setOnClickListener(this);
        device_list_iv_back.setOnClickListener(this);
    }

    private void getLocation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.playback_location);
        int n = 0;
        builder.setSingleChoiceItems((CharSequence[])new String[] { this.getString(R.string.playback_loc_device), this.getString(R.string.playback_loc_server) }, n, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (n == 0) {
                    mTvLocation.setText(R.string.playback_loc_device);
                }
                else {
                    mTvLocation.setText(R.string.playback_loc_server);
                }
                mLocation = n;
                mDlgLocation.dismiss();
            }
        });
        mDlgLocation = builder.create();
        mDlgLocation.show();
    }

    protected void getChannel() {
        if (departmentCar == null) {
            Toast.makeText(this, R.string.select_terminal_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.playback_channel);
        final String[] array = new String[]{"所有", "CH1", "CH2", "CH3", "CH4"};
        int n = 0;
        builder.setSingleChoiceItems((CharSequence[])array, n, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                mChannel = n;
                mTvChannel.setText(array[n]);
                mDlgChannel.dismiss();
            }
        });
        (this.mDlgChannel = builder.create()).show();
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
        if (mLocation == -1) {
            Toast.makeText(this, "请选择文件位置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mChannel == -1) {
            Toast.makeText(this, "请选择通道", Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.mBegTime.getTimeInMillis() >= this.mEndTime.getTimeInMillis()) {
            Toast.makeText(this, R.string.select_time_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("departmentCar", departmentCar);
        intent.putExtra("location", mLocation);
        intent.putExtra("channel", mChannel);
        intent.putExtra("begTime", mTvDate.getText().toString() + " " + mTvBegTime.getText().toString());
        intent.putExtra("endTime", mTvDate.getText().toString() + " " + mTvEndTime.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.device_list_iv_back:
                finish();
                break;
            case R.id.playback_layout_device:
                Intent intent = new Intent(this, CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
                break;
            case R.id.playback_layout_location:
                getLocation();
                break;
            case R.id.playback_layout_channel:
                getChannel();
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
            case R.id.playback_lySearch:
                searchTrack();
                break;
            default:
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
