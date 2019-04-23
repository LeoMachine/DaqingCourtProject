package com.boju.daqingcourt.inter;

import android.app.ProgressDialog;

/**
 * Created by Administrator on 2017/5/19.
 * 缓冲进度加载对话框
 */

public interface DialogControl {

    public abstract void hideWaitDialog();

    public abstract ProgressDialog showWaitDialog();

    public abstract ProgressDialog showWaitDialog(int resid);

    public abstract ProgressDialog showWaitDialog(String text);
}
