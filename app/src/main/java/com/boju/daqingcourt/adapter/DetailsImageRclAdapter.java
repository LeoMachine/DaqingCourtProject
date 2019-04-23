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
public class DetailsImageRclAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private LayoutInflater mLayoutInflater;
    private List<String> pathList;

    public DetailsImageRclAdapter(Activity context, List<String> pathList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.pathList = pathList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            Glide.with(context).load(pathList.get(position)).into(((ViewHolder) holder).iv_photo);
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PreviewPhotoDialog(context,R.style.dialog,(ArrayList<String>) pathList,position).show();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return  pathList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_photo;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }
}