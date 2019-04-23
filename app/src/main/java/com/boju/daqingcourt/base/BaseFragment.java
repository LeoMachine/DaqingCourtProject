package com.boju.daqingcourt.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.inter.DialogControl;
import com.boju.daqingcourt.widget.empty.EmptyLayout;


/**
 * Created by Administrator on 2017/5/19.
 * fragment基类
 */
public abstract class BaseFragment extends Fragment implements
        View.OnClickListener{
    public EmptyLayout emptyLayout;
    protected boolean isVisible;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }



    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();
    protected void onInvisible(){}
    @Override
    public void onClick(View v) {

    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    //添加进度布局
    public void emptyLayoutAdd(View view){
        ViewGroup rootView=view.findViewById(R.id.fl_content);
        emptyLayout= (EmptyLayout) LayoutInflater.from(getActivity()).inflate(R.layout.empty_layout,rootView,false);
        rootView.addView(emptyLayout);
        emptyLayoutLoding();
    }
    //网络错误
    public void emptyLayoutNetworkError(){
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }
    //隐藏进度布局
    public void emptyLayoutHideLayout(){
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
    }
    //加载中
    public void emptyLayoutLoding(){
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    }
    //暂无数据
    public void emptyLayoutNoData(){
        emptyLayout.setErrorType(EmptyLayout.NODATA);
    }
}