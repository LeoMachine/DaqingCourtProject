package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.ArticleInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.StringUtil;
import com.google.gson.Gson;
import com.zzhoujay.richtext.RichText;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 * 文书填写
 */

public class DocumentWriteActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_document_write;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("文书填写");

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
