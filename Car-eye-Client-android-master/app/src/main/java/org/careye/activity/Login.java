package org.careye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonObject;

import org.careye.bll.PrefBiz;
import org.careye.net.OkhttpNET;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.NetworkUtil;
import org.careye.utils.Tools;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Login extends Activity implements OnClickListener{
	public final static String TAG = "Login";
	private Button btn_submit;
	private EditText et_user,et_pwd,login_et_ip;
	private Handler handler;
	private PrefBiz prefBiz;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//	if (MyIntentService.instance == null) {
		//		Intent intent=new Intent(this,MyIntentService.class);
		//		   startService(intent);
		//	}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Log.i(TAG, "Login-onCreate():");

		prefBiz = new PrefBiz(this);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == Constants.WHAT_HTTPOK){
					try {
						Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
						prefBiz.putStringInfo(Constants.PREF_LOGIN_NAME, userNmae);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_PW, userPW);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_IP, userip);
						prefBiz.putBooleanInfo(Constants.PREF_IS_LOGIN, true);

						Intent intent = new Intent(Login.this, MainActivity.class);
						startActivity(intent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(msg.what == Constants.WHAT_JSON_PARSE_ERROR_B){
					Toast.makeText(Login.this,"json解析错误",Toast.LENGTH_SHORT).show();
				}
			}
		};
		setupViews();
		addListener();
	}

	private void setupViews() {
		// TODO Auto-generated method stub
		btn_submit = (Button) findViewById(R.id.login_btn_submit);
		et_user = (EditText) findViewById(R.id.login_et_user);
		et_pwd = (EditText) findViewById(R.id.login_et_pwd);
		login_et_ip = (EditText) findViewById(R.id.login_et_ip);
		//test
//		et_user.setText("admin");
//		et_pwd.setText("123456");
	String userName = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
	String pw = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
	String ip = prefBiz.getStringInfo(Constants.PREF_LOGIN_IP, "");
		if(! Tools.isNull(userName)){
			et_user.setText(userName);

		}
		if(! Tools.isNull(pw)){
			et_pwd.setText(pw);

		}
		if(! Tools.isNull(ip)){
			login_et_ip.setText(ip);

		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn_submit:
			if(! NetworkUtil.getInstance().isConnect(Login.this)){
				Toast.makeText(this,"当前网络不可用,请检查网络设置!",Toast.LENGTH_SHORT).show();
			}else{
				login();
			}
			break;
		case R.id.tv_find_register:
		/*	Intent intentRegister = new Intent(Login.this, LoginRegisterActivity.class);
			//			intentRegister.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intentRegister, Constants.BACK_LOGIN_RE);*/
			break;

		}
	}

	private void login() {
		if(!NetworkUtil.getInstance().isConnect(Login.this) ){

			Toast.makeText(this,"当前网络不可用,请检查网络设置!",Toast.LENGTH_SHORT).show();
		} else {
			String name = et_user.getText().toString().trim();
			String pwd = et_pwd.getText().toString().trim();
			String ip = login_et_ip.getText().toString().trim();
			if (Tools.isNull(name) || Tools.isNull(pwd)) {

				Toast.makeText(this, "帐号和密码不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}
			if ( Tools.isNull(ip)) {

				Toast.makeText(this, "ip不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}
			userNmae =  name;
			userPW = pwd;
			userip = ip;

			Retrofit retrofit = BaseRequest.getInstance().getRetrofit(userip);
			CmsRequest.DeptTree request = retrofit.create(CmsRequest.DeptTree.class);
			String tradeno = DateUtil.getTodayDate(DateUtil.df6);
			String sign = MD5Util.MD5Encode(userNmae + userPW + tradeno, "utf-8");
			JsonObject body = new JsonObject();
			body.addProperty("username", userNmae);
			body.addProperty("tradeno", tradeno);
			body.addProperty("password", userPW);
			body.addProperty("sign", sign);
			Call<JsonObject> call = request.deptTree(body);
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					JsonObject resultObj = response.body();
					if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
						Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
						prefBiz.putStringInfo(Constants.PREF_LOGIN_NAME, userNmae);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_PW, userPW);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_IP, userip);
						prefBiz.putBooleanInfo(Constants.PREF_IS_LOGIN, true);

						Intent intent = new Intent(Login.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(Login.this,resultObj.get("resultMsg").getAsString(), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					String message = t.getMessage();
				}
			});
		}
	}

	private void doLogin() {
		// TODO Auto-generated method stub
		 if(!NetworkUtil.getInstance().isConnect(Login.this) ){

			Toast.makeText(this,"当前网络不可用,请检查网络设置!",Toast.LENGTH_SHORT).show();
		} else {
			String name = et_user.getText().toString().trim();
			String pwd = et_pwd.getText().toString().trim();
			if (Tools.isNull(name) || Tools.isNull(pwd)) {

				Toast.makeText(this, "帐号和密码不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}
			userNmae =  name;
			 userPW = pwd;
			// 登录处理事件
			final String  httpData =  getHttdata (name,pwd);

			OkhttpNET.postNetgetHttp(Constants.HTTP_URL+Constants.ACTION_LOGIN,httpData,handler);
			Log.i(TAG,TAG+"httpData:"+httpData);
		}
	}
    private String getHttdata(String name,String pwd){
		String loghttpStr = null;
		try {
		/*  username	用户名	string	是	平台用户名
		tradeno	流水号	string	是	客户端自由定，可以取时间戳
		sign	签名	string	是	详细见签名规则*/
			Map<String,String> map = new HashMap<String,String>();
			map.put("tradeno", DateUtil.getNowDateToMeter());
			map.put("username", name);//
			map.put("password", pwd);
			loghttpStr = OkhttpNET.getRequestJson(map, R.array.network_eye_login);
		} catch (Exception e) {
			e.printStackTrace();
			return"";
		}
		return loghttpStr;
	}
	private void doExit() {
		// TODO Auto-generated method stub
		/*String text = "退出"+getResources().getString(R.string.app_name);
		Builder dialog = new Builder(Login.this);
		dialog.setTitle("退出！").setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				prefBiz.putBooleanInfo(Constants.PREF_IS_LOGIN, false);
				TApplication.getInstance().finishAll();
				dialog.dismiss();
			}
		}).setMessage(text).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).create().show();*/

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	/*	boolean	 isCancalLOgin = prefBiz.getBooleanInfo(Constants.PREF_IS_LOGIN_FROMLOGIN, false);
		if (isCancalLOgin) {
			prefBiz.putBooleanInfo(Constants.PREF_IS_LOGIN_FROMLOGIN, false);
			finish();
		}else {
			doExit();
		}*/
	}
	private Bundle bundle;
	String	userNmae="";
	String	userPW="";
	String	userip="";

}
