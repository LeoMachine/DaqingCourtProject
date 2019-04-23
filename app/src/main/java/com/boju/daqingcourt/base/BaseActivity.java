package com.boju.daqingcourt.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.boju.daqingcourt.AppManager;
import com.boju.daqingcourt.R;
import com.boju.daqingcourt.inter.BaseViewInterface;
import com.boju.daqingcourt.inter.DialogControl;
import com.boju.daqingcourt.utils.DialogHelp;
import com.boju.daqingcourt.widget.empty.EmptyLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/5/19.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, BaseViewInterface, DialogControl {
    private ProgressDialog _waitDialog;
    private Unbinder unbinder;
    private InputMethodManager imm;
    public EmptyLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(getLayoutId());
        // 通过注解绑定控件
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
    }
    /**
     * 布局ID
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelp.getWaitDialog(this, message);
                _waitDialog.setCanceledOnTouchOutside(false);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
    }

    @Override
    public void hideWaitDialog() {
        if (_waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftKeyBoard();
        this.imm = null;
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyBoard();
    }

    //隐藏软键盘
    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }
    //添加添加空数据布局布局
    public void emptyLayoutAdd() {
        ViewGroup rootView = getWindow().getDecorView().findViewById(R.id.fl_content);
        emptyLayout = (EmptyLayout) LayoutInflater.from(this).inflate(R.layout.empty_layout, rootView, false);
        rootView.addView(emptyLayout);
        emptyLayoutLoding();
    }

    //网络错误
    public void emptyLayoutNetworkError() {
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    //隐藏进度布局
    public void emptyLayoutHideLayout() {
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
    }

    //加载中
    public void emptyLayoutLoding() {
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    }

    //暂无数据
    public void emptyLayoutNoData() {
        emptyLayout.setErrorType(EmptyLayout.NODATA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
