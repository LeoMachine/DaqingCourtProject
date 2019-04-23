package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.DetailFsalvRclAdapter;
import com.boju.daqingcourt.adapter.DetailsCaseBeigaoRclAdapter;
import com.boju.daqingcourt.adapter.DetailsImageRclAdapter;
import com.boju.daqingcourt.adapter.DetailsSusongRclAdapter;
import com.boju.daqingcourt.adapter.ImageRclAdapter;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.CaseInfoDetails;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.StringUtil;
import com.google.gson.Gson;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 * 案件详情
 */

public class CaseDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_statusshow)
    TextView tv_statusshow;
    @BindView(R.id.tv_beizhu)
    TextView tv_beizhu;
    @BindView(R.id.tv_add_time)
    TextView tv_add_time;

    @BindView(R.id.tv_qisushow)
    TextView tv_qisushow;
    @BindView(R.id.tv_categoryshow)
    TextView tv_categoryshow;
    @BindView(R.id.tv_fayuan)
    TextView tv_fayuan;
    @BindView(R.id.tv_yiju)
    TextView tv_yiju;
    @BindView(R.id.rcl_beigao)
    RecyclerView rcl_beigao;
    @BindView(R.id.rcl_susong)
    RecyclerView rcl_susong;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.rcl_falv)
    RecyclerView rcl_falv;
    @BindView(R.id.rcl_images)
    RecyclerView rcl_images;
    @BindView(R.id.tv_remark)
    TextView tv_remark;
    @BindView(R.id.tv_images_count)
    TextView tv_images_count;
    int case_id;
    List<CaseInfoDetails.BeiBean> caseBeigaoInfoList = new ArrayList<>();
    List<CaseInfoDetails.SusongBean> susongBeanArrayList = new ArrayList<>();
//    List<CaseInfoDetails.FalvBean> falvBeanArrayList = new ArrayList<>();
    List<String> imagePathList = new ArrayList<>();
    CaseInfoDetails.YuanBean yuanBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_details;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("案件详情");
        case_id = getIntent().getIntExtra("case_id", 0);
        getCaseDetailsRequest();
    }

    private void setDataDetails(CaseInfoDetails caseInfoDetails) {
        tv_qisushow.setText(caseInfoDetails.getQisushow());
        tv_number.setText(caseInfoDetails.getNumber());
        tv_statusshow.setText(caseInfoDetails.getStatusshow());
        String beizhu=caseInfoDetails.getBeizhu();
        if(StringUtil.isEmpty(beizhu)){
            tv_beizhu.setText("无");
        }else{
            tv_beizhu.setText(beizhu);
        }
        tv_add_time.setText(caseInfoDetails.getAdd_time());
        tv_categoryshow.setText(caseInfoDetails.getCategoryshow());
        tv_fayuan.setText(caseInfoDetails.getFayuan());
        tv_yiju.setText(caseInfoDetails.getYiju());
        caseBeigaoInfoList=caseInfoDetails.getBei();
        initBeigaoRclAdapter();
        susongBeanArrayList=caseInfoDetails.getSusong();
        initSusongRclAdapter();
        tv_content.setText(caseInfoDetails.getContent());
//        falvBeanArrayList=caseInfoDetails.getFalv();
//        initfalvRclAdapter();
        imagePathList=caseInfoDetails.getImages();
        tv_images_count.setText(imagePathList.size()+"");
        initRclImages();
        tv_remark.setText(caseInfoDetails.getRemark());
        yuanBean=caseInfoDetails.getYuan();
    }

    @OnClick({R.id.common_iv_back,R.id.tv_see})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back://返回
                finish();
                break;
            case R.id.tv_see://原告查看
                Intent intentYuna=  new Intent(this, CaseYuangaoDetailsActivity.class);
                intentYuna.putExtra("yuanBean",yuanBean);
                startActivity(intentYuna);
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
    }
    //被告添加adapter
    private void initBeigaoRclAdapter() {
        rcl_beigao.setLayoutManager(new LinearLayoutManager(this));
        DetailsCaseBeigaoRclAdapter caseBeigaoAddRclAdapter = new DetailsCaseBeigaoRclAdapter(this, caseBeigaoInfoList);
        rcl_beigao.setAdapter(caseBeigaoAddRclAdapter);
        rcl_beigao.setNestedScrollingEnabled(false);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_beigao.addItemDecoration(dividerItemDecoration);
        caseBeigaoAddRclAdapter.setOnViewClickListener(new DetailsCaseBeigaoRclAdapter.OnViewClickListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                Intent intentBeigao= new Intent(CaseDetailsActivity.this, CaseBeigaoDetailsActivity.class);
                intentBeigao.putExtra("bei_id",caseBeigaoInfoList.get(position).getId());
                startActivity(intentBeigao);
            }
        });
    }

    //诉讼adapter
    private void initSusongRclAdapter() {
        rcl_susong.setLayoutManager(new LinearLayoutManager(this));
        DetailsSusongRclAdapter detailsSusongRclAdapter = new DetailsSusongRclAdapter(this, susongBeanArrayList);
        rcl_susong.setAdapter(detailsSusongRclAdapter);
        rcl_susong.setNestedScrollingEnabled(false);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_susong.addItemDecoration(dividerItemDecoration);
        detailsSusongRclAdapter.setOnViewClickListener(new DetailsSusongRclAdapter.OnViewClickListener() {
            @Override
            public void onItemViewClick(View view, int position) {

            }
        });
    }
    //法律依据adapter
//    private void initfalvRclAdapter() {
//        rcl_falv.setLayoutManager(new LinearLayoutManager(this));
//        DetailFsalvRclAdapter detailFsalvRclAdapter = new DetailFsalvRclAdapter(this, falvBeanArrayList);
//        rcl_falv.setAdapter(detailFsalvRclAdapter);
//        rcl_falv.setNestedScrollingEnabled(false);
//        //分隔线
////        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
////        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
////        rcl_falv.addItemDecoration(dividerItemDecoration);
//        detailFsalvRclAdapter.setOnViewClickListener(new DetailFsalvRclAdapter.OnViewClickListener() {
//            @Override
//            public void onItemViewClick(View view, int position) {
//
//            }
//        });
//    }
    //获取案件信息
    private void getCaseDetailsRequest() {
        RetrofitClient.getInstance().postMaCaseInfoDetails(case_id, new BaseSubscriber<HttpResponse<CaseInfoDetails>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse<CaseInfoDetails> userInfoHttpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(userInfoHttpResponse));
                if (userInfoHttpResponse.getCode() == 0) {
                    CaseInfoDetails caseInfoDetails = userInfoHttpResponse.getData();
                    setDataDetails(caseInfoDetails);
                } else {
                    Toast.makeText(CaseDetailsActivity.this, userInfoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //图片展示
    public void initRclImages() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rcl_images.setLayoutManager(manager);
        rcl_images.setNestedScrollingEnabled(false);
        DetailsImageRclAdapter imagesAdapter = new DetailsImageRclAdapter(this, imagePathList);
        rcl_images.setAdapter(imagesAdapter);
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
