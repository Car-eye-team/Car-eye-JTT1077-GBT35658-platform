package org.careye.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.activity.CarTreeActivity;
import org.careye.activity.TrackSettingActivity;
import org.careye.bll.PrefBiz;
import org.careye.model.CarInfoGPS;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.CarStatusUtil;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.ToastUtil;
import org.careye.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrackFragment extends Fragment implements View.OnClickListener   ,OnGetRoutePlanResultListener {

    private final String TAG = TrackFragment.class.getSimpleName();
    public final static int REQUEST_SELECT_DEVICE_CODE = 0x11;
    private AlertDialog alertDialog2; //单选框
    private OnFragmentInteractionListener mListener;
    private ImageView mImageView = null;
    private String[] departmentCar;
    private Handler handler;
    private View popView;
    private ImageView tvSelectTime;
    private TextView tv_car_num_map;
    private TextView tv_car_state_map;
    private TextView tv_distance_map;
    private TextView tv_location_time_map;
    private TextView tv_sys_time_map;
    private RoutePlanSearch mSearch;
    private LinearLayout ll_refresh;
    //两个 拖动 进度条  与数据
    private LinearLayout ll_seekBar_control_view ,ll_data_control_view;

   // Button btn_play;
    ImageButton btnPlay;
    //LinearLayout ll_play;
    //SeekBar sb;

    private SeekBar seekBar;//位置
    private SeekBar seekBar_control;//快慢

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

    private ArrayList<CarInfoGPS> posList;	// 历史轨迹点
    ArrayList<CarInfoGPS> trackEntityList;	// 历史轨迹点  新 ui

    public TrackFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        return fragment;
    }
    ArrayList<CarInfoGPS> arrayLGPSInfoList= null;
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
                    arrayLGPSInfoList = ( ArrayList<CarInfoGPS>) bundle.getSerializable("arraylgpsinfolist");
                    if(arrayLGPSInfoList != null&& arrayLGPSInfoList.size() != 0){

                        initCarStatus();//获取数据后 赋值
                    }else{
                        Toast.makeText(getActivity(), "亲，没有数据哦！", Toast.LENGTH_SHORT).show();
//                       ToastUtil.showToast(CarLocation.this, "亲，没有数据哦！");
//                        if (llCurr != null) {
//                            gocar(llCurr);
//                        }
//                        iv_my_car_location.setBackgroundResource(R.drawable.my_location);
//                        isLocationCarOrMy = false;

                    }


                }

//                else if(msg.what == Constants.FINISH){
//                    drawLine(list.get(currentIndex-1),list.get(currentIndex));
//                    doFinish(list.get(currentIndex));
//                }
                else if(msg.what == Constants.WHAT_UPDATE_DATA_TPLAY){
                    ToastUtil.showToast(getActivity(), "播放完毕");
                    btnPlay.setImageResource(R.drawable.selector_playing_buttom_src);
                    isHaveTeackPlay= false;

                    runTimePause();


                }

