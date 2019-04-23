package com.boju.daqingcourt.utils;

import android.support.v7.widget.RecyclerView;

import com.boju.daqingcourt.widget.empty.EmptyLayout;
import com.boju.daqingcourt.widget.loadmore.LoadingFooter;
import com.boju.daqingcourt.widget.loadmore.RecyclerViewStateUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by Administrator on 2018/1/26.
 * 上拉加载帮助类
 */

public class HttpUtils {
    RecyclerView recyclerView;
    EmptyLayout emptyLayout;
    SmartRefreshLayout refreshLayout;

    public HttpUtils(RecyclerView recyclerView, EmptyLayout emptyLayout, SmartRefreshLayout refreshLayout) {
        this.recyclerView = recyclerView;
        this.emptyLayout = emptyLayout;
        this.refreshLayout = refreshLayout;
    }

    public   void  loding(int page, boolean isRefresh,boolean isMore){
        if (page == 1) {
            emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
    }
    public   void  hideLoding(int page, boolean isRefresh){
        if (page == 1) {
            refreshLayout.finishRefresh(true);//传入false表示刷新失败
            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
        }
    }


    public   void  error(int page, boolean isRefresh){
        if (page == 1) {
            refreshLayout.finishRefresh(false);//传入false表示刷新失败
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }else{
            RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.NetWorkError);
        }

    }
}
