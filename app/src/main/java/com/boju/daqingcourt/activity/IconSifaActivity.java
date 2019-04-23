package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.IconGonggaoRclAdapter;
import com.boju.daqingcourt.adapter.IconSifaVideoRclAdapter;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.IconGonggaoInfo;
import com.boju.daqingcourt.bean.SifaVideoInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.HttpUtils;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.widget.CustomDatePicker;
import com.boju.daqingcourt.widget.empty.EmptyLayout;
import com.boju.daqingcourt.widget.loadmore.LoadingFooter;
import com.boju.daqingcourt.widget.loadmore.RecyclerViewStateUtils;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
/**
 * Created by Administrator on 2018/7/3.
 * 阳光司法
 */

public class IconSifaActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.rcl_view)
    RecyclerView rcl_view;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.iv_search)
    ImageView iv_search;

    List<SifaVideoInfo> dataAllList = new ArrayList<>();
    int page = 1, page_count = 10;
    boolean isMore = true, isRefresh = false;
    HttpUtils httpUtils;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    String keywords="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sifa;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("阳光司法");
        setHeader();
        initRefresh();
    }

    @Override
    public void initData() {
        super.initData();
    }

    //刷新初始化
    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableFooterTranslationContent(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                isRefresh = true;
                dataListRequest();
            }
        });
        emptyLayoutAdd();
        httpUtils = new HttpUtils(rcl_view, emptyLayout, refreshLayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayoutLoding();
                dataListRequest();
            }
        });
        dataListRequest();
    }

    @OnClick({R.id.common_iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back://返回
                if (VideoPlayerManager.getInstance().onBackPressed()) {
                    finish();
                }
                break;
        }
    }
    private void setHeader() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keywords = et_search.getText().toString();
                    if (StringUtil.isEmpty(keywords)) {
                        Toast.makeText(IconSifaActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    hideSoftKeyBoard();
                    page1Refresh();
                    return true;
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtil.isEmpty(s.toString())) {
                    iv_search.setVisibility(View.GONE);
                } else {
                    iv_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                page1Refresh();
            }
        });

    }
    private void initRclAdapter() {
        rcl_view.setLayoutManager(new LinearLayoutManager(this));
        IconSifaVideoRclAdapter adapter = new IconSifaVideoRclAdapter(this, dataAllList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rcl_view.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        rcl_view.addOnScrollListener(mOnScrollListener);
        rcl_view.setNestedScrollingEnabled(false);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_view.addItemDecoration(dividerItemDecoration);
        //添加头部
        adapter.setOnItemClickListener(new IconSifaVideoRclAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }



    private void page1Refresh() {
        page = 1;
        isRefresh = true;
        dataListRequest();
    }

    //接口请求
    private void dataListRequest() {
        httpUtils.loding(page, isRefresh, isMore);
        keywords = et_search.getText().toString();
        RetrofitClient.getInstance().postIconSifaVideoInfo(page, page_count, keywords, new BaseSubscriber<HttpResponse<List<SifaVideoInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                httpUtils.error(page, isRefresh);
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(HttpResponse<List<SifaVideoInfo>> httpResponse) {
                httpUtils.hideLoding(page, isRefresh);
                refreshState(LoadingFooter.State.Normal);
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
//                    Toast.makeText(MyTabGuanzhuActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    List<SifaVideoInfo> dataList = httpResponse.getData();
                    if (page == 1) {
                        dataAllList.clear();
                        dataAllList.addAll(dataList);
                        initRclAdapter();
                    } else {
                        dataAllList.addAll(dataList);
                        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    if (dataList.size() <= 0) {
                        isMore = false;
                        if (page == 1) {
                            emptyLayout.setErrorType(EmptyLayout.NODATA);
                        } else {
                            refreshState(LoadingFooter.State.TheEnd);
                        }
                    } else {
                        page++;
                        isMore = true;
                    }
                } else {
                    httpUtils.error(page, isRefresh);
                }
            }
        });
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rcl_view);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }
            isRefresh=false;
            if (isMore) {
                refreshState(LoadingFooter.State.Loading);
                dataListRequest();
            }
        }
    };


    public void refreshState(LoadingFooter.State state) {
        if (state == LoadingFooter.State.NetWorkError) {
            RecyclerViewStateUtils.setFooterViewState(this, rcl_view, page_count, state, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataListRequest();
                }
            });
        } else {
            RecyclerViewStateUtils.setFooterViewState(this, rcl_view, page_count, state, null);
        }
    }
    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.getInstance().onBackPressed()) {
            finish();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        VideoPlayerManager.getInstance().onConfigurationChanged(newConfig);//横竖屏切换
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //如果进入详情播放则不暂停视频释放资源//为空内部已经处理
        VideoPlayerManager.getInstance().onPause(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().onDestroy();
    }
}
