package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boju.daqingcourt.R;

/**
 * Created by Administrator on 2017/11/11.
 * 专家adapter
 */

public class GridRclAdapter extends RecyclerView.Adapter<GridRclAdapter.ViewHolder> {
    Context context;
    private OnRecyclerItemClickListener mOnItemClickListener;//单击事件
    int[] images;String[] titles;String[] titles2;
    public GridRclAdapter(Context context, int[] images, String[] titles, String[] titles2) {
        this.context = context;
        this.images = images;
        this.titles = titles;
        this.titles2 = titles2;
    }

    /**
     * 暴露给外面的设置单击事件
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定视图到holder,就如同ListView的getView(),但是这里已经把复用实现了,我们只需要填充数据就行.
     * 由于在复用的时候都是调用该方法填充数据,但是上滑的时候,又会随机产生高度设置到控件上,这样当滑
     * 到顶部可能就会看到一片空白,因为后面随机产生的高度和之前的高度不一样,就不能填充屏幕了,所以
     * 需要记录每个控件产生的随机高度,然后在复用的时候再设置上去
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.iv_image.setImageResource(images[position]);
        holder.tv_title.setText(titles[position]);
        holder.tv_title2.setText(titles2[position]);
        //设置单击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里是为textView设置了单击事件,回调出去
                    //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_title,tv_title2;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.iv_image);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_title2=itemView.findViewById(R.id.tv_title2);
        }
    }

    /**
     * 处理item的点击事件
     */
    public interface OnRecyclerItemClickListener {
        public void onItemClick(View view, int position);
    }


}
