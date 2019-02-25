package org.careye.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * 显示工具�?
 */
public class ToastUtil {
	/**
	 * 
	 * @param context 上下文对�?
	 * @param msg 显示内容
	 */
	public static void showToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void longToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showToast(Context context, int resId){
		String msg = context.getResources().getString(resId);
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void longToast(Context context, int resId){
		String msg = context.getResources().getString(resId);
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
