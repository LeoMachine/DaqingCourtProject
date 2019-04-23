package com.boju.daqingcourt.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.activity.ArticleDetailsActivity;
import com.boju.daqingcourt.activity.BrowserActivity;
import com.boju.daqingcourt.activity.DocumentWriteActivity;
import com.boju.daqingcourt.activity.DocumentsPublicActivity;
import com.boju.daqingcourt.activity.IconGonggaoActivity;
import com.boju.daqingcourt.activity.IconSifaActivity;
import com.boju.daqingcourt.adapter.ArticleRclAdapter;
import com.boju.daqingcourt.adapter.GridRclAdapter;
import com.boju.daqingcourt.base.BaseFragment;
import com.boju.daqingcourt.bean.ArticleInfo;
import com.boju.daqingcourt.bean.BannerInfo;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.GlideUtils;
import com.boju.daqingcourt.widget.GridDividerItemDecoration;
import com.boju.daqingcourt.widget.loadmore.LoadingFooter;
import com.boju.daqingcourt.widget.loadmore.RecyclerViewStateUtils;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/3.
 * Page1Fragment3
 */

public class Page1Fragment extends BaseFragment {
    private Unbinder unbinder;
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    @BindView(R.id.rcl_view)
    RecyclerView rcl_view;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fl_content)
    View fl_content;
    List<ArticleInfo> dateAllList = new ArrayList<>();
    int page = 1, page_count = 10;
    boolean isMore = true, isRefresh = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    ImageView iv_banner;
    RecyclerView rcl_view_grid;
    final int[] images = {R.mipmap.icon1, R.mipmap.icon2, R.mipmap.icon3, R.mipmap.icon4,R.mipmap.icon5, R.mipmap.icon6};
    final String[] titles = {"开庭公告", "文书填单", "法院指引", "阳光司法", " 裁判文书", " 互联网庭审"};
    final String[] subTitles = {"互联网法院开庭安排", "网上文书填单", "法院概况", "庭审实况播放", "裁判文书网", "互联网庭审系统"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_page1, null);
        emptyLayoutAdd(view);
        unbinder = ButterKnife.bind(this, view);
        tv_common_title_name.setText("大庆微法院");
        //初始化view的各控件
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            initRefresh();
            isPrepared = false;
        }
    }

//    @OnClick({R.id.rl_my_information})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_integral://积分商城
//                startActivity(new Intent(getActivity(), IntegralShopActivity.class));
//                break;
//        }
//    }



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
                indexBannerRequest();
            }
        });
        initRclAdapter();
    }

    private void initRclAdapter() {
        rcl_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArticleRclAdapter adapter = new ArticleRclAdapter(getActivity(), dateAllList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rcl_view.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        rcl_view.addOnScrollListener(mOnScrollListener);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_view.addItemDecoration(dividerItemDecoration);
        //添加头部
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_article_header, (ViewGroup) fl_content, false);
        adapter.setHeaderView(headerView);
        iv_banner = headerView.findViewById(R.id.iv_banner);
        rcl_view_grid = headerView.findViewById(R.id.rcl_view_grid);
        adapter.setOnItemClickListener(new ArticleRclAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                intent.putExtra("title", "新闻详情");
                intent.putExtra("category_id", "");
                intent.putExtra("id", dateAllList.get(position).getId() + "");
                startActivity(intent);
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataListRequest();
            }
        });
        indexBannerRequest();
        initRclGridAdapter();
        dataListRequest();
    }

    //设置Gridview
    private void initRclGridAdapter() {
        rcl_view_grid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        GridRclAdapter adapter = new GridRclAdapter(getActivity(), images, titles, subTitles);
        rcl_view_grid.setAdapter(adapter);
        rcl_view_grid.setNestedScrollingEnabled(false);
        //分隔线
        GridDividerItemDecoration dividerItemDecorationVertical = new GridDividerItemDecoration(1, getResources().getColor(R.color.lightgray));
        rcl_view_grid.addItemDecoration(dividerItemDecorationVertical);
        adapter.setOnItemClickListener(new GridRclAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0://开庭公告
                        startActivity(new Intent(getActivity(), IconGonggaoActivity.class));
                        break;
                    case 1://文书填单
                        String url = "http://daqing.tdfy.net/api/apphtml/wenshu/";
                        toBrowserActivity(url, "文书填单");
                        break;
                    case 2://法院指引
                        startActivity(new Intent(getActivity(), DocumentsPublicActivity.class));
                        break;
                    case 3://阳光司法
//                        startActivity(new Intent(getActivity(), IconSifaActivity.class));
                        toBrowserActivity("http://tv.hljcourt.gov.cn/index/index/court/38", "阳光司法");
                        break;
                    case 4://裁判文书
                        String urlWenshu="http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+%E9%BB%91%E9%BE%99%E6%B1%9F%E7%9C%81%E5%A4%A7%E5%BA%86%E5%B8%82%E4%B8%AD%E7%BA%A7%E4%BA%BA%E6%B0%91%E6%B3%95%E9%99%A2+++%E4%B8%AD%E7%BA%A7%E6%B3%95%E9%99%A2:%E9%BB%91%E9%BE%99%E6%B1%9F%E7%9C%81%E5%A4%A7%E5%BA%86%E5%B8%82%E4%B8%AD%E7%BA%A7%E4%BA%BA%E6%B0%91%E6%B3%95%E9%99%A2";
                        toBrowserActivity(urlWenshu, "裁判文书");
                        break;
                    case 5://互联网庭审
                        toBrowserActivity("http://daqing.tdfy.net/api/apphtml/hlwts/", "互联网庭审");
                        break;
                }

            }
        });
    }
    //跳转网页
    private void toBrowserActivity(String url, String title) {
        Intent intent = new Intent(getActivity(), BrowserActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    //接口请求
    private void dataListRequest() {
        RetrofitClient.getInstance().postArticleInfo(page, page_count, new BaseSubscriber<HttpResponse<List<ArticleInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                if (page == 1 && isRefresh) {
                    refreshLayout.finishRefresh(false);//传入false表示刷新失败
                } else {
                    RecyclerViewStateUtils.setFooterViewState(rcl_view, LoadingFooter.State.NetWorkError);
                }
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<ArticleInfo>> httpResponse) {
                if (page == 1 && isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    RecyclerViewStateUtils.setFooterViewState(rcl_view, LoadingFooter.State.Normal);
                }
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
//                    Toast.makeText(MyTabGuanzhuActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    List<ArticleInfo> dataList = httpResponse.getData();
                    if (page == 1) {
                        dateAllList.clear();
                    }
                    dateAllList.addAll(dataList);
                    mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                    if (dataList.size() <= 0) {
                        isMore = false;
                        refreshState(LoadingFooter.State.TheEnd);
                    } else {
                        page++;
                        isMore = true;
                    }
                } else {
                    if (page == 1 && isRefresh) {
                        refreshLayout.finishRefresh(false);//传入false表示刷新失败
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

    //首页轮播图请求
    private void indexBannerRequest() {
        RetrofitClient.getInstance().postIndexBannerList(new BaseSubscriber<HttpResponse<BannerInfo>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
//                Toast.makeText(getActivity(), responeThrowable.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<BannerInfo> httpResponse) {
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    BannerInfo bannerInfo = httpResponse.getData();
                    GlideUtils.loadImageView(getActivity(), bannerInfo.getImg_url(), iv_banner);
                } else {
//                    Toast.makeText(getActivity(), httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
