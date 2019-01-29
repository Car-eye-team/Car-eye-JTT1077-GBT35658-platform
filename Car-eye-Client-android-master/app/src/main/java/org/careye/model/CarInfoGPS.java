package org.careye.model;

import java.io.Serializable;

public class CarInfoGPS implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String  terminal;//	设备号
    private String   carnumber;//产品号
    private  String  carstatus;//车辆状态
    private  String  devicetype;//	设备类型
    private String   typename;//	设备类型名称
    private String   disktype;//	硬盘类型
    private  String  audioCodec;//	音频编码
    private  String  leftRotation	;//
    private  String  stopStatus;//	刹车状态
    private  String   foreward;//	正转状态
    private String    overturn	;//	反转状态
    private String   antenna;//	gps 线状态
    private String   disk	;//	硬盘状态
    private  String  network	;//	3g/4g模块状态
    private   String  staticStatus	;//	静止状态
    private  String  replenish	;//	补传状态
    private String    nightStatus	;//	夜间状态
    private String    overload	;//	超载状态
    private String    aacGps	;//	停车acc状态
    private  String   areaAlarm	;//	出区域报警（终端产生
    private  String  overRoute	;//	出线路报警（终端产生）
    private  String  areaAlarmStatus	;//	区域报警状态
    private  String   flowAlarm	;//	流量使用报警
    private  String   backupBattery	;//	主机掉电由后备电池供电
    private  String   carDefences;//		车辆设防
    private  String   leaveArea	;//	出区域报警（终端产生）
    private String    vollower	;//	电池电压过低
    private String    motor	;//	发动机
    private  String   loaded	;//	车载状态
    private String   working	;//	作业状态
    private String   operation;//		运营状态
    private  String   oilway	;//	油路正常
    private   String  circuit	;//	电路正常
    private String   doorlock;//		门解锁
    private  String   overspeedAlarm	;//	区域超速报警(平台产生)
    private  String   overtimeAlarm ;//		时间段超速报警(平台产生)
    private  String   lowerspeedAlarm	;//	时间段低速报警(平台产生)
    private String   tiredAlarm;//	疲劳驾驶(平台产生)
    private String    channel1;//		通道1视频丢失

    private  String   channel2;//		通道2视频丢失
    private  String   channel3	;//	通道3视频丢失
    private String   channel4 ;//	 通道4视频丢失

    private   String    channe5 ;//	通道5视频丢失

    private  String  channel6	;//	通道6视频丢失
    private  String   channel7;//		通道7视频丢失
    private  String   channel8	;//	通道8视频丢失
    private  String   urgentAlarm	;//	紧急报警
    private  String   gnssFault	;//	gnss模块故障
    private  String   gnssDisconnected	;//gnss;//	天线未接或者剪断
    private  String  lcdFault	;//	终端lcd或者显示器故障)
    private String   channel1Fault	;//	摄像头通道1故障
    private  String   channel2Fault	;//	摄像头通道2故障
    private String   channel3Fault	;//	摄像头通道3故障
    private String    channel4Fault	;//	摄像头通道4故障
    private String    channel5Fault	;//	摄像头通道5故障
    private String   channel6Fault	;//	摄像头通道6故障
    private String   channel7Fault	;//	摄像头通道7故障

    private String  channel8Fault;//		摄像头通道8故障
    private String   deviate;//		路线偏离报警
    private String   driveOvertime	;//	当天累计驾驶超时
    private String   stolen
            ;//	 车辆被盗
    private String    ignition;//		车辆非法点火
    private String    oilAbnormal	;//	车辆油量异常
    private   String    bump	;//	碰撞侧翻报警
    private String   unusualDrive1;//		异常行驶状态
    private String    unusualDrive2	;//	异常行驶状态
    private String    enterErea	;//	进区域
    private String    enterRoute	;//	进路线
    private String    ptz;//
    private String   lng	;//	经度
    private String   lat	;//	纬度
    private String   blng	;//	百度经度
    private String   blat	;//	百度纬度
    private String   glng	;//	高德经度

    public CarInfoGPS() {
        super();
    }

    private String   glat	;//	高德纬度
    private String    gaddress	;//	高德地址
    private String    altitude	;//	高度
    private String   speed	;//	速度
    private String    direction	;//	方向
    private String    address	;//	地址
    private String   mileage  ;//	里程

    private String    summileage ;// 总里程

    private String  gpstime;//	gps时间
    private String   gpsflag;//	gps是否有效
    private String   acc;//	acc状态

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }
/**车辆状态 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位*/
    public String getCarstatus() {
        return carstatus;
    }
    /**车辆状态 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位*/
    public void setCarstatus(String carstatus) {
        this.carstatus = carstatus;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDisktype() {
        return disktype;
    }

    public void setDisktype(String disktype) {
        this.disktype = disktype;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String getLeftRotation() {
        return leftRotation;
    }

    public void setLeftRotation(String leftRotation) {
        this.leftRotation = leftRotation;
    }

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public String getForeward() {
        return foreward;
    }

    public void setForeward(String foreward) {
        this.foreward = foreward;
    }

    public String getOverturn() {
        return overturn;
    }

    public void setOverturn(String overturn) {
        this.overturn = overturn;
    }

    public String getAntenna() {
        return antenna;
    }

    public void setAntenna(String antenna) {
        this.antenna = antenna;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getStaticStatus() {
        return staticStatus;
    }

    public void setStaticStatus(String staticStatus) {
        this.staticStatus = staticStatus;
    }

    public String getReplenish() {
        return replenish;
    }

    public void setReplenish(String replenish) {
        this.replenish = replenish;
    }

    public String getNightStatus() {
        return nightStatus;
    }

    public void setNightStatus(String nightStatus) {
        this.nightStatus = nightStatus;
    }

    public String getOverload() {
        return overload;
    }

    public void setOverload(String overload) {
        this.overload = overload;
    }

    public String getAacGps() {
        return aacGps;
    }

    public void setAacGps(String aacGps) {
        this.aacGps = aacGps;
    }

    public String getAreaAlarm() {
        return areaAlarm;
    }

    public void setAreaAlarm(String areaAlarm) {
        this.areaAlarm = areaAlarm;
    }

    public String getOverRoute() {
        return overRoute;
    }

    public void setOverRoute(String overRoute) {
        this.overRoute = overRoute;
    }

    public String getAreaAlarmStatus() {
        return areaAlarmStatus;
    }

    public void setAreaAlarmStatus(String areaAlarmStatus) {
        this.areaAlarmStatus = areaAlarmStatus;
    }

    public String getFlowAlarm() {
        return flowAlarm;
    }

    public void setFlowAlarm(String flowAlarm) {
        this.flowAlarm = flowAlarm;
    }

    public String getBackupBattery() {
        return backupBattery;
    }

    public void setBackupBattery(String backupBattery) {
        this.backupBattery = backupBattery;
    }

    public String getCarDefences() {
        return carDefences;
    }

    public void setCarDefences(String carDefences) {
        this.carDefences = carDefences;
    }

    public String getLeaveArea() {
        return leaveArea;
    }

    public void setLeaveArea(String leaveArea) {
        this.leaveArea = leaveArea;
    }

    public String getVollower() {
        return vollower;
    }

    public void setVollower(String vollower) {
        this.vollower = vollower;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getLoaded() {
        return loaded;
    }

    public void setLoaded(String loaded) {
        this.loaded = loaded;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOilway() {
        return oilway;
    }

    public void setOilway(String oilway) {
        this.oilway = oilway;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getDoorlock() {
        return doorlock;
    }

    public void setDoorlock(String doorlock) {
        this.doorlock = doorlock;
    }

    public String getOverspeedAlarm() {
        return overspeedAlarm;
    }

    public void setOverspeedAlarm(String overspeedAlarm) {
        this.overspeedAlarm = overspeedAlarm;
    }

    public String getOvertimeAlarm() {
        return overtimeAlarm;
    }

    public void setOvertimeAlarm(String overtimeAlarm) {
        this.overtimeAlarm = overtimeAlarm;
    }

    public String getLowerspeedAlarm() {
        return lowerspeedAlarm;
    }

    public void setLowerspeedAlarm(String lowerspeedAlarm) {
        this.lowerspeedAlarm = lowerspeedAlarm;
    }

    public String getTiredAlarm() {
        return tiredAlarm;
    }

    public void setTiredAlarm(String tiredAlarm) {
        this.tiredAlarm = tiredAlarm;
    }

    public String getChannel1() {
        return channel1;
    }

    public void setChannel1(String channel1) {
        this.channel1 = channel1;
    }

    public String getChannel2() {
        return channel2;
    }

    public void setChannel2(String channel2) {
        this.channel2 = channel2;
    }

    public String getChannel3() {
        return channel3;
    }

    public void setChannel3(String channel3) {
        this.channel3 = channel3;
    }

    public String getChannel4() {
        return channel4;
    }

    public void setChannel4(String channel4) {
        this.channel4 = channel4;
    }

    public String getChanne5() {
        return channe5;
    }

    public void setChanne5(String channe5) {
        this.channe5 = channe5;
    }

    public String getChannel6() {
        return channel6;
    }

    public void setChannel6(String channel6) {
        this.channel6 = channel6;
    }

    public String getChannel7() {
        return channel7;
    }

    public void setChannel7(String channel7) {
        this.channel7 = channel7;
    }

    public String getChannel8() {
        return channel8;
    }

    public void setChannel8(String channel8) {
        this.channel8 = channel8;
    }

    public String getUrgentAlarm() {
        return urgentAlarm;
    }

    public void setUrgentAlarm(String urgentAlarm) {
        this.urgentAlarm = urgentAlarm;
    }

    public String getGnssFault() {
        return gnssFault;
    }

    public void setGnssFault(String gnssFault) {
        this.gnssFault = gnssFault;
    }

    public String getGnssDisconnected() {
        return gnssDisconnected;
    }

    public void setGnssDisconnected(String gnssDisconnected) {
        this.gnssDisconnected = gnssDisconnected;
    }

    public String getLcdFault() {
        return lcdFault;
    }

    public void setLcdFault(String lcdFault) {
        this.lcdFault = lcdFault;
    }

    public String getChannel1Fault() {
        return channel1Fault;
    }

    public void setChannel1Fault(String channel1Fault) {
        this.channel1Fault = channel1Fault;
    }

    public String getChannel2Fault() {
        return channel2Fault;
    }

    public void setChannel2Fault(String channel2Fault) {
        this.channel2Fault = channel2Fault;
    }

    public String getChannel3Fault() {
        return channel3Fault;
    }

    public void setChannel3Fault(String channel3Fault) {
        this.channel3Fault = channel3Fault;
    }

    public String getChannel4Fault() {
        return channel4Fault;
    }

    public void setChannel4Fault(String channel4Fault) {
        this.channel4Fault = channel4Fault;
    }

    public String getChannel5Fault() {
        return channel5Fault;
    }

    public void setChannel5Fault(String channel5Fault) {
        this.channel5Fault = channel5Fault;
    }

    public String getChannel6Fault() {
        return channel6Fault;
    }

    public void setChannel6Fault(String channel6Fault) {
        this.channel6Fault = channel6Fault;
    }

    public String getChannel7Fault() {
        return channel7Fault;
    }

    public void setChannel7Fault(String channel7Fault) {
        this.channel7Fault = channel7Fault;
    }

    public String getChannel8Fault() {
        return channel8Fault;
    }

    public void setChannel8Fault(String channel8Fault) {
        this.channel8Fault = channel8Fault;
    }

    public String getDeviate() {
        return deviate;
    }

    public void setDeviate(String deviate) {
        this.deviate = deviate;
    }

    public String getDriveOvertime() {
        return driveOvertime;
    }

    public void setDriveOvertime(String driveOvertime) {
        this.driveOvertime = driveOvertime;
    }

    public String getStolen() {
        return stolen;
    }

    public void setStolen(String stolen) {
        this.stolen = stolen;
    }

    public String getIgnition() {
        return ignition;
    }

    public void setIgnition(String ignition) {
        this.ignition = ignition;
    }

    public String getOilAbnormal() {
        return oilAbnormal;
    }

    public void setOilAbnormal(String oilAbnormal) {
        this.oilAbnormal = oilAbnormal;
    }

    public String getBump() {
        return bump;
    }

    public void setBump(String bump) {
        this.bump = bump;
    }

    public String getUnusualDrive1() {
        return unusualDrive1;
    }

    public void setUnusualDrive1(String unusualDrive1) {
        this.unusualDrive1 = unusualDrive1;
    }

    public String getUnusualDrive2() {
        return unusualDrive2;
    }

    public void setUnusualDrive2(String unusualDrive2) {
        this.unusualDrive2 = unusualDrive2;
    }

    public String getEnterErea() {
        return enterErea;
    }

    public void setEnterErea(String enterErea) {
        this.enterErea = enterErea;
    }

    public String getEnterRoute() {
        return enterRoute;
    }

    public void setEnterRoute(String enterRoute) {
        this.enterRoute = enterRoute;
    }

    public String getPtz() {
        return ptz;
    }

    public void setPtz(String ptz) {
        this.ptz = ptz;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBlng() {
        return blng;
    }

    public void setBlng(String blng) {
        this.blng = blng;
    }

    public String getBlat() {
        return blat;
    }

    public void setBlat(String blat) {
        this.blat = blat;
    }

    public String getGlng() {
        return glng;
    }

    public void setGlng(String glng) {
        this.glng = glng;
    }

    public String getGlat() {
        return glat;
    }

    public void setGlat(String glat) {
        this.glat = glat;
    }

    public String getGaddress() {
        return gaddress;
    }

    public void setGaddress(String gaddress) {
        this.gaddress = gaddress;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getSummileage() {
        return summileage;
    }

    public void setSummileage(String summileage) {
        this.summileage = summileage;
    }

    public String getGpstime() {
        return gpstime;
    }

    public void setGpstime(String gpstime) {
        this.gpstime = gpstime;
    }

    public String getGpsflag() {
        return gpsflag;
    }

    public void setGpsflag(String gpsflag) {
        this.gpsflag = gpsflag;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }
}
