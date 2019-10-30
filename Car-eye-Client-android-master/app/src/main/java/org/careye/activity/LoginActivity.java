/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonObject;

import org.careye.bll.PrefBiz;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.NetworkUtil;
import org.careye.utils.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 登录页
 * */
public class LoginActivity extends Activity implements OnClickListener {
	public final static String TAG = "LoginActivity";

	private Button btn_submit;
	private EditText et_user,et_pwd,login_et_ip;

	private PrefBiz prefBiz;

	String userName = "";
	String userPW = "";
	String userIP = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Log.i(TAG, "LoginActivity-onCreate():");

		prefBiz = new PrefBiz(this);

		setupViews();
		addListener();
	}

	private void setupViews() {
		btn_submit = (Button) findViewById(R.id.login_btn_submit);
		et_user = (EditText) findViewById(R.id.login_et_user);
		et_pwd = (EditText) findViewById(R.id.login_et_pwd);
		login_et_ip = (EditText) findViewById(R.id.login_et_ip);

		// TODO
		et_user.setText("admin");
		et_pwd.setText("car-eye2019");
		login_et_ip.setText("www.liveoss.com");
//		login_et_ip.setText("120.79.67.102");

		String userName = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
		String pw = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
		String ip = prefBiz.getStringInfo(Constants.PREF_LOGIN_IP, "");

		if (!Tools.isNull(userName)) {
			et_user.setText(userName);
		}

		if (!Tools.isNull(pw)) {
			et_pwd.setText(pw);
		}

		if (!Tools.isNull(ip)) {
			login_et_ip.setText(ip);
		}
	}

	private void addListener() {
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_submit:
			if (! NetworkUtil.getInstance().isConnect(LoginActivity.this)) {
				Toast.makeText(this,"当前网络不可用,请检查网络设置!",Toast.LENGTH_SHORT).show();
			} else {
				login();
			}
			break;
		case R.id.tv_find_register:
			// TODO
			break;
		case R.id.tv_find_password:
			// TODO
			break;
		}
	}

	private void login() {
		if (!NetworkUtil.getInstance().isConnect(LoginActivity.this)) {
			Toast.makeText(this,"当前网络不可用,请检查网络设置!",Toast.LENGTH_SHORT).show();
		} else {
			String name = et_user.getText().toString().trim();
			String pwd = et_pwd.getText().toString().trim();
			String ip = login_et_ip.getText().toString().trim();

			if (Tools.isNull(name) || Tools.isNull(pwd)) {
				Toast.makeText(this, "帐号和密码不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}

			if (Tools.isNull(ip)) {
				Toast.makeText(this, "ip不能为空！",Toast.LENGTH_SHORT).show();
				return;
			}

			userName = name;
			userPW = pwd;
			userIP = ip;

			Retrofit retrofit = BaseRequest.getInstance().getRetrofit(userIP);
			CmsRequest.UserLogin request = retrofit.create(CmsRequest.UserLogin.class);

			String tradeno = DateUtil.getTodayDate(DateUtil.df6);
			String sign = MD5Util.MD5Encode(userName + userPW + tradeno, "utf-8");

			JsonObject body = new JsonObject();
			body.addProperty("username", userName);
			body.addProperty("tradeno", tradeno);
			body.addProperty("password", userPW);
			body.addProperty("sign", sign);

			Call<JsonObject> call = request.userLogin(body);

			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					JsonObject resultObj = response.body();

					if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
						Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

						prefBiz.putStringInfo(Constants.PREF_LOGIN_NAME, userName);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_PW, userPW);
						prefBiz.putStringInfo(Constants.PREF_LOGIN_IP, userIP);
						prefBiz.putBooleanInfo(Constants.PREF_IS_LOGIN, true);

						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						if (resultObj != null) {
							Toast.makeText(LoginActivity.this, resultObj.get("resultMsg").getAsString(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}
					}
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e(TAG, t.getMessage());
				}
			});
		}
	}
}
