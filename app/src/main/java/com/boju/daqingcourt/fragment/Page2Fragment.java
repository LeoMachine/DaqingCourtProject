package com.boju.daqingcourt.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.AppApplication;
import com.boju.daqingcourt.R;
import com.boju.daqingcourt.activity.BrowserActivity;
import com.boju.daqingcourt.activity.SetupcaseActivity;
import com.boju.daqingcourt.base.BaseFragment;
import com.boju.daqingcourt.bean.ArticleInfo;
import com.boju.daqingcourt.bean.CaseAnyouTypeInfo;
import com.boju.daqingcourt.bean.UserInfo;
import com.boju.daqingcourt.bean.YuangaoInfo;
import com.boju.daqingcourt.citypicker.CityPicker;
import com.boju.daqingcourt.citypicker.CityPicker2;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.DeviceUtil;
import com.boju.daqingcourt.utils.PropertyUtil;
import com.boju.daqingcourt.utils.StringUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;

/**
 * Created by Administrator on 2018/5/3.
 * 在线诉讼
 */

public class Page2Fragment extends BaseFragment {
    private Unbinder unbinder;
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.rl_container)
    LinearLayout rl_container;
    @BindView(R.id.tv_mobile)
    EditText tv_mobile;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_tel)
    EditText et_tel;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.et_address_details)
    EditText et_address_details;
    @BindView(R.id.tv_you_address)
    TextView tv_you_address;
    @BindView(R.id.et_you_address_details)
    EditText et_you_address_details;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private CityPicker2 mCityPicker;
    private CityPicker2 mCityPicker2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_page2, null);
        unbinder = ButterKnife.bind(this, view);
        tv_common_title_name.setText("补充原告信息");
        initCityData(false);
        //初始化view的各控件
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
        }
    }

    private void initCityData(boolean isFirst){
        mCityPicker = new CityPicker2(getActivity(), rl_container,isFirst)
                .setOnCitySelectListener(new CityPicker2.OnCitySelectListener() {
                    @Override
                    public void onCitySelect(String province, String city, String county) {
                        String address = province + "-" + city + "-" + county;
                        tv_address.setText(address);
                    }
                });
        mCityPicker2 = new CityPicker2(getActivity(), rl_container,isFirst)
                .setOnCitySelectListener(new CityPicker2.OnCitySelectListener() {
                    @Override
                    public void onCitySelect(String province, String city, String county) {
                        String address = province + "-" + city + "-" + county;
                        tv_you_address.setText(address);
                    }
                });
    }

    @OnClick({R.id.tv_sure, R.id.tv_address, R.id.tv_you_address})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure://提交
                yuangaoRequest();
                break;
            case R.id.tv_address://选择地址
                mCityPicker.show();
                break;
            case R.id.tv_you_address://选择地址
                mCityPicker2.show();
                break;
        }
    }

    //意见反馈接口请求
    private void yuangaoRequest() {
        String mobile = tv_mobile.getText().toString();
        String email = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        String code = et_code.getText().toString();
        String address = tv_address.getText().toString();
        String addressDetails = et_address_details.getText().toString();
        String youAddress = tv_you_address.getText().toString();
        String youAddressDetails = et_you_address_details.getText().toString();

        if (StringUtil.isEmpty(mobile)) {
            Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(email)) {
            Toast.makeText(getActivity(), "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(code)) {
            Toast.makeText(getActivity(), "请输入邮编", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(address)) {
            Toast.makeText(getActivity(), "请选择当前所在地", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(addressDetails)) {
            Toast.makeText(getActivity(), "请输入当前所在详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(youAddress)) {
            Toast.makeText(getActivity(), "请选择邮寄送达地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(youAddressDetails)) {
            Toast.makeText(getActivity(), "请输入邮寄送达详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        String now_province, now_city, now_area;
        String[] nowAddressArray = address.split("-");
        now_province = nowAddressArray[0];
        now_city = nowAddressArray[1];
        now_area = nowAddressArray[2];

        String you_province, you_city, you_area;
        String[] youAddressArray = youAddress.split("-");
        you_province = youAddressArray[0];
        you_city = youAddressArray[1];
        you_area = youAddressArray[2];
        RetrofitClient.getInstance().postMapYuangao(mobile, email, tel, code, now_province, now_city, now_area, addressDetails,
                you_province, you_city, you_area, youAddressDetails, new BaseSubscriber<HttpResponse<String>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        hideWaitDialog();
                        Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onNext(HttpResponse<String> httpResponse) {
                        hideWaitDialog();
                        Gson gson = new Gson();
                        Log.i("onNext", gson.toJson(httpResponse));
                        if (httpResponse.getCode() == 0) {
//                            Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                            String case_id=httpResponse.getData();
                            Intent intent= new Intent(getActivity(), SetupcaseActivity.class);
                            intent.putExtra("case_id",case_id);
                            startActivity(intent);
                            initCityData(false);
                        } else {
                            Toast.makeText(getActivity(), httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        showWaitDialog();
    }

    //获取原告信息
    private void getMyInformationRequest() {
        RetrofitClient.getInstance().postMapMyInformation(new BaseSubscriber<HttpResponse<UserInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<UserInfo> userInfoHttpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(userInfoHttpResponse));
                if (userInfoHttpResponse.getCode()==0) {
                    UserInfo userInfo = userInfoHttpResponse.getData();
                    setUserInformation(userInfo);
                } else{
//                    Toast.makeText(getActivity(), userInfoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //设置用户信息
    private void setUserInformation(UserInfo userInfo) {
      tv_mobile.setText(userInfo.getMobile());
      et_email.setText(userInfo.getEmail());
      et_code.setText(userInfo.getCode());
      et_tel.setText(userInfo.getTel());
      tv_address.setText(userInfo.getNow_province()+"-"+userInfo.getNow_city()+"-"+userInfo.getNow_area());
      et_address_details.setText(userInfo.getNow_address());
      tv_you_address.setText(userInfo.getYou_province()+"-"+userInfo.getYou_city()+"-"+userInfo.getYou_area());
      et_you_address_details.setText(userInfo.getYou_address());
      if(StringUtil.isEmpty(userInfo.getNow_province())){
          tv_address.setText("黑龙江省-大庆市-萨尔图区");
          tv_you_address.setText("黑龙江省-大庆市-萨尔图区");
          initCityData(true);
      }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!StringUtil.isEmpty(PropertyUtil.getPropertyUtil().getProperty("token"))){
            getMyInformationRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
