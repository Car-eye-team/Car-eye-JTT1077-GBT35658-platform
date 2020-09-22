package org.careye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.careye.CarEyeClient.R;
import com.tencent.bugly.beta.Beta;

import org.careye.bll.PrefBiz;
import org.careye.utils.Constants;
import org.careye.utils.NetworkUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_pleace_inputinfor;
    Button btn_taguser,btn_version;
    TextView tv_carsettings_carnumber,tv_carsettings_version;

    private ImageView device_list_iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initListener();
    }

    private void initView() {
        device_list_iv_back = findViewById(R.id.device_list_iv_back);
        et_pleace_inputinfor = (EditText) findViewById(R.id.et_pleace_inputinfor);
        btn_taguser = (Button) findViewById(R.id.btn_taguser);
        btn_version = (Button) findViewById(R.id.btn_version);
        tv_carsettings_carnumber = (TextView) findViewById(R.id.tv_carsettings_carnumber);
        tv_carsettings_version = (TextView) findViewById(R.id.tv_carsettings_version);

        et_pleace_inputinfor.setInputType(InputType.TYPE_CLASS_PHONE);
        btn_taguser.setOnClickListener(this);
        btn_version.setOnClickListener(this);

        String useName = new PrefBiz(this).getStringInfo(Constants.PREF_LOGIN_NAME, "--");
        tv_carsettings_carnumber.setText(useName);
        tv_carsettings_version.setText("v" + NetworkUtil.packageName(this));
    }

    private void initListener() {
        device_list_iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.device_list_iv_back:
                finish();
                break;
            case R.id.btn_taguser:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_version:
                Beta.checkUpgrade();
                break;
        }
    }
}
