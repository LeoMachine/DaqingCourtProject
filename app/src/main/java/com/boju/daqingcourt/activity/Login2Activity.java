package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * 会员登录2
 */

public class Login2Activity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.iv_banner)
    ImageView iv_banner;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_card)
    EditText et_card;
    String user_id;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login2;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("实名认证");
        user_id=getIntent().getStringExtra("user_id");
        indexBannerRequest();
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick({R.id.common_iv_back,  R.id.tv_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.tv_sure://确定
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
                    GlideUtils.loadImageView(Login2Activity.this, bannerInfo.getImg_url(), iv_banner);
                } else {
//                    Toast.makeText(getActivity(), httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    //登录接口请求
    private void login() {
        String name = et_name.getText().toString();
        String card = et_card.getText().toString();
        if (StringUtil.isEmpty(name)) {
            Toast.makeText(Login2Activity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(card)) {
            Toast.makeText(Login2Activity.this, "请输入身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().postMapUserInformation(user_id,name, card,new BaseSubscriber<HttpResponse<UserInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                Toast.makeText(Login2Activity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<UserInfo> userInfoHttpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(userInfoHttpResponse));
                if (userInfoHttpResponse.getCode()==0) {
                    Toast.makeText(Login2Activity.this, "成功", Toast.LENGTH_SHORT).show();
                    UserInfo userInfo = userInfoHttpResponse.getData();
                    PropertyUtil.getPropertyUtil().setProperty("user_id",userInfo.getUser_id());
                    PropertyUtil.getPropertyUtil().setProperty("token",userInfo.getToken());
                    finish();
                } else {
                    Toast.makeText(Login2Activity.this, userInfoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
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
    }
}
