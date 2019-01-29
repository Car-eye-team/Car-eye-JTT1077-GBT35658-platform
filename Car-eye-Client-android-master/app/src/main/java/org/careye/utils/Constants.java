package org.careye.utils;

public class Constants {

    /** 用户名*/
    public static final String PREF_LOGIN_NAME = "pref_login_name"; 	// 用户名
    public static final String PREF_LOGIN_PW = "pref_login_pw"; 	// 用户名
    public static final String PREF_LOGIN_IP = "pref_login_ip"; 	// ip
    /**登录 URL*/
    public static final String HTTP_URL = "http://www.liveoss.com:8088/cmsapi/"; 	// 用户名


    /**登录*/
    public static final String ACTION_LOGIN ="userLogin"; 	// 用户名



    /** Handler msg.what 特殊消息相关*/
    public static final int WHAT_CONNECTED_TIMEOUT_B = 1041;	// http网络连接超时  特殊
    /** Handler msg.what 特殊消息相关*/
    public static final int WHAT_JSON_PARSE_ERROR_B = 1042;	// json解析错误

    /** Handler msg.what 特殊消息相关*/
    public static final int WHAT_CONNECTED_TIMEOUT_C = 1043;	// http网络连接超时 特殊
    /** Handler msg.what 特殊消息相关*/
    public static final int WHAT_JSON_PARSE_ERROR_C = 1044;	// json解析错误  特殊

    public static final int WHAT_AKEY_TIMEOUT = 1045;	// WHAT_akey_timeout

    public static final int WHAT_LOGIN = 1005;	// 登录
    public static final int WHAT_HTTPOK = 1006;	// HTTPOK
    public static final int WHAT_HTTPEOOOR = 1007;	// HTTPEOOOR

    public static final int  WHAT_GET_CAR_LAST_POSITION = 108;	// HTTPEOOOR
    public static final int WHAT_UPDATE_DATA = 109;	// 刷新数据
    public static final int  WHAT_GET_CAR_LAST_TRACK = 110;	// HTTPEOOOR
    /**历史轨迹播放相关*/
    public static final int START = 101;	// 开始
    public static final int DRAW_LINE = 102;	// 画线
    public static final int DRAW_POINT = 103;	// 画点
    public static final int OVERLAY = 104;	// 覆盖物
    public static final int FINISH = 105;	// 结束
    public static final String PREF_INTERVAL = "pref_interval"; 	// 历史轨迹播放间隔

    public static final int WHAT_UPDATE_DATA_TPLAY = 10299;	// 播放按 钮
    public static final int WHAT_UPDATE_DATA_TPSU = 10399;	// 暂停按 钮
    public static final int WHAT_UPDATE_DATA_T = 10199;	// 刷新数据

    public static final String PREF_FIRST_TIME = "first_time"; 	// 是否初次使用
    public static final String PREF_IS_LOGIN = "is_login"; 	// 是否已登录

    /** 用户id*/
    public static final String PREF_USER_ID = "pref_user_id"; 	// 用户id
    /**carid 默认登陆 取第一个carID 为当前carid*/
    public static final String PREF_THIS_CURREN_CARID = "pref_this_curren_carid"; 	// 默认登陆 取第一个carID 为当前carid
    public static final String PREF_THIS_CURREN_CARNUM = "pref_this_curren_carnum"; 	//
    public static final String PREF_MAP_REFRESH = "pref_map_refresh"; 	//
    public static final String PREF_AUTO_UPDATE = "pref_auto_update"; 	//
    public static final String PREF_UPDATE_INTERVAL = "pref_update_interval"; 	//

    public static int screenWidth  ;

    /** 汽车状态 **/

    public final static int STATUS_LONG_OFF_LINE = 1;
    public final static String LONG_OFF_LINE = "(长时间离线)";

    public final static int STATUS_OFF_LINE = 2;
    public final static String OFF_LINE = "(离线)";

    public final static int STATUS_SHUT_DOWN = 3;
    public final static String SHUT_DOWN = "(熄火)";

    public final static int STATUS_RUN = 5;
    public final static String RUN = "(行驶)";

    public final static int STATUS_PARK = 4;
    public final static String PARK = "(停车)";

    public final static int STATUS_ALARM = 6;
    public final static String ALARM = "(报警)";

    public final static int STATUS_ONLINE = 7;
    public final static String ONLINE = "(在线)";

    public final static int STATUS_NO_POSITION = 8;
    public final static String NO_POSITION = "(未定位)";
}
