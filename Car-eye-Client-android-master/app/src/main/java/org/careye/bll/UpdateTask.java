package org.careye.bll;

import android.os.Handler;

import org.careye.utils.Constants;

import java.util.TimerTask;

/**
 * 定时刷新类
 * @author zxt
 *
 */
public class UpdateTask extends TimerTask{

	private Handler handler;
	
	public UpdateTask(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		handler.obtainMessage(Constants.WHAT_UPDATE_DATA).sendToTarget();
	}
}
