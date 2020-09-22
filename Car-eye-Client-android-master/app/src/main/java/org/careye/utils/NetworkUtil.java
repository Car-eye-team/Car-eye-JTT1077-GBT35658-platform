package org.careye.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {

	private static NetworkUtil instance;
	/**
	 * 获取实例
	 * @return
	 */
	public static NetworkUtil getInstance(){
		if(instance == null){
			instance = new NetworkUtil();
		}
		return instance;
	}
	
	/**
	 * 
	 * 获取网络类型
	 * 
	 */
	public int getNetWorkType(Context context) {
		int netType = -1;

		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}

		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}

		return netType;
	} 
	
	/**
	 * 
	 * 检测网络是否连接
	 * 
	 */
	public boolean isConnect(Context context) {
		boolean flag = false;

		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (null != info && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						flag = true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}

		return flag;
	}

	public static String packageName(Context context) { PackageManager manager = context.getPackageManager();
		String name = null;

		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			name = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return name;
	}
}
