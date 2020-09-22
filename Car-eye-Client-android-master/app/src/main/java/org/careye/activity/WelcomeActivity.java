package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.careye.CarEyeClient.R;

import org.careye.bll.PrefBiz;

import org.careye.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {
    public static final String TAG = "WelcomeActivity";

    private Handler handler;
    private PrefBiz prefBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Typeface type= Typeface.createFromAsset(getAssets(),"fonts/FZSTK.TTF");

        prefBiz = new PrefBiz(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        goOnActivity();
    }

    void goOnActivity(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean firstTime = prefBiz.getBooleanInfo(Constants.PREF_FIRST_TIME, true);

                Intent intent;

                boolean isLogin = prefBiz.getBooleanInfo(Constants.PREF_IS_LOGIN, false);
                if (isLogin) {
                    int userId = prefBiz.getIntInfo(Constants.PREF_USER_ID, -1);
                    String userName = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

