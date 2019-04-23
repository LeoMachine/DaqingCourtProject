package com.boju.daqingcourt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.activity.CaseDetailsActivity;
import com.boju.daqingcourt.adapter.CaseRclAdapter;
import com.boju.daqingcourt.base.HeaderViewPagerFragment;
import com.boju.daqingcourt.bean.CaseInfo;
import com.boju.daqingcourt.bean.MessageEvent;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.widget.empty.EmptyLayout;
import com.boju.daqingcourt.widget.loadmore.LoadingFooter;
import com.boju.daqingcourt.widget.loadmore.RecyclerViewStateUtils;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/19.
 * 我的案件
 */

public class MyCaseFragment extends HeaderViewPagerFragment {
    private Unbinder unbinder;
    @BindView(R.id.rcl_view)
    RecyclerView rcl_view;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layout_empty)
    EmptyLayout layout_empty;

    List<CaseInfo> dateAllList = new ArrayList<>();
    int page = 1, page_count = 10;
    boolean isMore = true, isRefresh = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    public static MyCaseFragment newInstance(int position) {
        MyCaseFragment newFragment = new MyCaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_common_refresh, null);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        //初始化view的各控件
        isPrepared = true;
        initRefresh();
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared) {

            isPrepared = true;
        }
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
        initRclAdapter();
        layout_empty.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataListRequest();
            }
        });
        layout_empty.setErrorType(EmptyLayout.HIDE_LAYOUT);
    }

    private void initRclAdapter() {
        rcl_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        CaseRclAdapter adapter = new CaseRclAdapter(getActivity(), dateAllList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rcl_view.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        rcl_view.addOnScrollListener(mOnScrollListener);
        rcl_view.setNestedScrollingEnabled(false);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_view.addItemDecoration(dividerItemDecoration);
        adapter.setOnItemClickListener(new CaseRclAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CaseDetailsActivity.class);
                intent.putExtra("case_id",dateAllList.get(position).getId());
                startActivity(intent);
            }

        });
        dataListRequest();
    }

    //接口请求
    private void dataListRequest() {
        if(page==1){
            layout_empty.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
        RetrofitClient.getInstance().postCasesInfoList(page, page_count, new BaseSubscriber<HttpResponse<List<CaseInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                if (page == 1) {
                    refreshLayout.finishRefresh(false);
                    layout_empty.setErrorType(EmptyLayout.NETWORK_ERROR);
                } else {
                    RecyclerViewStateUtils.setFooterViewState(rcl_view, LoadingFooter.State.NetWorkError);
                }
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(HttpResponse<List<CaseInfo>> httpResponse) {
                if (page == 1) {
                    refreshLayout.finishRefresh(true);
                    layout_empty.setErrorType(EmptyLayout.HIDE_LAYOUT);
                }else {
                    RecyclerViewStateUtils.setFooterViewState(rcl_view, LoadingFooter.State.Normal);
                }
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode()==0) {
//                    Toast.makeText(MyTabGuanzhuActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    List<CaseInfo> dataList =httpResponse.getData();
                    if (page == 1) {
                        dateAllList.clear();
                    }
                    dateAllList.addAll(dataList);
                    mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                    if (dataList.size() <= 0) {
                        isMore = false;
                        if (page == 1) {
                            layout_empty.setErrorType(EmptyLayout.NODATA);
                        } else {
                            refreshState(LoadingFooter.State.TheEnd);
                        }
                    } else {
                        page++;
                        isMore = true;
                    }
                } else {
                    if (page == 1) {
                        refreshLayout.finishRefresh(false);
                        layout_empty.setErrorType(EmptyLayout.NETWORK_ERROR);
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(rcl_view, LoadingFooter.State.NetWorkError);
                    }
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
            RecyclerViewStateUtils.setFooterViewState(getActivity(), rcl_view, page_count, state, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataListRequest();
                }
            });
        } else {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), rcl_view, page_count, state, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMessage(MessageEvent<String> event) {
        String message = event.getMessage();
        switch (message) {
            case "updateCase":
                page = 1;
                isRefresh = false;
                dataListRequest();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public View getScrollableView() {
        return rcl_view;
    }
}
