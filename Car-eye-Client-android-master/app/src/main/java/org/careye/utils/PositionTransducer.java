/**    
 * Description: GPS车联网平台    
 * 文件名：PositionTransducer.java   
 * 版本信息：    
 * 日期：2015-12-26  
 * Copyright 深圳远佳智慧科技有限公司 Copyright (c) 2015     
 * 版权所有    
 *    
 */
package org.careye.utils;

/**
 *     
 * 项目名称：CVP    
 * 类名称：PositionTransducer    
 * 类描述：    
 * 创建人：Administrator    
 * 创建时间：2015-12-26 下午03:07:09    
 * 修改人：Administrator    
 * 修改时间：2015-12-26 下午03:07:09    
 * 修改备注：    
 * @version 1.0  
 *     
 */
public class PositionTransducer {
	
	static double pi = 3.14159265358979324;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	public final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	
	/**
	 * GPS坐标转百度坐标
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static double[] gps2bd(double lat, double lon) {
		double[] wgs2gcj = wgs2gcj(lat, lon);
		double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
		return gcj2bd;
	}
	
	public static double[] gcj2bd(double lat, double lon) {
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new double[] { bd_lat, bd_lon };
	}
	
	public static double[] bd2gcj(double lat, double lon) {
		double x = lon - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lat, gg_lon };
	}
	
	public static double[] wgs2gcj(double lat, double lon) {
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		double[] loc = { mgLat, mgLon };
		return loc;
	}
	
	private static double transformLat(double lat, double lon) {
		double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi  / 30.0)) * 2.0 / 3.0;
		return ret;
	}
	
	private static double transformLon(double lat, double lon) {
		double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}
	
	public static void main(String[] args) {
		double[] lnglatd = PositionTransducer.gps2bd(22.578573,113.864585);
		double lng = lnglatd[1];
		double lat = lnglatd[0];
		System.out.println(lng+","+lat);
//		BaiDuInfo binfo = BaiDuHttp.getBaiDuInfo(22.733053,114.025842);
//		System.out.println("百度地址..."+JSON.toJSONString(binfo));
	}
	
}