//                else if(msg.what == Constants.){
//
//                    seekBar.setProgress(0);//timeNew
//                    addMarker(latLngS.get(0));
//
//                }
                else if(msg.what == Constants.WHAT_UPDATE_DATA_T){

                    int strackInt = trackEntityList.size();//392
                    if (trackEntityList.size()>=9999) {
                        strackInt =9999;
                    }
                    if (timeNew>=strackInt) {//392 > = 392
                        return;
                    }
                    if (timeNew >= trackEntityList.size()) {
                        return;
                    }
                    try {




                        seekBar.setProgress(timeNew);
                        if (timeNew >= latLngS.size()) {
                            return;
                        }
                        Log.e(TAG, "-WHAT_UPDATE_DATA_T_timeNew:"+timeNew);
                        //setTextVlue(trackEntityList.get(timeNew));
                        addMarker(latLngS.get(timeNew),trackEntityList.get(timeNew));

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e(TAG, "-WHAT_UPDATE_DATA_T_e:"+e);//-WHAT_UPDATE_DATA_T_e:java.lang.IndexOutOfBoundsException: Invalid index 392, size is 392
                    }
                }
                 else if(what == Constants.WHAT_GET_CAR_LAST_TRACK){

                    if (posList != null) {
                        posList.clear();
                    }
                    if (trackEntityList != null) {
                        trackEntityList.clear();
                    }
                    posList =( ArrayList<CarInfoGPS>) msg.obj;
                    trackEntityList =( ArrayList<CarInfoGPS>) msg.obj;




                    List<LatLng> arg0 = new ArrayList<LatLng>();
                    if(posList != null && posList.size()>0){

                        if (trackEntityList != null || trackEntityList.size()>0) {


                            if (2 <= trackEntityList.size()  ) {//&& trackEntityList.size() < 10000
//								ToastUtil.showToast(CarLocation.this, "开始画图size:"+trackEntityList.size());
                                //开始画图
                                addCustomElementsDemoTwo(trackEntityList,0);

                                //默认不播放
                                isHaveTeackPlay = false;
                                isCanMove  =false;
                                btnPlay.setImageResource(R.drawable.selector_playing_buttom_src);

                                //测试
                                showTime(trackEntityList);


                            }else if (1 == trackEntityList.size()){
                                ToastUtil.showToast(getActivity(), "暂无该时间段的轨迹信息");
                            }else{

                                ToastUtil.showToast(getActivity(), "设备异常");
//								pointLo();
                            }

                        }

//						doPlay(posList);
                    }else{
                        ToastUtil.showToast(getActivity(), "历史轨迹为空");
                        /** 播放控制板块初始化 原来的 进度 条*/
                       // ll_play.setVisibility(View.GONE);


                        mBaiduMap.clear();
                    }
                }
                else if(what == Constants.WHAT_UPDATE_DATA){

                }
            }};
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        mImageView = (ImageView) view.findViewById(R.id.iv_search);

       // btn_play = (Button)  view.findViewById(R.id.his_btn_play);
        btnPlay = (ImageButton) view. findViewById(R.id.btn_play);

        ll_seekBar_control_view = (LinearLayout) view.findViewById(R.id.ll_seekBar_control_view);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar_play);
        seekBar_control = (SeekBar) view.findViewById(R.id.seekBar_control);



        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar_control.setOnSeekBarChangeListener(onSeekBarChangeListenerS);



        mImageView.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        initMap(view);
        prefBiz = new PrefBiz(getActivity());
        terminalCurr = prefBiz.getStringInfo(Constants.PREF_THIS_CURREN_CARID, "");
        int  refresh = prefBiz.getIntInfo(Constants.PREF_MAP_REFRESH, 0);
