package com.boju.daqingcourt.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.AppApplication;
import com.boju.daqingcourt.AppManager;
import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.MessageEvent;
import com.boju.daqingcourt.fragment.Page1Fragment;
import com.boju.daqingcourt.fragment.Page2Fragment;
import com.boju.daqingcourt.fragment.Page3Fragment;
import com.boju.daqingcourt.utils.PropertyUtil;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.utils.Utils;
import com.boju.daqingcourt.widget.CustomViewPager;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


/**
 * 首页
 */
public class MainActivity extends BaseActivity implements   View.OnClickListener{
    private static final String TAG = "MainActivity";
    @BindView(R.id.main_viewpager)
    CustomViewPager mViewPager;
    @BindView(R.id.rl_fragment1)
    RelativeLayout rl_fragment1;
    @BindView(R.id.rl_fragment2)
    RelativeLayout rl_fragment2;
    @BindView(R.id.rl_fragment3)
    RelativeLayout rl_fragment3;
    @BindView(R.id.tab_img1)
    ImageView tab_img1;
    @BindView(R.id.tab_img2)
    ImageView tab_img2;
    @BindView(R.id.tab_img3)
    ImageView tab_img3;
    @BindView(R.id.tab_text1)
    TextView tab_text1;
    @BindView(R.id.tab_text2)
    TextView tab_text2;
    @BindView(R.id.tab_text3)
    TextView tab_text3;
    Fragment[] fragments={new Page1Fragment(),new Page2Fragment(),new Page3Fragment()};
   Drawable tab1_se,tab2_se,tab3_se;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    /**
     * 权限申请
     */
    private void requestPermission() {
        /**
         * 需要进行检测的权限数组
         */
        String[] needPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(needPermissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }
    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        tab1_se= Utils.tintDrawable(ContextCompat.getDrawable(this, R.drawable.tab1), ContextCompat.getColor(this, R.color.main_bar_color));
        tab2_se=Utils.tintDrawable(ContextCompat.getDrawable(this, R.drawable.tab2), ContextCompat.getColor(this, R.color.main_bar_color));
        tab3_se=Utils.tintDrawable(ContextCompat.getDrawable(this, R.drawable.tab3), ContextCompat.getColor(this, R.color.main_bar_color));
        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();
        requestPermission();
    }

    @Override
    public void initData() {
        super.initData();
        rl_fragment1.setOnClickListener(this);
        rl_fragment2.setOnClickListener(this);
        rl_fragment3.setOnClickListener(this);
    }

    //初始化viewpager
    private void initMainViewPager() {
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setScanScroll(false);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTextViewColor();
                changeSelectedTabState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    };

    //初始化导航背景和颜色
    private void changeTextViewColor() {
        tab_img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab1));
        tab_img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab2));
        tab_img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab3));
        tab_text1.setTextColor(getResources().getColor(R.color.gray));
        tab_text2.setTextColor(getResources().getColor(R.color.gray));
        tab_text3.setTextColor(getResources().getColor(R.color.gray));

    }

    //改变选中状态
    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                tab_text1.setTextColor(getResources().getColor(R.color.main_bar_color));
                tab_img1.setBackgroundDrawable(tab1_se);
                break;
            case 1:
                tab_text2.setTextColor(getResources().getColor(R.color.main_bar_color));
                tab_img2.setBackgroundDrawable(tab2_se);
                break;
            case 2:
                tab_text3.setTextColor(getResources().getColor(R.color.main_bar_color));
                tab_img3.setBackgroundDrawable(tab3_se);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment1:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.rl_fragment2:
                if(StringUtil.isEmpty(PropertyUtil.getPropertyUtil().getProperty("token"))){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }else{
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case R.id.rl_fragment3:
                if(StringUtil.isEmpty(PropertyUtil.getPropertyUtil().getProperty("token"))){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }else {
                    mViewPager.setCurrentItem(2, false);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMessage(MessageEvent<String> event) {
        String message = event.getMessage();
        switch (message) {
            case "cleanUserInfo":
                mViewPager.setCurrentItem(0, false);
                break;
        }
    }
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    /***
     * Android ViewPager+Fragment 模式中 Fragment的返回键的处理接口
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(AppApplication.getInstance(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            AppManager.getAppManager().finishAllActivity();
            System.exit(0);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
