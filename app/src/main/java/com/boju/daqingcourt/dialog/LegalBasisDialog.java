package com.boju.daqingcourt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.LegalExpandAdapter;
import com.boju.daqingcourt.bean.CaseZhengjuInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/4.
 * 法律依据doalog
 */

public class LegalBasisDialog extends Dialog{
    private DialogViewListener listener;
    Context context;
    TextView tv_sure,tv_cancle;
    ExpandableListView expand_list;
    List<CaseZhengjuInfo> caseZhengjuInfoList;
    private LegalExpandAdapter selva;

    public interface DialogViewListener{
        public void onListSureClick(View view);
    }
    public void setDialogViewListener(DialogViewListener listener){
        this.listener=listener;
    }
    public LegalBasisDialog(@NonNull Context context, int themeResId,List<CaseZhengjuInfo> caseZhengjuInfoList) {
        super(context, themeResId);
        this.context=context;
        this.caseZhengjuInfoList=caseZhengjuInfoList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_legal_basis,null);
        setContentView(view);

        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //解决弹出被底部导航栏遮挡问题
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //消除边距
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //设置Dialog点击外部消失
        setCanceledOnTouchOutside(true);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = wm.getDefaultDisplay().getHeight()/3*2;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.CityPickerAnim);
        expand_list = view.findViewById(R.id.expand_list);
        tv_sure= view.findViewById(R.id.tv_sure);
        tv_cancle= view.findViewById(R.id.tv_cancle);

        selva = new LegalExpandAdapter(caseZhengjuInfoList,context);
        expand_list.setAdapter(selva);
//        for (int i = 0; i < selva.getGroupCount(); i++) {
//            expand_list.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
//        }
        selva.setCheckInterface(new LegalExpandAdapter.CheckInterface() {

            @Override
            public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
                selva.notifyDataSetChanged();
            }
        });// 关键步骤1,设置复选框接口
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                listener.onListSureClick(v);
            }
        });
    }

}
