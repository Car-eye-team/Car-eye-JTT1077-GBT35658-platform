package org.careye.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.activity.CarTreeActivity;
import org.careye.activity.SettingActivity;
import org.careye.bll.PrefBiz;
import org.careye.model.CarInfoGPS;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.CarStatusUtil;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.GsonUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 地图tab
 * */
public class MapFragment extends Fragment implements View.OnClickListener, OnGetRoutePlanResultListener {
    private final String TAG = MapFragment.class.getSimpleName();

    public final static int REQUEST_SELECT_DEVICE_CODE = 0x11;

    private OnFragmentInteractionListener mListener;

    private Handler handler;
    private PrefBiz prefBiz;

    private AlertDialog alertDialog2; // 单选框
    private ImageView mImageView;
    private ImageView iv_setting;
    private View popView;
    private ImageView tvSelectTime;
    private TextView tv_car_state_map;
    private TextView tv_location_time_map;
    private TextView tv_car_num_map;
    private TextView tv_distance_map;
    private TextView tv_sys_time_map;
    private RoutePlanSearch mSearch;
    private LinearLayout ll_refresh;
    private TextView tv_timerefresh, tv_terminal, tv_timeregps;

    String terminalCurr;
    private int selectSingleAlertDialogIndex;

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private InfoWindow mInfoWindow;
    private InfoWindow mInfoWindowS;
    private Marker marker;
    private Marker markerCurr;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    boolean isFirstSecond = true;
    boolean isFirstLoc = true;  // 是否首次定位
    CarInfoGPS carGpsInfo = null;
    TimeCount mTimeCount = null;

