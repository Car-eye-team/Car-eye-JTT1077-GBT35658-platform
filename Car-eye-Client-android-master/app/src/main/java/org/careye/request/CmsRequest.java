package org.careye.request;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class CmsRequest {

    /**
     * 用户登录接口
     */
    public interface UserLogin {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("userLogin")
        Call<JsonObject> userLogin(@Body JsonObject body);
    }

    /**
     * 获取用户机构车辆树接口
     */
    public interface DeptTree {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("deptTree")
        Call<JsonObject> deptTree(@Body JsonObject body);
    }

    /**
     * 播放和停止视频
     */
    public interface PlayOrStopSend {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("playSend")
        Call<JsonObject> playOrStopSend(@Body JsonObject body);
    }

    /**
     * 根据车辆获取设备信息列表
     */
    public interface GetTerminalList {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("getTerminalList")
        Call<JsonObject> getTerminalList(@Body JsonObject body);
    }

    /**
     * 跟据设备号获取设备GPS状态
     */
    public interface GetGpsStatus {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("getGpsStatus")
        Call<JsonObject> getGpsStatus(@Body JsonObject body);
    }

    /**
     * 获取设备历史轨迹数据
     */
    public interface GetHistoryTrack {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("getHistoryTrack")
        Call<JsonObject> getHistoryTrack(@Body JsonObject body);
    }

    /**
     * 获取设备在线状态。
     */
    public interface GetCarStatus {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("getCarStatus")
        Call<JsonObject> getCarStatus(@Body JsonObject body);
    }

    /**
     * 查询设备历史录像列表。
     */
    public interface QueryTerminalFileList {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("queryTerminalFileList")
        Call<JsonObject> queryTerminalFileList(@Body JsonObject body);
    }

    /**
     * 获取设备报警分页列表
     */
    public interface QueryAlarmList {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("queryAlarmList")
        Call<JsonObject> queryAlarmList(@Body JsonObject body);
    }

    /**
     * 根据设备号获取设备信息
     */
    public interface GetTerminalInfo {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("getTerminalInfo")
        Call<JsonObject> getTerminalInfo(@Body JsonObject body);
    }

    /**
     * 远程录像回放。
     */
    public interface PlaybackAppoint {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("playbackAppoint")
        Call<JsonObject> playbackAppoint(@Body JsonObject body);
    }
}