//        if(!"".equals(terminalCurr)){
//            getHttpCarInfoState(terminalCurr);
//        }else{
//            Toast.makeText(getActivity(), "当前没有选定车", Toast.LENGTH_SHORT).show();
//
//        }

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



    LatLng ll;
    //
    private void pointLo() {
        isFirstLoc = true;
        //		if (ll!=null) {
        //			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        //			mBaiduMap.animateMapStatus(u);
        //		}



    }

    private Marker mMarkerA;
    private Marker mMarkerB;
    //	private InfoWindow mInfoWindow;

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.start_x);//start_x
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.end_x);//ic_end_poi_icon



    BitmapDescriptor none_tripTeack_1 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_1);
    BitmapDescriptor none_tripTeack_2 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_2);
    BitmapDescriptor none_tripTeack_3 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_3);
    BitmapDescriptor none_tripTeack_4 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_4);
    BitmapDescriptor none_tripTeack_5 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_5);

    BitmapDescriptor none_tripTeack_6 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_6);
    BitmapDescriptor none_tripTeack_7 = BitmapDescriptorFactory
            .fromResource(R.drawable.none_tripteack_7);

    int LoLatInt = 0;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
            Log.e(TAG, "选择位置onProgressChanged:"+paramAnonymousInt+"-seekBar.getMax():"+seekBar.getMax());//拖
            if (!isTreckData) {
                return;
            }
            ///timeNew<=ttts && isCanMove


            LoLatInt =paramAnonymousInt;

            int thisSeekMax = seekBar.getMax();
            int thisSeekMaxS = seekBar.getMax()-1;
            if (LoLatInt == thisSeekMax ) {

                handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();
                return;
            }else {
                //				if (LoLatInt == thisSeekMaxS) {
                //					handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();
                //					return;
                //				}

            }


            if (!isCanMove ) {
                timeNew = LoLatInt;
                Log.e(TAG, "onProgressChanged--------------");
                handler.obtainMessage(Constants.WHAT_UPDATE_DATA_T).sendToTarget();
            }
        }

        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
        {
            Log.e(TAG, "选择位置onStartTrackingTouch:");


            if (!isTreckData) {

                return;
            }

            //			停止位移
            isCanMove = false;
        }

        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
        {


            if (!isTreckData) {
                ToastUtil.showToast(getActivity(), "没有轨迹信息不能播放或暂停");
                return;
            }
            isCanMove = true;


            timeNew = LoLatInt;
            int thisSeekMax = seekBar.getMax();
            int thisSeekMaxS = seekBar.getMax()-1;
            if (LoLatInt == thisSeekMax ) {

                handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();

            }
            //			else if(LoLatInt == 0){
            //				handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPSU).sendToTarget();
            //
            //			}
            else{
                //				if (LoLatInt == thisSeekMaxS) {
                //					handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();
                //					return;
                //				}
                Log.e(TAG, "onStopTrackingTouch--------------");
                handler.obtainMessage(Constants.WHAT_UPDATE_DATA_T).sendToTarget();
                if (!isHaveTeackPlay) {//三角形

                    //					handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();
                }else {
                    startThraad();
                }
            }

        }
    };
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListenerS = new SeekBar.OnSeekBarChangeListener()
    {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
            Log.e(TAG, "快 与慢  onProgressChanged:"+paramAnonymousInt);//拖
            //			控制快慢   def 1000 int：5
            switch (paramAnonymousInt) {
                case 0:
                    timeSpeed  = 10000;
                    break;
                case 1:
                    timeSpeed  = 8000;
                    break;
                case 2:
                    timeSpeed  = 6000;
                    break;
                case 3:

                    timeSpeed  = 4000;

                    break;
                case 4:
                    timeSpeed  = 2000;
                    break;
                case 5:
                    timeSpeed  = 1000;//def
                    break;
                case 6:
                    timeSpeed  = 800;
                    break;
                case 7:
                    timeSpeed  = 600;
                    break;
                case 8:
                    timeSpeed  = 400;
                    break;
                case 9:
                    timeSpeed  = 200;
                    break;
                case 10:
                    timeSpeed  = 50;
                    break;

                default:
                    timeSpeed  = 1000;
                    break;
            }

        }

        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
        {
            Log.e(TAG, "快 与慢 onStartTrackingTouch:");
        }

        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
        {
            Log.e(TAG, "快 与慢onStopTrackingTouch:");
        }
    };


    List<LatLng> latLngS =null;
    /**
     * 添加点、线 纹理路径
     */
    public void addCustomElementsDemoTwo(final List<CarInfoGPS> trackEntityList,final int intText) {
        mBaiduMap.clear();

        if (mMarkerA != null) {
            mMarkerA.remove();
        }
        if (mMarkerB != null) {
            mMarkerB.remove();
        }
        if (mInfoWindow != null) {
            mInfoWindow = null;
        }
        if (mInfoWindowS != null) {
            mInfoWindowS = null;
        }

        if (latLngS != null) {
            latLngS.clear();
        }


        latLngS= new ArrayList<LatLng>();
        int ttts = trackEntityList.size();
        if (trackEntityList.size()>=9999) {
            ttts =9999;
        }

        for (int i = 0; i < ttts; i++) {

            double douLat=0;
            double douLng=0;

            try {//car1.getbLat(), car1.getbLng()
                douLat = getStrTodouble ( trackEntityList.get(i).getBlat() );
                douLng = getStrTodouble ( trackEntityList.get(i).getBlng()) ;
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            LatLng pt_a = new LatLng(douLat, douLng);
            latLngS.add(pt_a);

        }


        // add marker overlay
        OverlayOptions ooA = new MarkerOptions().position(latLngS.get(latLngS.size()-1)).icon(bdB)//起点
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        OverlayOptions ooB = new MarkerOptions().position(latLngS.get(0)).icon(bdA)//终点
                .zIndex(5);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));



        try {
            mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                public boolean onMarkerClick(final Marker marker) {
                    if (intText == 1) {
                        return true;
                    }
                    Button button = new Button(getActivity());
                    button.setBackgroundResource(R.drawable.popup);
                    InfoWindow.OnInfoWindowClickListener listener = null;
                    if (marker == mMarkerA) {
								button.setText("更改位置");
//
								button.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {

										mBaiduMap.hideInfoWindow();
									}
								});


                        LatLng ll = marker.getPosition();


								mInfoWindow = addInfoWindow(ll,trackEntityList.get(trackEntityList.size()-1));
								mBaiduMap.showInfoWindow(mInfoWindow);


                    } else if (marker == mMarkerB) {


								button.setText("更改图标");
								button.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {

										mBaiduMap.hideInfoWindow();
									}
								});
                        LatLng ll = marker.getPosition();

								mInfoWindow = addInfoWindow(ll,trackEntityList.get(0));
								mBaiduMap.showInfoWindow(mInfoWindow);
                    }
                    return true;
                }
            });
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.e(TAG, "mBaiduMap.setOnMarkerClickListener-e1"+e1);
        }

        List<LatLng> points = new ArrayList<LatLng>();

        for (int i = 0; i < latLngS.size(); i++) {
            points.add(latLngS.get(i));//点元素
        }


