package com.boju.daqingcourt.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.dialog.PreviewPhotoDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter
 * Created by Yancy on 2015/12/4.
 * 发帖图片选择adapter
 */
public class ImageRclAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private LayoutInflater mLayoutInflater;
    private List<String> pathList;
    private final static String TAG = "Adapter";
    /**
     * 一行最多显示的图片数量
     */
    public static final int NUM_ITEM = 10;
    /**
     * 底部添加图片view
     */
    private final int ITEM_FOOTER = 0;
    /**
     * 内容区域，显示图片
     */
    private final int ITEM_CONTENT = 1;

    private OnViewClickListener onViewClickListener;

    public ImageRclAdapter(Activity context, List<String> pathList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.pathList = pathList;
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_FOOTER) {
            return new FootViewHolder(mLayoutInflater.inflate(R.layout.item_photo_add, parent, false));
        } else {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_photo_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            Glide.with(context).load(pathList.get(position)).into(((ViewHolder) holder).iv_photo);
            ((ViewHolder) holder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathList.remove(position);
                    notifyDataSetChanged();
                    onViewClickListener.onDeleteViewClick(view);
                }
            });
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PreviewPhotoDialog(context, R.style.dialog, (ArrayList<String>) pathList, position).show();
                }
            });
        } else if (holder instanceof FootViewHolder) {
            if (onViewClickListener != null) {
                ((FootViewHolder) holder).mImageAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onAddViewClick(v);
                    }
                });
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (pathList.size() <= NUM_ITEM) {
            if (pathList.size() <= 0) {//没有数据，显示添加按钮
                return ITEM_FOOTER;
            } else {
                if (position == pathList.size()) {//在范围内最后一个显示添加按钮，其余显示数据
                    return ITEM_FOOTER;
                } else {
                    return ITEM_CONTENT;
                }
            }
        } else {
            return ITEM_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        if (pathList.size() < 10) {
            return pathList.size() + 1;
        } else {
            return pathList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_photo, iv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }

    }

    //底部ViewHolder
    public static class FootViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageAdd;

        public FootViewHolder(View footView) {
            super(footView);
            mImageAdd = (ImageView) itemView.findViewById(R.id.imageviewadd);
        }
    }

    public interface OnViewClickListener {
        void onAddViewClick(View view);
        void onDeleteViewClick(View view);
    }
}