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

public class Welcome extends AppCompatActivity {
    public static final String TAG = "Welcome";
    private Handler handler;
    private PrefBiz prefBiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        //Typeface type= Typeface.createFromAsset(getAssets(),"fonts/FZSTK.TTF");

        prefBiz = new PrefBiz(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                super.handleMessage(msg);
                int what = msg.what;

            }
        };



            goOnActivity();

    }


    void goOnActivity(){
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {


              boolean firstTime = prefBiz.getBooleanInfo(Constants.PREF_FIRST_TIME, true);
                Intent intent = null;

              boolean isLogin = prefBiz.getBooleanInfo(Constants.PREF_IS_LOGIN, false);

                    Log.i(TAG, "Login-Constants.PREF_IS_LOGIN"+isLogin);
                    if(isLogin){
                           int userId = prefBiz.getIntInfo(Constants.PREF_USER_ID, -1);
                            String userName = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
                            intent = new Intent(Welcome.this, MainActivity.class);
                    }else{
                        intent = new Intent(Welcome.this, Login.class);
                    }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//				}
            }
        }, 1000);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

