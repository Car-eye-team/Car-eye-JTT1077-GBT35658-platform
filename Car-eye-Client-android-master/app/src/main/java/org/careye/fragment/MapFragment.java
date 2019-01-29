package org.careye.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import org.careye.bll.PrefBiz;
import org.careye.bll.UpdateTask;
import org.careye.model.CarInfoGPS;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.CarStatusUtil;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapFragment extends Fragment implements View.OnClickListener   ,OnGetRoutePlanResultListener {

    private final String TAG = MapFragment.class.getSimpleName();
    public final static int REQUEST_SELECT_DEVICE_CODE = 0x11;
    private AlertDialog alertDialog2; //单选框
    private OnFragmentInteractionListener mListener;
    private ImageView mImageView = null;
    private DepartmentCar departmentCar;
    private Handler handler;
    private View popView;
    private ImageView tvSelectTime;
    private TextView tv_car_num_map;
    private TextView tv_car_state_map;
    private TextView tv_distance_map;
    private TextView tv_location_time_map;
    private TextView tv_sys_time_map;
    private RoutePlanSearch mSearch;
    private LinearLayout  ll_refresh;
    private PrefBiz prefBiz;
    private TextView tv_timerefresh ,tv_terminal ,tv_timeregps;
    String terminalCurr = "0";
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;;
    private InfoWindow mInfoWindow;
    private InfoWindow mInfoWindowS;
    private Marker marker;
    private Marker markerCurr;

    boolean isFirstLoc = true;// 是否首次定位
    /**是否有车辆信息 def false*/
    boolean ishaveCarInfo = false;// 是否有车辆信息
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;



    public MapFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }
    CarInfoGPS carGpsInfo= null;
    boolean isNoTrick  = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int what = msg.what;
                if(what == Constants.WHAT_GET_CAR_LAST_POSITION){
                    Bundle bundle = msg.getData();
                    carGpsInfo = (CarInfoGPS) bundle.getSerializable("arraylgpsinfo");
                    if(carGpsInfo != null){
                        ishaveCarInfo = true;
                        prefBiz.putStringInfo(Constants.PREF_THIS_CURREN_CARID, carGpsInfo.getTerminal());
                        //已经来 获取的车辆最后位置 的 数据
                        tv_terminal.setText(carGpsInfo.getTerminal());
                        tv_timeregps.setText(carGpsInfo.getGpstime());
                        initCarStatus();//获取数据后 赋值
                    }else{
//                       ToastUtil.showToast(CarLocation.this, "亲，没有数据哦！");
//                        if (llCurr != null) {
//                            gocar(llCurr);
//                        }
//                        iv_my_car_location.setBackgroundResource(R.drawable.my_location);
//                        isLocationCarOrMy = false;

                    }


                    isNoTrick  = true;
//                    UiUpdataFive();

                }else if(what == Constants.WHAT_UPDATE_DATA){

                }
            }};
    }
    TimeCount mTimeCount =null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mImageView = (ImageView) view.findViewById(R.id.iv_search);
        ll_refresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
        tv_timerefresh = (TextView) view.findViewById(R.id.tv_timerefresh);

        tv_terminal = (TextView) view.findViewById(R.id.tv_terminal);
        tv_timeregps = (TextView) view.findViewById(R.id.tv_timeregps);
        mImageView.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);
        initMap(view);
        prefBiz = new PrefBiz(getActivity());
        terminalCurr = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        int  refresh = prefBiz.getIntInfo(Constants.PREF_MAP_REFRESH, 0);
        if(!"".equals(terminalCurr)){
            getHttpCarInfoState(terminalCurr);
        }else{
            Toast.makeText(getActivity(), "当前没有选定车", Toast.LENGTH_SHORT).show();

        }


        isNoTrick = true;
        mTimeCount = new TimeCount(getTimeCount(refresh),1000);
        mTimeCount.start();
        return view;
    }
    public static long getTimeCount(int type){
            long currentTime = 1000;
            switch (type){
                case 0 :
                    currentTime = 1000*10;
                    break;
                case 1 :
                    currentTime = 1000*30;
                    break;
                case 2 :
                    currentTime = 1000*60;
                    break;
                case 3 :
                    currentTime = 1000*60*2;
                    break;
                case 4 :
                    currentTime = 1000*60*3;
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

            case R.id.iv_search:
                Intent intent = new Intent(getActivity(), CarTreeActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
                break;
                case R.id.ll_refresh:
               showSingleAlertDialog(ll_refresh);
                break;
        }
    }

    /**一进来 car的最后位置*/
    LatLng latLng = null;
    protected void initCarStatus() {
        // TODO Auto-generated method stub
        mBaiduMap.clear();
        showCarLocationInfo();//

        OverlayOptions overlayOptions = null;
        Marker marker = null;
        int status = 0;
        int drawableId = 0;
        // 位置
        latLng = new LatLng(getStrTodouble(carGpsInfo.getBlat()),getStrTodouble(carGpsInfo.getBlng()));  // 一进来 car的最后位置
        endLatitudew =getStrTodouble(carGpsInfo.getBlat());
        endLongitudej =getStrTodouble(carGpsInfo.getBlng());
        endadress = carGpsInfo.getAddress();

        // 图标节点是车辆才有值
        //车辆状态 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位
        status =getStrToInt(carGpsInfo.getCarstatus()) ;
        drawableId = CarStatusUtil.getDrawableId(status);
        overlayOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .fromResource(drawableId)).zIndex(5);
        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
        String direc = carGpsInfo.getDirection();
        if(! Tools.isNull(direc)){
            float direction = Float.parseFloat(direc);
            marker.setRotate(360f-direction);
        }
        //生成一个TextView用户在地图中显示InfoWindow
        mInfoWindow = addInfoWindow(latLng,carGpsInfo);
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
				mInfoWindow = addInfoWindow(ll,carGpsInfo);
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
//
//        tv_carcurrlocation_rpm.setText(car.getRpm());
//        tv_carcurrlocation_v.setText(car.getVoltage());
//        tv_carcurrlocation_c.setText(car.getCt());
//        tv_carcurrlocation_km.setText(car.getMileage());
//        tv_carcurrlocation_l.setText(car.getOils());

    }

    public InfoWindow addInfoWindow(LatLng ll,CarInfoGPS carGpsInfo){
        Log.i(TAG, "-addInfoWindow():"+ll);
        TextView carInfo = new TextView(getActivity().getApplicationContext());
        carInfo.setWidth(400);
        carInfo.setBackgroundResource(R.drawable.popup);
        carInfo.setPadding(25, 20, 25, 70);
        carInfo.setTextColor(Color.parseColor("#000000"));
        String direc = carGpsInfo.getDirection();
        if(! Tools.isNull(direc)){
//            float direction = Float.parseFloat(direc);
//            marker.setRotate(360f-direction);
        }
        String info = carGpsInfo.getCarnumber()+" "
                  +"\n"+
                "经度："+ carGpsInfo.getBlng()+" 纬度"+ carGpsInfo.getBlat() +"\n"+
                "方向："+CarStatusUtil.parseDirection(direc)+"\n"+
                "速度："+ carGpsInfo.getSpeed()+" 千米/小时\n"+

                "地址："+ carGpsInfo.getAddress()+"\n"+
                "更新时间："+ DateUtil.dateFormatyyMMddHHmmssS(carGpsInfo.getGpstime()) ;

        carInfo.setText(info);
        //将marker所在的经纬度的信息转化成屏幕上的坐标
        android.graphics.Point  p ;



        try {
            Log.i(TAG, "-addInfoWindow() 0:"+""+mBaiduMap);

            Log.i(TAG, "-addInfoWindow()  2:"+"-----"+mBaiduMap);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(carInfo);
            mInfoWindow = new InfoWindow(markerIcon, ll, -10, new InfoWindow.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick() {
                    // TODO Auto-generated method stub
                    //				//隐藏InfoWindow
                    mBaiduMap.hideInfoWindow();
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "-addInfoWindow():"+e);


        }


        return mInfoWindow;
    }

    int widthX =0;
    int heightY = 0;
    private void initOverPop()


    {
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        int screenWidth  = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        widthX = wm.getDefaultDisplay().getWidth();
        heightY = wm.getDefaultDisplay().getHeight();
        Constants.screenWidth=  screenWidth;

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

        if (true)
            this.tv_car_num_map.setText("粤B12");
    }
    private void initMap(View view) {
        // TODO Auto-generated method stub
        mMapView = (MapView) view.findViewById(R.id.his_bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

//		this.mBaiduMap = this.mMapView.getMap();
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
                // TODO Auto-generated method stub
                String title = "";
                if (mapPoi != null){
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
                // TODO Auto-generated method stub
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
                    departmentCar = data.getParcelableExtra("device");
//                    departmentCar.
                      List<String>  terminallist = departmentCar.getTerminalList();
                    if(terminallist !=null && terminallist.size() != 0){
                        String  terminallistS = "";

                        for (int i = 0;i< terminallist.size();i++){
                            if(!"".equals(terminallistS)){
                                terminallistS+=";";
                            }
                            terminallistS +=terminallist.get(i);
                        }
                        Log.i(TAG," 当前树结构设备号:"+terminallistS);
                        getHttpCarInfoState(terminallistS);
                    }
                }


                break;
            default:
                break;
        }
    }

    private void getHttpCarInfoState(String terminal) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.GetTerminalGpsStatus request = retrofit.create(CmsRequest.GetTerminalGpsStatus.class);
        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String sign = MD5Util.MD5Encode(prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "") + prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "") + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("username", prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, ""));
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);

        body.addProperty("terminal", terminal);

        Call<JsonObject> call = request.getTerminalGpsStatus(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                   // JsonObject dataObj = resultObj.getAsJsonObject("resultData");
                    JsonArray jsonArray = resultObj.getAsJsonArray("resultData");
                    if(jsonArray !=null && jsonArray.size() != 0){
                        ArrayList<CarInfoGPS> arrayLGPSInfo = new ArrayList<CarInfoGPS>();
//                        JsonArray jsonArray = dataObj.getAsJsonArray("");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject dataObj = jsonArray.get(i).getAsJsonObject();
                            CarInfoGPS mCarInfoGPS = new CarInfoGPS();
                            String  blng =dataObj.get("blng").getAsString();
                            String blag =dataObj.get("blat").getAsString();

                            mCarInfoGPS.setBlng(blng);//                    blng	百度经度
                            mCarInfoGPS.setBlat(blag);//                    blat	百度纬度
                            mCarInfoGPS.setCarstatus(dataObj.get("carstatus").getAsString());//carstatus
                            mCarInfoGPS.setDirection(dataObj.get("direction").getAsString());//carstatus
                            mCarInfoGPS.setSpeed(dataObj.get("speed").getAsString());//carstatus
                            mCarInfoGPS.setAddress(dataObj.get("address").getAsString());//carstatus
                            mCarInfoGPS.setTerminal(dataObj.get("terminal").getAsString());//carstatus
                            mCarInfoGPS.setCarnumber(dataObj.get("carnumber").getAsString());//carstatus
                            mCarInfoGPS.setGpstime(dataObj.get("gpstime").getAsString());//carstatus



                            arrayLGPSInfo.add(mCarInfoGPS);

                            Log.i(TAG,"百度经度blng:"+blng+"-百度纬度blag:"+blag);
                        }
                        if(arrayLGPSInfo!=null &&arrayLGPSInfo.size()!= 0){


                                // 发送消息更新数据完成
                            Message msg = new Message();
                            msg.what = Constants.WHAT_GET_CAR_LAST_POSITION;
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("arraylgpsinfo", arrayLGPSInfo.get(0));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
            }
        });
    }

    private String startadress ="";
    private double latitudew =0.0;
    private double longitudej =0.0;

    private double endLatitudew =0.0;
    private double endLongitudej =0.0;
    private String endadress ="";
    /**定位当到前位置*/
    LatLng llCurr;
    /**
     *
     */
    public class MyLocationListenner implements BDLocationListener {

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

            latitudew =location.getLatitude();
            longitudej =location.getLongitude();
            startadress =location.getAddrStr();
			Log.i(TAG, longitudej+"---------------------onCreate**********定位SDK监听函数-startadress:"+startadress+location.getAddrStr());
            llCurr= new LatLng(location.getLatitude(),
                    location.getLongitude());

            if (isFirstLoc) {
                isFirstLoc = false;

                Log.i(TAG, "---------------------onCreate**********定位SDK监听函数:"+location.getLatitude()+"-longitudej"+location.getLongitude());
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

    public void showSingleAlertDialog(View view){
        final String[] items = {"10秒", "30秒", "1分钟", "2分钟","3分钟"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择位置更新频率");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), items[i]+"-i:"+i, Toast.LENGTH_SHORT).show();



                if(mTimeCount != null){

                    mTimeCount.cancel();

                }
                mTimeCount = new TimeCount(getTimeCount(i),1000);
                mTimeCount.start();
                prefBiz.putIntInfo(Constants.PREF_MAP_REFRESH, i);
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG,"确定更新："+i);
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

    private double getStrTodouble( String a){
        double d= 0.0;
        try {
            d=Double.parseDouble(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }
    private int getStrToInt( String a){
        int d= 0;
        try {
            d=Integer.parseInt(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }
    private TimeCount times;
    private Button checking;
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        private  long millisInFutureC = 1000;
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
            this.millisInFutureC = millisInFuture;
        }
        @Override
        public void onFinish() {//计时完毕时触发


            try {
//                if (isNoTrick) {
//                    if (carGpsInfo != null) {
//                        carGpsInfo = null;
//
//                    }
//                    getHttpCarInfoState(terminalCurr);
//                }else {
//                    if(times!=null){
//                        times.cancel();
//                    }
//
//                }
                int  refresh = prefBiz.getIntInfo(Constants.PREF_MAP_REFRESH, 0);
                getHttpCarInfoState(terminalCurr);

                isNoTrick = true;
                mTimeCount = new TimeCount(getTimeCount(refresh),1000);
                mTimeCount.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            long timeShow =    millisInFutureC/1000 - millisUntilFinished /1000;
            tv_timerefresh.setText(millisUntilFinished /1000+"秒");
        }

    }
    public static String formatDuring(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);

        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);

        long seconds = (mss % (1000 * 60)) / 1000;

        return days + "天" + hours + "时" + minutes + "分"
                + seconds + "秒 ";

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        try {
            super.onDestroy();
            // 退出时销毁定位
            mLocClient.stop();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();

            if(mTimeCount != null){

                mTimeCount.cancel();
                mTimeCount =null;

            }
            Log.e(TAG,"onDestroy");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onPause() {
        /**
         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
         */
        mMapView.onPause();
        super.onPause();
        System.out.println("Main,onpause()");
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if (times != null) {
            times.cancel();
        }
        if(mTimeCount != null){

            mTimeCount.cancel();
            mTimeCount =null;

        }
        Log.e(TAG,"onPause");
    }

    private Timer timer;
    @Override
    public void onResume() {
        /**
         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
         */
        mMapView.onResume();
        super.onResume();
        System.out.println("Main,onresume()");
        boolean update = prefBiz.getBooleanInfo(Constants.PREF_AUTO_UPDATE, false);
//        if(update ){
//            String timeStr = prefBiz.getStringInfo(Constants.PREF_UPDATE_INTERVAL, "10秒");
//            long time = DateUtil.getLongTime(timeStr);
//            timer = new Timer();
//            UpdateTask task = new UpdateTask(handler);
//            timer.schedule(task, 10000, time);
//        }

        isFirstLoc = true;
        Log.e(TAG,"onResume");

    }

}