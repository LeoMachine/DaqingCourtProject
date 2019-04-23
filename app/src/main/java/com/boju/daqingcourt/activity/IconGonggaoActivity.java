package com.boju.daqingcourt.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.IconGonggaoRclAdapter;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.IconGonggaoInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.HttpUtils;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.utils.ToastUtil;
import com.boju.daqingcourt.widget.CustomDatePicker;
import com.boju.daqingcourt.widget.EditTextClearable;
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

/**
 * Created by Administrator on 2018/7/3.
 * 开庭公告
 */

public class IconGonggaoActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.rcl_view)
    RecyclerView rcl_view;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fl_content)
    View fl_content;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;
    @BindView(R.id.iv_start)
    ImageView iv_start;
    @BindView(R.id.iv_end)
    ImageView iv_end;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    List<IconGonggaoInfo> dataAllList = new ArrayList<>();
    int page = 1, page_count = 10;
    boolean isMore = true, isRefresh = false;
    HttpUtils httpUtils;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    String keywords = "", start_time = "", end_time = "";
    private CustomDatePicker customDatePicker;
    String currentDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gonggao;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("开庭公告");
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
                finish();
                break;
        }
    }

    private void initRclAdapter() {
        rcl_view.setLayoutManager(new LinearLayoutManager(this));
        IconGonggaoRclAdapter adapter = new IconGonggaoRclAdapter(this, dataAllList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rcl_view.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        rcl_view.addOnScrollListener(mOnScrollListener);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_view.addItemDecoration(dividerItemDecoration);
        //添加头部
        adapter.setOnItemClickListener(new IconGonggaoRclAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void setHeader() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keywords = et_search.getText().toString();
                    if (StringUtil.isEmpty(keywords)) {
                        Toast.makeText(IconGonggaoActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
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
        tv_start_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtil.isEmpty(s.toString())) {
                    iv_start.setVisibility(View.GONE);
                } else {
                    iv_start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_end_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtil.isEmpty(s.toString())) {
                    iv_end.setVisibility(View.GONE);
                } else {
                    iv_end.setVisibility(View.VISIBLE);
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
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_start_time.setText("");
                page1Refresh();
            }
        });
        iv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_end_time.setText("");
                page1Refresh();
            }
        });
        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(tv_start_time);

            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(tv_end_time);
            }
        });
    }

    private void initDatePicker(final TextView textView) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        currentDate = now.split(" ")[0];
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate = time.split(" ")[0];
                textView.setText(currentDate);
                page1Refresh();

            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        customDatePicker.show(currentDate);

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
        start_time = tv_start_time.getText().toString();
        end_time = tv_end_time.getText().toString();
        RetrofitClient.getInstance().postIconGonggaoInfo(page, page_count, keywords, start_time, end_time, new BaseSubscriber<HttpResponse<List<IconGonggaoInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                httpUtils.error(page, isRefresh);
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(HttpResponse<List<IconGonggaoInfo>> httpResponse) {
                httpUtils.hideLoding(page, isRefresh);
                refreshState(LoadingFooter.State.Normal);
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
//                    Toast.makeText(MyTabGuanzhuActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    List<IconGonggaoInfo> dataList = httpResponse.getData();
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
            isRefresh = false;
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
