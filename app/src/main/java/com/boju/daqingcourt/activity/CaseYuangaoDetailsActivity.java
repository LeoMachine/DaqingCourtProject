package com.boju.daqingcourt.activity;

import android.view.View;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.CaseInfoDetails;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 * 案件-原告详情
 */

public class CaseYuangaoDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_card)
    TextView tv_card;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_tel)
    TextView tv_tel;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.tv_address)
    TextView tv_address;

    CaseInfoDetails.YuanBean yuanBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_yuangao_details;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("原告信息");
        yuanBean = (CaseInfoDetails.YuanBean) getIntent().getSerializableExtra("yuanBean");
        tv_name.setText(yuanBean.getName());
        tv_card.setText(yuanBean.getCard());
        tv_mobile.setText(yuanBean.getMobile());
        tv_email.setText(yuanBean.getEmail());
        tv_tel.setText(yuanBean.getTel());
        tv_code.setText(yuanBean.getCode());
        tv_address.setText(yuanBean.getNow_province()+yuanBean.getNow_city()+yuanBean.getNow_area()+yuanBean.getNow_address());
    }

    @OnClick({R.id.common_iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back://返回
                finish();
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
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
