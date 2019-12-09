package org.careye.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.careye.CarEyeClient.R;

import org.careye.model.Alarm;

public class WarnActivity extends AppCompatActivity implements View.OnClickListener {
    private Alarm alarm;

    private ImageView device_list_iv_back;
    private TextView warn_terminal_id_tv;
    private TextView warn_carnumber_tv;
    private TextView warn_alarm_number_tv;
    private TextView warn_alarm_desc_tv;
    private TextView warn_start_time_tv;
    private TextView warn_start_speed_tv;
    private TextView warn_start_lat_lon_tv;
    private TextView warn_start_location_tv;
    private TextView warn_end_time_tv;
    private TextView warn_end_speed_tv;
    private TextView warn_end_lat_lon_tv;
    private TextView warn_end_location_tv;

    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);

        alarm = (Alarm) getIntent().getSerializableExtra("Alarm");

        device_list_iv_back = findViewById(R.id.device_list_iv_back);
        warn_terminal_id_tv = findViewById(R.id.warn_terminal_id_tv);
        warn_carnumber_tv = findViewById(R.id.warn_carnumber_tv);
        warn_alarm_number_tv = findViewById(R.id.warn_alarm_number_tv);
        warn_alarm_desc_tv = findViewById(R.id.warn_alarm_desc_tv);
        warn_start_time_tv = findViewById(R.id.warn_start_time_tv);
        warn_start_speed_tv = findViewById(R.id.warn_start_speed_tv);
        warn_start_lat_lon_tv = findViewById(R.id.warn_start_lat_lon_tv);
        warn_start_location_tv = findViewById(R.id.warn_start_location_tv);
        warn_end_time_tv = findViewById(R.id.warn_end_time_tv);
        warn_end_speed_tv = findViewById(R.id.warn_end_speed_tv);
        warn_end_lat_lon_tv = findViewById(R.id.warn_end_lat_lon_tv);
        warn_end_location_tv = findViewById(R.id.warn_end_location_tv);

        device_list_iv_back.setOnClickListener(this);

        warn_terminal_id_tv.setText(alarm.getTerminalId());
        warn_carnumber_tv.setText(alarm.getCarNumber());
        warn_alarm_number_tv.setText(alarm.getAlarmNumber());
        warn_alarm_desc_tv.setText(alarm.getAlarmDesc());
        warn_start_time_tv.setText(alarm.getStartTime());
        warn_start_speed_tv.setText(alarm.getStartSpeed() + "km/h");
        warn_start_lat_lon_tv.setText(alarm.getStartLon() + ", " + alarm.getStartLat());
        warn_start_location_tv.setText(alarm.getStartLocation());
        warn_end_time_tv.setText(alarm.getEndTime());
        warn_end_speed_tv.setText(alarm.getEndSpeed() + "km/h");
        warn_end_lat_lon_tv.setText(alarm.getEndLon() + ", " + alarm.getEndLat());
        warn_end_location_tv.setText(alarm.getEndLocation());

        initGeoCoder();

        if (alarm.getStartLat().equals("--") || alarm.getStartLon().equals("--")) {

            if (!alarm.getEndLat().equals("--") && !alarm.getEndLon().equals("--")) {
                lat = Double.parseDouble(alarm.getEndLat());
                lng = Double.parseDouble(alarm.getEndLon());
                search(new LatLng(lat, lng));
            }

        } else {
            lat = Double.parseDouble(alarm.getStartLat());
            lng = Double.parseDouble(alarm.getStartLon());
            search(new LatLng(lat, lng));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.device_list_iv_back:
                finish();
                break;
        }
    }

    /**
     * 检索经纬度所在地址
     * @param latLng 经纬度信息
     */
    private void search(LatLng latLng) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    private GeoCoder geoCoder;

    private void initGeoCoder() {
        if (geoCoder != null) {
            return;
        }

        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
//                    return;
                }
//                ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();

                String address = reverseGeoCodeResult.getAddress();
                if (TextUtils.isEmpty(address)) {
                    address = "";
                }

                if (lat == Double.parseDouble(alarm.getStartLat()) &&
                        lng == Double.parseDouble(alarm.getStartLon())) {
                    alarm.setStartLocation(address);
                    warn_start_location_tv.setText(alarm.getStartLocation());

                    if (!alarm.getEndLat().equals("--") && !alarm.getEndLon().equals("--")) {
                        lat = Double.parseDouble(alarm.getEndLat());
                        lng = Double.parseDouble(alarm.getEndLon());
                        search(new LatLng(lat, lng));
                    }
                } else {
                    alarm.setEndLocation(address);
                    warn_end_location_tv.setText(alarm.getEndLocation());
                }
            }
        });
    }
}
