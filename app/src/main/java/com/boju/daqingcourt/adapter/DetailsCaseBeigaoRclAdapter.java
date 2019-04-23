package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;
import com.boju.daqingcourt.bean.CaseInfoDetails;

import java.util.List;


/**
 * Created by Administrator on 2017/9/11.
 * 代办adapter
 */

public class DetailsCaseBeigaoRclAdapter extends RecyclerView.Adapter<DetailsCaseBeigaoRclAdapter.ViewHolder> {
    Context context;
    private OnViewClickListener onViewClickListener;
    List<CaseInfoDetails.BeiBean> dataAllList;
    public DetailsCaseBeigaoRclAdapter(Context context, List<CaseInfoDetails.BeiBean> dataAllList) {
        this.context = context;
        this.dataAllList = dataAllList;
    }
    public final void addItem(CaseInfoDetails.BeiBean detailsBeigaoInfo) {
        this.dataAllList.add(detailsBeigaoInfo);
        notifyDataSetChanged();
    }
    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.dataAllList.remove(position);
            notifyDataSetChanged();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_defendant, parent, false);
        return new ViewHolder(view);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CaseInfoDetails.BeiBean detailsBeigaoInfo=dataAllList.get(position);
        holder.tv_title_name.setText("被告人"+(position+1));
        holder.tv_title.setText(detailsBeigaoInfo.getName());

        if (onViewClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClickListener.onItemViewClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataAllList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
       TextView tv_title,tv_title_name;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_title_name=itemView.findViewById(R.id.tv_title_name);
        }
    }

    public interface OnViewClickListener {
        void onItemViewClick(View view, int position);

    }
}
