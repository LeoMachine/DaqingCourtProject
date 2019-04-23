package com.boju.daqingcourt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.BannerInfo;
import com.boju.daqingcourt.bean.UserInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.GlideUtils;
import com.boju.daqingcourt.utils.PropertyUtil;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.utils.ToastUtil;
import com.boju.daqingcourt.widget.EditTextClearable;
import com.google.gson.Gson;


import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/3/21.
 * 会员登录
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.et_mobile)
    EditTextClearable et_mobile;
    @BindView(R.id.et_verification_code)
    EditTextClearable et_verification_code;
    @BindView(R.id.tv_code_get)
    TextView tv_code_get;
    @BindView(R.id.iv_banner)
    ImageView iv_banner;
    String msg_id;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what >= 0) {
                tv_code_get.setText("已发送" + what + "秒");
                what--;
                mHandler.sendEmptyMessageDelayed(what, 1000);
                tv_code_get.setEnabled(false);
            } else {
                tv_code_get.setText("重新获取");
                tv_code_get.setEnabled(true);
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("登录");
        indexBannerRequest();
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick({R.id.common_iv_back, R.id.tv_code_get, R.id.tv_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.tv_code_get://获取验证码
                sendSmsRequest();
                break;
            case R.id.tv_login://用户端登录
                login();
                break;

        }
    }

    //首页轮播图请求
    private void indexBannerRequest() {
        RetrofitClient.getInstance().postIndexBannerList(new BaseSubscriber<HttpResponse<BannerInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<BannerInfo> httpResponse) {
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    BannerInfo bannerInfo = httpResponse.getData();
                    GlideUtils.loadImageView(LoginActivity.this, bannerInfo.getImg_url(), iv_banner);
                } else {
//                    Toast.makeText(getActivity(), httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //注册--请求验证码接口
    private void sendSmsRequest() {
        String mobile = et_mobile.getText().toString();
        if (StringUtil.isEmpty(mobile)) {
            ToastUtil.shortToast(this, "手机号不能为空");
            return;
        }
        RetrofitClient.getInstance().postMapSendSms(mobile, new BaseSubscriber<HttpResponse<String>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                Toast.makeText(LoginActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<String> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    msg_id = httpResponse.getData();
                    Toast.makeText(LoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(60);
                } else {
                    Toast.makeText(LoginActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showWaitDialog();
    }

    //
//    //登录接口请求
    private void login() {
        String mobile = et_mobile.getText().toString();
        String verificationCode = et_verification_code.getText().toString();
        if (StringUtil.isEmpty(mobile)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(verificationCode)) {
            Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(StringUtil.isEmpty(msg_id)){
//            msg_id="18";
//        }
//        verificationCode="866415";
        RetrofitClient.getInstance().postMapUserLogin(mobile, verificationCode, msg_id, new BaseSubscriber<HttpResponse<UserInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                Toast.makeText(LoginActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<UserInfo> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    UserInfo userInfo = httpResponse.getData();
                    PropertyUtil.getPropertyUtil().setProperty("user_id",userInfo.getUser_id());
                    PropertyUtil.getPropertyUtil().setProperty("token",userInfo.getToken());
                    finish();
                } else if (httpResponse.getCode() == 2) {
                    UserInfo userInfo = httpResponse.getData();
                    Intent intent= new Intent(LoginActivity.this,Login2Activity.class);
                    intent.putExtra("user_id",userInfo.getUser_id());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        showWaitDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