    public MapFragment() {

    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int what = msg.what;
                if (what == Constants.WHAT_GET_CAR_LAST_POSITION) {
                    Bundle bundle = msg.getData();
                    carGpsInfo = (CarInfoGPS) bundle.getSerializable("arraylgpsinfo");

                    if (carGpsInfo != null) {
                        prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARID, carGpsInfo.getTerminal());

                        // 已经来 获取的车辆最后位置 的 数据
//                        tv_terminal.setText(carGpsInfo.getTerminal());
                        tv_timeregps.setText(carGpsInfo.gpstimeDesc());

                        // 获取数据后 赋值
                        // 位置
                        latLng = new LatLng(getStrToDouble(carGpsInfo.getBlat()), getStrToDouble(carGpsInfo.getBlng()));  // 一进来 car的最后位置
                        search(latLng);
                    } else {
//                       ToastUtil.showToast(CarLocation.this, "亲，没有数据哦！");
//                        if (llCurr != null) {
//                            gocar(llCurr);
//                        }
//                        iv_my_car_location.setBackgroundResource(R.drawable.my_location);
//                        isLocationCarOrMy = false;
                    }
                } else if (what == Constants.WHAT_UPDATE_DATA) {

                }
            }};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mImageView = (ImageView) view.findViewById(R.id.iv_search);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        ll_refresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
        tv_timerefresh = (TextView) view.findViewById(R.id.tv_timerefresh);
        tv_terminal = (TextView) view.findViewById(R.id.tv_terminal);
        tv_timeregps = (TextView) view.findViewById(R.id.tv_timeregps);

        mImageView.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);

        initMap(view);
        initGeoCoder();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        } else {
            Log.i("ACCESS_FINE_LOCATION", "已开启定位权限");
        }

        prefBiz = new PrefBiz(getActivity());

        String terminal = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        if (TextUtils.isEmpty(terminal)) {
            Toast.makeText(getActivity(), "当前没有选定车", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();

        super.onResume();

        isFirstSecond = true;
        isFirstLoc = true;

        tv_terminal.setText("");
        tv_timeregps.setText("");

        String terminal = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        if (TextUtils.isEmpty(terminalCurr) || !terminalCurr.equals(terminal)) {
            terminalCurr = terminal;

            int refresh = prefBiz.getIntInfo(Constants.PREF_MAP_REFRESH, 0);
            mTimeCount = new TimeCount(getTimeCount(refresh), 1000);
            mTimeCount.start();
        }
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();

        super.onPause();

        if (mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            // 退出时销毁定位
            mLocClient.stop();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();

            if (mTimeCount != null) {
                mTimeCount.cancel();
                mTimeCount =null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 200://刚才的识别码
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//用户同意权限,执行我们的操作
                    Log.i("ACCESS_FINE_LOCATION", "已开启定位权限");
                } else {
                    Toast.makeText(getActivity(), "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }

    public static long getTimeCount(int type) {
            long currentTime = 1000;

            switch(type) {
                case 0 :
                    currentTime = 1000 * 10;
                    break;
                case 1 :
                    currentTime = 1000 * 30;
                    break;
                case 2 :
                    currentTime = 1000 * 60;
                    break;
                case 3 :
                    currentTime = 1000 * 60 * 2;
                    break;
                case 4 :
                    currentTime = 1000 * 60 * 3;
                    break;
            }

            return currentTime;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search: {
                Intent intent = new Intent(getActivity(), CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
            }
                break;
            case R.id.iv_setting: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.ll_refresh:
                showSingleAlertDialog(ll_refresh);
                break;
        }
    }

    /**一进来 car的最后位置*/
    LatLng latLng = null;

    protected void initCarStatus() {
        mBaiduMap.clear();
        showCarLocationInfo();

        OverlayOptions overlayOptions;
        Marker marker;
        int status, drawableId;

        endLatitudew = getStrToDouble(carGpsInfo.getBlat());
        endLongitudej = getStrToDouble(carGpsInfo.getBlng());
//        endadress = carGpsInfo.getAddress();

        // 图标节点是车辆才有值
        // 车辆状态 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位
        status = getStrToInt(carGpsInfo.getCarstatus()) ;
        drawableId = CarStatusUtil.getDrawableId(status);
        overlayOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(drawableId)).zIndex(5);
        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

        String direc = carGpsInfo.getDirection();
        if (!Tools.isNull(direc)) {
            float direction = Float.parseFloat(direc);
            marker.setRotate(360f-direction);
        }

        //生成一个TextView用户在地图中显示InfoWindow
        mInfoWindow = addInfoWindow(latLng, carGpsInfo);
        //显示InfoWindow
 	    mBaiduMap.showInfoWindow(mInfoWindow);
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
//        iv_my_car_location.setBackgroundResource(R.drawable.car_location);
//        isLocationCarOrMy = false;

		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				//生成一个TextView用户在地图中显示InfoWindow
				LatLng ll = marker.getPosition();
				mInfoWindow = addInfoWindow(ll, carGpsInfo);
				//显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				// 将地图移到到最后一个经纬度位置
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				return true;
			}
		});
    }

    /**获取数据后 赋值*/
    protected void showCarLocationInfo() {
//        tv_carcurrlocation_time.setText(car.getCreateTime());
//        tv_carcurrlocation_speed.setText(car.getSpeed()+"km/h");
//        tv_carcurrlocation_adress.setText(car.getAddress());
//        tv_carcurrlocation_rpm.setText(car.getRpm());
//        tv_carcurrlocation_v.setText(car.getVoltage());
//        tv_carcurrlocation_c.setText(car.getCt());
//        tv_carcurrlocation_km.setText(car.getMileage());
//        tv_carcurrlocation_l.setText(car.getOils());
    }

    TextView carInfo;
    public InfoWindow addInfoWindow(LatLng ll, CarInfoGPS carGpsInfo) {
        Log.i(TAG, "-addInfoWindow():"+ll);

        carInfo = new TextView(getActivity().getApplicationContext());
        carInfo.setWidth(400);
        carInfo.setBackgroundResource(R.drawable.popup);
        carInfo.setPadding(25, 20, 25, 70);
        carInfo.setTextColor(Color.parseColor("#000000"));

        String direc = carGpsInfo.getDirection();
        if (!Tools.isNull(direc)) {
//            float direction = Float.parseFloat(direc);
//            marker.setRotate(360f-direction);
        }

        tv_terminal.setText(carGpsInfo.getCarnumber());

        String info = carGpsInfo.getCarnumber() + "\n" +
                "经度：" + carGpsInfo.getBlng() + " 纬度：" + carGpsInfo.getBlat() + "\n" +
                "方向：" + CarStatusUtil.parseDirection(direc) + "\n" +
                "速度：" + carGpsInfo.getSpeed() + " 千米/小时\n" +
                "地址：" + carGpsInfo.getBaiduAddress() + "\n" +
                "更新时间：" + carGpsInfo.gpstimeDesc();
        carInfo.setText(info);

        // 将marker所在的经纬度的信息转化成屏幕上的坐标
        android.graphics.Point p;

        try {
            Log.i(TAG, "-addInfoWindow() 0:" + "--" + mBaiduMap);
            Log.i(TAG, "-addInfoWindow() 2:" + "--" + mBaiduMap);

            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(carInfo);
            mInfoWindow = new InfoWindow(markerIcon, ll, -10, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    // 隐藏InfoWindow
                    mBaiduMap.hideInfoWindow();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "-addInfoWindow():" + e);
        }

        return mInfoWindow;
    }

    int widthX =0;
    int heightY = 0;

    private void initOverPop() {
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        widthX = wm.getDefaultDisplay().getWidth();
        heightY = wm.getDefaultDisplay().getHeight();

        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        Constants.screenWidth = screenWidth;

//		 this.popView = View.inflate(getApplicationContext(), R.layout.pop_history_map, null);
        this.popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_history_map, null);
        this.tv_car_num_map = ((TextView)this.popView.findViewById(R.id.tv_car_num_map));

        ((LinearLayout.LayoutParams)this.tv_car_num_map.getLayoutParams()).width = (Constants.screenWidth / 2);

        this.tv_sys_time_map = ((TextView)this.popView.findViewById(R.id.tv_sys_time_map));
        this.tv_location_time_map = ((TextView)this.popView.findViewById(R.id.tv_location_time));
        this.tv_car_state_map = ((TextView)this.popView.findViewById(R.id.tv_car_state_map));
        this.tv_distance_map = ((TextView)this.popView.findViewById(R.id.tv_distance_map));
        this.tv_sys_time_map.setVisibility(View.GONE);
        this.tv_distance_map.setVisibility(View.GONE);

        this.tv_car_num_map.setText("粤B12");
    }

    private void initMap(View view) {
        mMapView = (MapView) view.findViewById(R.id.his_bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

        this.mBaiduMap.setMapType(1);
//	    MapStatusUpdate localMapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0F);
//	    this.mBaiduMap.setMapStatus(localMapStatusUpdate);
        this.mMapView.showZoomControls(false);

        initOverPop();

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                String title = "";

                if (mapPoi != null) {
                    title = mapPoi.getName();
                    // ToastUtil.showToast(CarLocation.this, title);

                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mapPoi.getPosition());
                    mBaiduMap.animateMapStatus(u);
                }

                mBaiduMap.hideInfoWindow();
                return true;
            }

            @Override
            public void onMapClick(LatLng p) {
                mBaiduMap.hideInfoWindow();
            }
        });

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        // option.setPoiExtraInfo(true);
        option.setProdName("定位我当前的位置");
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setAddrType("all");
        option.setPriority(LocationClientOption.NetWorkFirst);

        mLocClient.setLocOption(option);
        mLocClient.start();

        mMapView.showZoomControls(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SELECT_DEVICE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    DepartmentCar departmentCar = data.getParcelableExtra("device");
                    terminalCurr = departmentCar.getTerminal();
                    prefBiz.putIntInfo(Constants.PREF_THIS_CURREN_CHANNEL, departmentCar.getChanneltotals());
                    getHttpCarInfoState(terminalCurr);
                }

                break;
            default:
                break;
        }
    }

    private void getHttpCarInfoState(String terminal) {
        if (terminal == null) {
            return;
        }

        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.GetGpsStatus request = retrofit.create(CmsRequest.GetGpsStatus.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("username", name);
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);
        body.addProperty("terminal", terminal);

        Call<JsonObject> call = request.getGpsStatus(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();

                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray jsonArray = resultObj.getAsJsonArray("resultData");

                    if (jsonArray != null && jsonArray.size() != 0) {
                        ArrayList<CarInfoGPS> arrayLGPSInfo = new ArrayList<>();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            CarInfoGPS mCarInfoGPS = GsonUtil.jsonToBean(jsonArray.get(i).toString(), CarInfoGPS.class);
                            arrayLGPSInfo.add(mCarInfoGPS);
                        }

                        if (arrayLGPSInfo != null && arrayLGPSInfo.size()!= 0) {
                            // 发送消息更新数据完成
                            Message msg = new Message();
                            msg.what = Constants.WHAT_GET_CAR_LAST_POSITION;
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("arraylgpsinfo", arrayLGPSInfo.get(0));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private double latitudew = 0.0;
    private String startadress = "";
    private double longitudej = 0.0;

    private double endLatitudew = 0.0;
    private double endLongitudej = 0.0;
//    private String endadress = "";

    /**定位当到前位置*/
    LatLng llCurr;

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            latitudew = location.getLatitude();
            longitudej = location.getLongitude();
            startadress = location.getAddrStr();

			Log.i(TAG, longitudej+"onCreate**********定位SDK监听函数-startadress:"+startadress+location.getAddrStr());

			llCurr = new LatLng(location.getLatitude(), location.getLongitude());

            if (isFirstLoc) {
                isFirstLoc = false;

                Log.i(TAG, "onCreate**********定位SDK监听函数:"+location.getLatitude()+"-longitudej"+location.getLongitude());
//				startAdressStr = location.getAddrStr();
//
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llCurr);
				mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**定位到 车辆的最后位置*/
    private void gocar(LatLng llCurr) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llCurr);
        mBaiduMap.animateMapStatus(u);
    }

    public void showSingleAlertDialog(View view) {
        final String[] items = { "10秒", "30秒", "1分钟", "2分钟", "3分钟" };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择位置更新频率");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getActivity(), items[i]+"-i:"+i, Toast.LENGTH_SHORT).show();
                selectSingleAlertDialogIndex = i;
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mTimeCount != null) {
                    mTimeCount.cancel();
                }

                mTimeCount = new TimeCount(getTimeCount(selectSingleAlertDialogIndex),1000);
                mTimeCount.start();
                prefBiz.putIntInfo(Constants.PREF_MAP_REFRESH, selectSingleAlertDialogIndex);

                alertDialog2.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }

    private double getStrToDouble(String a) {
        double d = 0.0;

        try {
            d = Double.parseDouble(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return d;
    }

    private int getStrToInt(String a) {
        int d = 0;

        try {
            d = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return d;
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        private  long millisInFutureC = 1000;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
            this.millisInFutureC = millisInFuture;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            try {
                getHttpCarInfoState(terminalCurr);

                if (mTimeCount != null) {
                    mTimeCount.cancel();
                    mTimeCount = null;
                }

                int refresh = prefBiz.getIntInfo(Constants.PREF_MAP_REFRESH, 0);
                mTimeCount = new TimeCount(getTimeCount(refresh),1000);
                mTimeCount.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            long timeShow = millisInFutureC / 1000 - millisUntilFinished / 1000;

            tv_timerefresh.setText(millisUntilFinished / 1000 + "秒");

            if (isFirstSecond && (timeShow == 2)) {
                isFirstSecond = false;
                if (!"".equals(terminalCurr)) {
                    getHttpCarInfoState(terminalCurr);
                }
            }
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
                    return;
                }
//                ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();

                carGpsInfo.setBaiduAddress(reverseGeoCodeResult.getAddress() + reverseGeoCodeResult.getSematicDescription());
//                carGpsInfo.setBaiduAddress(reverseGeoCodeResult.getAddress());

                initCarStatus();
            }
        });
    }
}