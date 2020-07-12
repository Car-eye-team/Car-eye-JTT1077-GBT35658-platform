package org.careye.model;

import android.text.TextUtils;

import org.careye.utils.PositionTransducer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Alarm implements Serializable {

    //"driverid":null,
    //        "showCount":null,
    //        "devicesafetyalarmid":null,
    //        "carid":null,
    //        "color":null,
    //        "colorName":null,
    //        "deptid":null,
    //        "deptname":null,
    //        "carnumber":null,
    //        "handleuser":null,
    //        "alarmLevelName":null,
    //        "minclassName":null,
    //        "terminal":null,
    //        "path":null,
    //        "filetype":null,
    //        "alarmname":null,
    //        "startTimeStr":null,
    //        "endTimeStr":null,
    //        "sbL1FcwCount":null,
    //        "sbL2FcwCount":null,
    //        "sbL1LdwCount":null,
    //        "sbL2LdwCount":null,
    //        "sbL1NearCarCount":null,
    //        "sbL2NearCarCount":null,
    //        "sbL1HitPedestrianCount":null,
    //        "sbL2HitPedestrianCount":null,
    //        "sbL1FrequentLaneChangeCount":null,
    //        "sbL2FrequentLaneChangeCount":null,
    //        "sbL1RoadMarkingOverLimitCount":null,
    //        "sbL2RoadMarkingOverLimitCount":null,
    //        "sbL1ObstacleCount":null,
    //        "sbL2ObstacleCount":null,
    //        "sbL1RouteFlagCount":null,
    //        "sbL2RouteFlagCount":null,
    //        "sbL1DriverChangeUpCount":null,
    //        "sbL2DriverChangeUpCount":null,
    //        "sbL1FatigueDrivingCount":null,
    //        "sbL2FatigueDrivingCount":null,
    //        "sbL1CallCount":null,
    //        "sbL2CallCount":null,
    //        "sbL1SmokingCount":null,
    //        "sbL2SmokingCount":null,
    //        "sbL1DistractedDrivingCount":null,
    //        "sbL2DistractedDrivingCount":null,
    //        "sbL1DriverAbnormalCount":null,
    //        "sbL2DriverAbnormalCount":null,
    //        "sbL1AdasTakePhotoCount":null,
    //        "sbL2AdasTakePhotoCount":null,
    //        "sbL1DsmTakePhotoCount":null,
    //        "sbL2DsmTakePhotoCount":null,
    //        "binServe":null,
    //        "binServePort":null,
    //        "drivername":null,
    //        "idnumber":null,
    //        "altitude":null,
    //        "gpsstatus":null,
    //        "strGPSstatus":null,
    //        "filepath":null,
    //        "vstatus":null

    private String id;
    private String alarmDesc;
    private String alarmDuration;
    private String alarmInfo;
    private String alarmLevel;
    private String alarmNumber;
    private String alarmSource;
    private String createDate;
    private String endExtraInfo;
    private String endLat;
    private String endLon;
    private String endSpeed;
    private String endLocation;
    private String endTime;
    private String endStatusList;
    private String endAlarmList;
    private String handleBy;
    private String handleContent;
    private String handleTime;
    private String isDelete;
    private String isHandle;
    private String modifyDate;
    private String startAlarmList;
    private String startStatusList;
    private String startExtraInfo;
    private String startLat;
    private String startLon;
    private String startLocation;
    private String startSpeed;
    private String startTime;
    private String startTimestamp;
    private String terminalId;
    private String terminal;
    private String stime;
    private String etime;
    private String count;
    private String carNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmDesc() {
        if (TextUtils.isEmpty(alarmDesc)) {
            return "";
        }
        return alarmDesc;
    }

    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }

    public String getAlarmDuration() {
        return alarmDuration;
    }

    public void setAlarmDuration(String alarmDuration) {
        this.alarmDuration = alarmDuration;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmNumber() {
        if (TextUtils.isEmpty(alarmNumber)) {
            return "";
        }

        return alarmNumber;
    }

    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEndExtraInfo() {
        return endExtraInfo;
    }

    public void setEndExtraInfo(String endExtraInfo) {
        this.endExtraInfo = endExtraInfo;
    }

    public String getEndLat() {
        if (TextUtils.isEmpty(endLon) || TextUtils.isEmpty(endLat)) {
            return "--";
        }

        double[] gcj2bd = PositionTransducer.gps2bd(Double.parseDouble(endLat), Double.parseDouble(endLon));
        return String.format("%.6f", gcj2bd[0]);
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLon() {
        if (TextUtils.isEmpty(endLon) || TextUtils.isEmpty(endLat)) {
            return "--";
        }

        double[] gcj2bd = PositionTransducer.gps2bd(Double.parseDouble(endLat), Double.parseDouble(endLon));
        return String.format("%.6f", gcj2bd[1]);
    }

    public void setEndLon(String endLon) {
        this.endLon = endLon;
    }

    public String getEndSpeed() {
        if (TextUtils.isEmpty(endSpeed)) {
            return "0";
        }
        return endSpeed;
    }

    public void setEndSpeed(String endSpeed) {
        this.endSpeed = endSpeed;
    }

    public String getEndLocation() {
        if (TextUtils.isEmpty(endLocation)) {
            return "";
        }
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getEndTime() {
        if (TextUtils.isEmpty(endTime)) {
            return "";
        }

        Date date = new Date(Long.parseLong(endTime));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndStatusList() {
        return endStatusList;
    }

    public void setEndStatusList(String endStatusList) {
        this.endStatusList = endStatusList;
    }

    public String getEndAlarmList() {
        return endAlarmList;
    }

    public void setEndAlarmList(String endAlarmList) {
        this.endAlarmList = endAlarmList;
    }

    public String getHandleBy() {
        if (TextUtils.isEmpty(handleBy)) {
            return "";
        }

        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleContent() {
        return handleContent;
    }

    public void setHandleContent(String handleContent) {
        this.handleContent = handleContent;
    }

    public String getHandleTime() {
        if (TextUtils.isEmpty(handleTime)) {
            return "";
        }
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getStartAlarmList() {
        return startAlarmList;
    }

    public void setStartAlarmList(String startAlarmList) {
        this.startAlarmList = startAlarmList;
    }

    public String getStartStatusList() {
        return startStatusList;
    }

    public void setStartStatusList(String startStatusList) {
        this.startStatusList = startStatusList;
    }

    public String getStartExtraInfo() {
        return startExtraInfo;
    }

    public void setStartExtraInfo(String startExtraInfo) {
        this.startExtraInfo = startExtraInfo;
    }

    public String getStartLat() {
        if (TextUtils.isEmpty(startLon) || TextUtils.isEmpty(startLat)) {
            return "--";
        }

        double[] gcj2bd = PositionTransducer.gps2bd(Double.parseDouble(startLat), Double.parseDouble(startLon));
        return String.format("%.6f", gcj2bd[0]);
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLon() {
        if (TextUtils.isEmpty(startLon) || TextUtils.isEmpty(startLat)) {
            return "--";
        }

        double[] gcj2bd = PositionTransducer.gps2bd(Double.parseDouble(startLat), Double.parseDouble(startLon));
        return String.format("%.6f", gcj2bd[1]);
    }

    public void setStartLon(String startLon) {
        this.startLon = startLon;
    }

    public String getStartLocation() {
        if (TextUtils.isEmpty(startLocation)) {
            return "";
        }
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartSpeed() {
        if (TextUtils.isEmpty(startSpeed)) {
            return "0";
        }
        return startSpeed;
    }

    public void setStartSpeed(String startSpeed) {
        this.startSpeed = startSpeed;
    }

    public String getStartTime() {
        if (TextUtils.isEmpty(startTime)) {
            return "";
        }

        Date date = new Date(Long.parseLong(startTime));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public String getTime() {
        Date date = new Date(Long.parseLong(getStartTimestamp()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimestamp() {
        if (TextUtils.isEmpty(startTimestamp)) {
            return "0";
        }

        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getTerminalId() {
        if (TextUtils.isEmpty(terminalId)) {
            return "";
        }
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCarNumber() {
        if (TextUtils.isEmpty(carNumber)) {
            return "";
        }
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
