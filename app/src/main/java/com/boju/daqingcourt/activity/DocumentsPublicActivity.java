package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.DocumentsPublicAdapter;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.DocumentInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.widget.empty.EmptyLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/27.
 * 法院指引
 */

public class DocumentsPublicActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.rcl_view)
    RecyclerView rcl_view;
    @BindView(R.id.layout_empty)
    EmptyLayout layout_empty;
    List<DocumentInfo> dateAllList = new ArrayList<>();
    DocumentsPublicAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_document_public;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("法院指引");
        initRclAdapter();
    }

    @Override
    public void initData() {
        super.initData();
    }
    @OnClick({R.id.common_iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back://返回
                finish();
                break;
        }
    }

    private void initRclAdapter() {
        rcl_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DocumentsPublicAdapter(this, dateAllList);
        rcl_view.setAdapter(adapter);
//        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider));
        rcl_view.addItemDecoration(dividerItemDecoration);
        adapter.setOnItemClickListener(new DocumentsPublicAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(DocumentsPublicActivity.this, ArticleDetailsActivity.class);
                intent.putExtra("title", "详情");
                intent.putExtra("category_id", "");
                intent.putExtra("id", dateAllList.get(position).getId() + "");
                startActivity(intent);
            }
        });
        dataListRequest();
    }
    //接口请求
    private void dataListRequest() {
        layout_empty.setErrorType(EmptyLayout.NETWORK_LOADING);
        RetrofitClient.getInstance().postDocumentInfoList(new BaseSubscriber<HttpResponse<List<DocumentInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                layout_empty.setErrorType(EmptyLayout.NETWORK_ERROR);
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(HttpResponse httpResponse) {
                layout_empty.setErrorType(EmptyLayout.HIDE_LAYOUT);
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode()==0) {
//                    Toast.makeText(MyTabGuanzhuActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    List<DocumentInfo> dataList = (List<DocumentInfo>) httpResponse.getData();
                    dateAllList.addAll(dataList);
                    adapter.notifyDataSetChanged();
                } else {
                    layout_empty.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }
        });
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
