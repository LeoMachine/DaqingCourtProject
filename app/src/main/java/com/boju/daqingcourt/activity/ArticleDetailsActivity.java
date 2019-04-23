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
 * 首页文章详情
 */

public class ArticleDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;

    String  title="",category_id="",id="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_details;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        category_id=intent.getStringExtra("category_id");
        id=intent.getStringExtra("id");
        tv_common_title_name.setText(title);
        getArticleDetailsRequest();

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
    //获取文章信息
    private void getArticleDetailsRequest() {
        showWaitDialog();
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("category_id",category_id);
        if(!StringUtil.isEmpty(id)){
            parameters.put("id",id);
        }
        RetrofitClient.getInstance().postMapArticleDetails(parameters,new BaseSubscriber<HttpResponse<ArticleInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                hideWaitDialog();
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<ArticleInfo> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode()==0) {
                    ArticleInfo articleInfo = httpResponse.getData();
                    tv_title.setText(articleInfo.getTitle());
                    tv_time.setText(articleInfo.getAdd_time()+"    "+articleInfo.getAuthor());
                    tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                    RichText.fromHtml(articleInfo.getContent()).into(tv_content);
                } else {
                    Toast.makeText(ArticleDetailsActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
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
