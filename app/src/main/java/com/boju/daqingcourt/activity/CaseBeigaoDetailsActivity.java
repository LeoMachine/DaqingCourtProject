package com.boju.daqingcourt.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;
import com.boju.daqingcourt.bean.CaseInfoDetails;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 * 案件-原告详情
 */

public class CaseBeigaoDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.ll_beigao_layout1)
    View ll_beigao_layout1;
    @BindView(R.id.ll_beigao_layout2)
    View ll_beigao_layout2;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_card)
    TextView tv_card;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_nation)
    TextView tv_nation;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_address)
    TextView tv_address;


    @BindView(R.id.tv_name_beigao)
    TextView tv_name_beigao;
    @BindView(R.id.tv_faren)
    TextView tv_faren;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_link)
    TextView tv_link;
    @BindView(R.id.tv_mobile_beigao)
    TextView tv_mobile_beigao;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_address_beigao)
    TextView tv_address_beigao;

    int bei_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_beigao_details;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("被告信息");
        bei_id = getIntent().getIntExtra("bei_id", 0);
        getCaseDetailsRequest();
    }

    private void setDataDetails(CaseBeigaoInfo caseInfoDetails) {
        int type = caseInfoDetails.getType();
        if (type == 1) {
            ll_beigao_layout1.setVisibility(View.VISIBLE);
            ll_beigao_layout2.setVisibility(View.GONE);
            tv_name.setText(caseInfoDetails.getName());
            tv_card.setText(caseInfoDetails.getCard());
            tv_sex.setText(caseInfoDetails.getSex());
            tv_nation.setText(caseInfoDetails.getNation());
            tv_mobile.setText(caseInfoDetails.getMobile());
            tv_address.setText(caseInfoDetails.getProvince()+caseInfoDetails.getCity()+caseInfoDetails.getArea()+caseInfoDetails.getAddress());
        } else {
            ll_beigao_layout1.setVisibility(View.GONE);
            ll_beigao_layout2.setVisibility(View.VISIBLE);
            tv_name_beigao.setText(caseInfoDetails.getName());
            tv_faren.setText(caseInfoDetails.getFaren());
            tv_job.setText(caseInfoDetails.getJob());
            tv_link.setText(caseInfoDetails.getLink());
            tv_mobile_beigao.setText(caseInfoDetails.getMobile());
            tv_email.setText(caseInfoDetails.getEmail());
            tv_address_beigao.setText(caseInfoDetails.getReg_province()+caseInfoDetails.getReg_city()+caseInfoDetails.getReg_area()+caseInfoDetails.getReg_address());
        }
    }

    @OnClick({R.id.common_iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back://返回
                finish();
                break;
        }
    }

    //获取案件信息
    private void getCaseDetailsRequest() {
        RetrofitClient.getInstance().postMapBeigaoDetails(bei_id, new BaseSubscriber<HttpResponse<CaseBeigaoInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<CaseBeigaoInfo> userInfoHttpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(userInfoHttpResponse));
                if (userInfoHttpResponse.getCode() == 0) {
                    CaseBeigaoInfo caseInfoDetails = userInfoHttpResponse.getData();
                    setDataDetails(caseInfoDetails);
                } else {
//                    Toast.makeText(getActivity(), userInfoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
