package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.boju.daqingcourt.AppManager;


/**
 * Created by Administrator on 2017/11/13.
 * 启动页
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        AppManager.getAppManager().addActivity(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(SPUtils.getSharedPreferencesBooble("isGuide")){
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }else{
//                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//                    SPUtils.saveSharedPreferencesBooble("isGuide",true);
//                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
