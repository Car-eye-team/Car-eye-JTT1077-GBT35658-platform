package org.careye.utils;

import com.careye.CarEyeClient.R;

/**
 * 汽车状态工具类
 *
 * @author wlh
 */
public class CarStatusUtil {

	/**
	 * 获取汽车状态
	 * @param status	状态码
	 * @return 状态名称
	 */
	public static String getStatus(int status){
		String carStatus = "";

		if (status == Constants.STATUS_ONLINE) {
			carStatus = Constants.ONLINE;
		} else if (status == Constants.STATUS_ALARM) {
			carStatus = Constants.ALARM;
		} else if (status == Constants.STATUS_OFF_LINE) {
			carStatus = Constants.OFF_LINE;
		} else if(status == Constants.STATUS_PARK) {
			carStatus = Constants.PARK;
		} else if(status == Constants.STATUS_RUN) {
			carStatus = Constants.RUN;
		} else if(status == Constants.STATUS_SHUT_DOWN) {
			carStatus = Constants.SHUT_DOWN;
		} else if(status == Constants.STATUS_LONG_OFF_LINE) {
			carStatus = Constants.LONG_OFF_LINE;
		} else if(status == Constants.STATUS_NO_POSITION) {
			carStatus = Constants.NO_POSITION;
		}

		return carStatus;
	}

	/**
	 * 获取汽车状态  轨迹
	 * @param statusS	状态码 //	0 全部1长时间离线2离线3 熄火4停车 5 行驶 6 报警 7在线 8 未定位
	 * @return 状态名称
	 */
	public static String getStatusTr(String statusS){
		String carStatus = "";
		String status = statusS.toString().trim();

		if (status.equals("7")) {
			carStatus = Constants.ONLINE;
		} else if(status.equals("6")) {
			carStatus = Constants.ALARM;
		} else if(status.equals("2")) {
			carStatus = Constants.OFF_LINE;
		} else if(status.equals("4")) {
			carStatus = Constants.PARK;
		} else if(status.equals("5")) {
			carStatus = Constants.RUN;
		} else if(status.equals("3")) {
			carStatus = Constants.SHUT_DOWN;
		} else if(status.equals("1")) {
			carStatus = Constants.LONG_OFF_LINE;
		} else if(status.equals("8")) {
			carStatus = Constants.NO_POSITION;
		}

		return carStatus;
	}

	/**
	 * 获取汽车状态图标id
	 * @param status	状态码 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位
	 * @return DrawableId
	 */
	public static String getDrawableIdStr(int status){
		String statusStr = "";

		switch (status) {
			case 8:
				statusStr = "未定位";
				break;
			case 1:
				statusStr = "长时间离线";
				break;
			case 2:
				statusStr = "离线";
				break;
			case 3:
				statusStr = "熄火";
				break;
			case 4:
				statusStr = "停车";
				break;
			case 5:
				statusStr = "行驶";
				break;
			case 6:
				statusStr = "报警";
				break;
			case 7:
				statusStr = "在线";
				break;
		}

		return statusStr;
	}

	/**
	 * 获取汽车状态图标id
	 * @param status	状态码 1：长时间离线 2：离线 3：熄火 4：停车 5：行驶 6：报警 7：在线 8：未定位
	 * @return DrawableId
	 */
	public static int getDrawableId(int status){
		int drawableId;

		if (status == Constants.STATUS_ONLINE) {
			drawableId = R.drawable.device_online;
		} else if(status == Constants.STATUS_ALARM) {
			drawableId = R.drawable.device_alarm;
		} else if(status == Constants.STATUS_OFF_LINE) {
			drawableId = R.drawable.device_offline;
		} else if(status == Constants.STATUS_PARK) {
			drawableId = R.drawable.device_stopaccon;
		} else if(status == Constants.STATUS_RUN) {
			drawableId = R.drawable.device_io;
		} else if(status == Constants.STATUS_SHUT_DOWN) {
			drawableId = R.drawable.device_stopaccoff;
		} else if(status == Constants.STATUS_LONG_OFF_LINE) {
			drawableId = R.drawable.device_offline;
		} else if(status == Constants.STATUS_NO_POSITION) {
			drawableId = R.drawable.device_nogps;
		} else {
			drawableId = R.drawable.device_offline;
		}

		return drawableId;
	}

	/**
	 * 获取汽车方向
	 * @param direction	方向数值
	 * @return 状态名称
	 */
	public static String parseDirection(String direction) {
		double direc;
		String direcStr;
		String flag = "方向";

		if (!Tools.isNull(direction)) {
			direc = Double.parseDouble(direction);

			if (direc == 0) {
				direcStr = "正北" + flag;
			} else if(direc < 90) {
				direcStr = "东北" + flag;
			} else if(direc == 90) {
				direcStr = "正东" + flag;
			} else if(direc < 180) {
				direcStr = "东南" + flag;
			} else if(direc == 180) {
				direcStr = "正南" + flag;
			} else if(direc < 270) {
				direcStr = "西南" + flag;
			} else if(direc == 270) {
				direcStr = "正西" + flag;
			} else if(direc < 360) {
				direcStr = "西北" + flag;
			} else if(direc == 360) {
				direcStr = "正北" + flag;
			} else {
				direcStr = direction+"数据错误！";
			}
		} else {
			direcStr = direction;
		}

		if (direcStr == null) {
			return "";
		}

		return direcStr;
	}
}
