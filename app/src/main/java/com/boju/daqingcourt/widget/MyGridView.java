package com.boju.daqingcourt.widget;

import android.widget.GridView;

/**
 * Created by Administrator on 2017/5/20.
 * 自定义GridView自适应高度控件
 */

public class MyGridView extends GridView
{
    public MyGridView(android.content.Context context,
                      android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
