package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;

import java.util.List;


/**
 * Created by Administrator on 2017/9/11.
 * 代办adapter
 */

public class CaseBeigaoAddRclAdapter extends RecyclerView.Adapter<CaseBeigaoAddRclAdapter.ViewHolder> {
    Context context;
    private OnViewClickListener onViewClickListener;
    List<CaseBeigaoInfo> dataAllList;
    public CaseBeigaoAddRclAdapter(Context context,List<CaseBeigaoInfo> dataAllList) {
        this.context = context;
        this.dataAllList = dataAllList;
    }
    public final void addItem(CaseBeigaoInfo caseBeigaoInfo) {
        this.dataAllList.add(caseBeigaoInfo);
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_defendant_add, parent, false);
        return new ViewHolder(view);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CaseBeigaoInfo caseBeigaoInfo=dataAllList.get(position);
        holder.tv_title.setText(caseBeigaoInfo.getName());
        holder.tv_title_name.setText("被告人"+(position+1));

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
