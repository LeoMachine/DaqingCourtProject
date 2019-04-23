package com.boju.daqingcourt.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.boju.daqingcourt.AppApplication;
import com.boju.daqingcourt.R;
import com.boju.daqingcourt.activity.BrowserActivity;
import com.boju.daqingcourt.activity.Login2Activity;
import com.boju.daqingcourt.activity.LoginActivity;
import com.boju.daqingcourt.base.BaseFragment;
import com.boju.daqingcourt.base.HeaderViewPagerFragment;
import com.boju.daqingcourt.bean.BannerInfo;
import com.boju.daqingcourt.bean.CaseInfo;
import com.boju.daqingcourt.bean.MessageEvent;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.DeviceUtil;
import com.boju.daqingcourt.utils.GlideUtils;
import com.boju.daqingcourt.utils.HttpUtils;
import com.boju.daqingcourt.utils.PropertyUtil;
import com.boju.daqingcourt.widget.headerpager.HeaderViewPager;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/3.
 * Page1Fragment3
 */

public class Page3Fragment extends BaseFragment {
    private Unbinder unbinder;
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.tv_common_title_right)
    TextView tv_common_title_right;
    @BindView(R.id.iv_banner3)
    ImageView iv_banner3;
    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    List<HeaderViewPagerFragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_page3, null);
        unbinder = ButterKnife.bind(this, view);
        tv_common_title_name.setText("我的案件");
        tv_common_title_right.setText("切换帐号");
        //初始化view的各控件
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            indexBannerRequest();
            initPager();
            isPrepared = false;
        }
    }

    @OnClick({R.id.tv_common_title_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_common_title_right://切换帐号
                PropertyUtil.getPropertyUtil().cleanUserInfo();
                EventBus.getDefault().post(new MessageEvent<String>("cleanUserInfo"));
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
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
                    GlideUtils.loadImageView(getActivity(), bannerInfo.getImg_url(), iv_banner3);
                } else {
//                    Toast.makeText(getActivity(), httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //初始化viewpager
    private void initPager() {
        fragments.add(MyCaseFragment.newInstance(0));
        MyViewPagerAdapter mAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        vp_view.setAdapter(mAdapter);
        vp_view.setCurrentItem(0);
        vp_view.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        scrollableLayout.setTopOffset(0);
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
//                pagerHeader.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;

            }
        });
    }

    /**
     * @description: ViewPager 适配器
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
