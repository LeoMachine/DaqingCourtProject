package com.boju.daqingcourt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class PreviewPhotoDialog extends Dialog {
    private DialogViewListener listener;
    Context context;
    TextView tv_common_title_name;
    ViewPager vp_photo_preview;
    ImageView common_iv_back;
    int position;
    List<String> pathList;
    //定义一个View的数组
    private List<View> views = new ArrayList<>();

    public interface DialogViewListener {
        public void onListItemClick(View view, int position);
    }

    public PreviewPhotoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void setDialogViewListener(DialogViewListener listener) {
        this.listener = listener;
    }

    public PreviewPhotoDialog(@NonNull Context context, int themeResId, List<String> pathList, int position) {
        super(context, themeResId);
        this.context = context;
        this.pathList = pathList;
        this.position = position;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_preview_photo, null);
        setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //解决弹出被底部导航栏遮挡问题
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //消除边距
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //设置Dialog点击外部消失
        setCanceledOnTouchOutside(true);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        tv_common_title_name = view.findViewById(R.id.tv_common_title_name);
        vp_photo_preview = view.findViewById(R.id.vp_photo_preview);
        common_iv_back= view.findViewById(R.id.common_iv_back);
        tv_common_title_name.setText("图片预览");
        tv_common_title_name.setText("图片预览" + "(" + (position + 1) + "/" + pathList.size() + ")");
        common_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        for (int i = 0; i < pathList.size(); i++) {
            View viewPhoto = LayoutInflater.from(context).inflate(R.layout.item_photo_preview, null);
            ImageView iv_photo = (ImageView) viewPhoto.findViewById(R.id.iv_photo);
            GlideUtils.loadImageView(context,pathList.get(i),iv_photo);
            views.add(viewPhoto);
            iv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dismiss();
                }
            });
        }
        MyImageAdapter adapter = new MyImageAdapter(views);
        vp_photo_preview.setAdapter(adapter);
        vp_photo_preview.setCurrentItem(position);
        vp_photo_preview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_common_title_name.setText("图片预览" + "(" + (position + 1) + "/" + pathList.size() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyImageAdapter extends PagerAdapter {
        List<View> views;

        public MyImageAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = views.get(position);
            //前一张图片划过后删除该View
            container.removeView(v);
        }

    }
}