//		//构造对象 非纹理
//		OverlayOptions ooPolyline = new PolylineOptions().width(5)
//				.color(0xAAFF0000).points(points);
//		//添加到地图
//		mBaiduMap.addOverlay(ooPolyline);


        try {
            PolylineOptions localPolylineOptions = new PolylineOptions().width(5).color(0xAAFF0000).points(points);

            mBaiduMap.addOverlay(localPolylineOptions);
//        this.dialogDismiss();
//			mMapView.invalidate();
//        this.index = 0;
            isCanMove = true;
            seekBar.setProgress(0);
//        this.latLngs = getLatLngs(arg0);
            seekBar.setMax(ttts-1);
//        this.mHandler.sendEmptyMessage(12);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "设备异常e:"+e);
        }finally{


        }



        LatLngBounds bounds = null;
        bounds = new LatLngBounds.Builder().include( latLngS.get(latLngS.size()-1) ).include(latLngS.get(0)).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.animateMapStatus(mapStatusUpdate,1000);
        this.mMapView.invalidate();

        mInfoWindow = addInfoWindow(mMarkerA.getPosition(),trackEntityList.get(trackEntityList.size()-1));
        mBaiduMap.showInfoWindow(mInfoWindow);
    }






    /**是否 有轨迹数据*/
    boolean isTreckData = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_search:
                Intent intent = new Intent(getActivity(), TrackSettingActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_DEVICE_CODE);
                break;

            case R.id.btn_play://播放 控件 进度条   播放暂停  切换



                if (!isTreckData) {

                    ToastUtil.showToast(getActivity(), "没有轨迹信息不能播放或暂停");
                    return;
                }
                int thisSeekMax = seekBar.getMax();
                if (LoLatInt+1 >= thisSeekMax) {

                    rePlaying();
                    return;
                }

                if (isHaveTeackPlay) {
                    btnPlay.setImageResource(R.drawable.selector_playing_buttom_src);
                    isHaveTeackPlay= false;

                    runTimePause();
                }else {

                    btnPlay.setImageResource(R.drawable.selector_pause_buttom_src);
                    isHaveTeackPlay= true;
                    runTimePlaying();
                }


                break;
        }
    }
    /** 复位 并 播放 */
    public void rePlaying(){

        //		if ( LoLatInt+1 >=seekBar.getMax()) {
        //			handler.obtainMessage(Constants.WHAT_UPDATE_DATA_TPLAY).sendToTarget();
        //			return;
        //		}
        timeNew= 0;
        seekBar.setProgress(timeNew);

        btnPlay.setImageResource(R.drawable.selector_pause_buttom_src);
        isHaveTeackPlay= true;
        runTimePlaying();

    }
    /** TimeStop停止*/
    public void runTimeStop(){

        mBaiduMap.clear();
        if(trackEntityList != null){
            trackEntityList.clear();
        }
        if(posList != null){
            posList.clear();
        }
        isCanMove = false;
        timeNew= 0;
        timeSpeed = 1000;
        ttts= 0;
        isCanMove= false;
    }
    /** Time暂停*/
    public void runTimePause(){
        //		 停止进度条
        //		 地图移动暂停
        isCanMove = false;
    }
    /** TimePlay*/
    public void runTimePlaying(){
        Log.e(TAG, "runTimePlaying--------------");
        handler.obtainMessage(Constants.WHAT_UPDATE_DATA_T).sendToTarget();///FIRST

        isCanMove = true;
        startThraad();


    }
    int timeNew= 0;
    long timeSpeed = 1000;
    int ttts= 0;
    boolean isCanMove= false;



    /**
     * shi
     */
    private void showTime( ArrayList<CarInfoGPS> trackEntityList ) {
        timeNew= 0;
        timeSpeed = 1000;
        ttts= 0;
        isTreckData = true;
     //   Log.i(TAG, "showTime-time:"+time);
      //  tv_loaction_history.setVisibility(View.VISIBLE);
      //  iv_my_car_triplineb.setVisibility(View.VISIBLE);
        ttts = trackEntityList.size();
        if (trackEntityList.size()>=9999) {
            ttts =9999;
        }
        //		currThread.start();

        Log.e(TAG, "------------------------------------位置0--------"+trackEntityList.size()+"-"+timeNew+"-"+ttts+"-"+isCanMove);

        startThraad();





    }

    void startThraad() {

        new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                while(timeNew<=ttts && isCanMove){

                    try {
                        Log.e(TAG, "------------------------------------位置1--------"+trackEntityList.size()+timeNew+"-"+ttts+"-"+isCanMove);
                        sleep(timeSpeed);
                        handler.obtainMessage(Constants.WHAT_UPDATE_DATA_T).sendToTarget();
                        timeNew++;

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e(TAG, "showTime run()e:"+e);
                    }
                }
            }
        }.start();

    }


    //	播放 控件 进度条   播放暂停  切换
    boolean isHaveTeackPlay = false;




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
               // "里程："+ carGpsInfo.getSummileage().replace("null","")+" 公里\n"+
                "地址："+ carGpsInfo.getAddress()+"\n"+
                "更新时间："+ DateUtil.dateFormatyyMMddHHmmssS(carGpsInfo.getGpstime()) ;

        carInfo.setText(info);
        //将marker所在的经纬度的信息转化成屏幕上的坐标
        android.graphics.Point  p ;


        try {

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
                    departmentCar = data.getStringArrayExtra("extra_trackcar");
                    String  terminal="";//	设备号	String
                    String carnumber="";//	车牌号	String
                    String  startTime="";//	开始时间	String
                    String  endTime="";//	结束时间	String
                    if(departmentCar != null &&departmentCar.length==4){
                        getHistoryTrack(departmentCar);
                    }else{
                        Log.i(TAG,"extra_trackcan  null:");
                    }

                }


                break;
            default:
                break;
        }
    }

    private void getHistoryTrack(String[] historyTrack) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.GetHistoryTrack request = retrofit.create(CmsRequest.GetHistoryTrack.class);
        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String sign = MD5Util.MD5Encode(prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "") + prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "") + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("username", prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, ""));
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);

        body.addProperty("terminal", historyTrack[0]);
        body.addProperty("carnumber", historyTrack[1]);
        body.addProperty("startTime", historyTrack[2]);
        body.addProperty("endTime", historyTrack[3]);

        Call<JsonObject> call = request.getHistoryTrack(body);
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

                            Log.i(TAG,"真实百度经度blng:"+blng+"-百度纬度blag:"+blag);
                        }
                        if(arrayLGPSInfo!=null &&arrayLGPSInfo.size()!= 0){
                            Collections.reverse(arrayLGPSInfo);

                            handler .obtainMessage( Constants.WHAT_GET_CAR_LAST_TRACK, arrayLGPSInfo).sendToTarget();
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
     * 定位SDK监听函数
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
            //Log.i(TAG, longitudej+"---------------------onCreate**********定位SDK监听函数-startadress:"+startadress+location.getAddrStr());
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

    private Button checking;



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

    void setTextVlue(CarInfoGPS carGpsInfo){
//        this.tv_car_num_map = ((TextView)this.popView.findViewById(R.id.tv_car_num_map));
//
//        ((LinearLayout.LayoutParams)this.tv_car_num_map.getLayoutParams()).width = (Constants.screenWidth / 2);
//
//        this.tv_sys_time_map = ((TextView)this.popView.findViewById(R.id.tv_sys_time_map));
//        this.tv_location_time_map = ((TextView)this.popView.findViewById(R.id.tv_location_time));
//        this.tv_car_state_map = ((TextView)this.popView.findViewById(R.id.tv_car_state_map));
//        this.tv_distance_map = ((TextView)this.popView.findViewById(R.id.tv_distance_map));
        this.tv_sys_time_map.setVisibility(View.VISIBLE);
//        this.tv_distance_map.setVisibility(View.GONE);
       int  status =getStrToInt(carGpsInfo.getCarstatus()) ;
            this.tv_car_num_map.setText(""+carGpsInfo.getCarnumber());
            this.tv_sys_time_map.setText("时间:"+carGpsInfo.getGpstime());
            this.tv_car_state_map.setText("状态:"+CarStatusUtil.getDrawableIdStr(status));
            this.tv_location_time_map.setText("定位:"+carGpsInfo.getAddress());
    }

    private void addMarker(LatLng paramLatLng ,CarInfoGPS carGpsInfoS){

        if (this.markerCurr != null) {
            markerCurr.remove();

        }


        MarkerOptions localMarkerOptions = new MarkerOptions().position(paramLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
        this.markerCurr = ((Marker)this.mBaiduMap.addOverlay(localMarkerOptions));

//        mInfoWindowS = new InfoWindow(this.popView, this.markerCurr.getPosition(), -50);
//
//        this.mBaiduMap.showInfoWindow(mInfoWindowS);

        mInfoWindow = addInfoWindow(paramLatLng,carGpsInfoS);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);


        Point currPoint =  mBaiduMap.getProjection().toScreenLocation(paramLatLng);
        int xint =  currPoint.x;
        int yint =  currPoint.y;
        int lsettop = Dp2Px(getActivity(), 140f); //
        int lsetbottom = Dp2Px(getActivity(), 70f); //


        if (xint<27 || yint<lsettop ||xint>widthX-27 || yint>heightY-lsetbottom) {
            MapStatusUpdate localMapStatusUpdate1 = MapStatusUpdateFactory.newLatLng(paramLatLng);
            this.mBaiduMap.setMapStatus(localMapStatusUpdate1);
            this.mMapView.invalidate();
        }

    }
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}