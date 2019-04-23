package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.bean.CaseInfoDetails;

import java.util.List;


/**
 * Created by Administrator on 2017/9/11.
 * 法律依据adapter
 */

public class DetailFsalvRclAdapter extends RecyclerView.Adapter<DetailFsalvRclAdapter.ViewHolder> {
    Context context;
    private OnViewClickListener onViewClickListener;
    List<CaseInfoDetails.FalvBean> dataAllList;
    public DetailFsalvRclAdapter(Context context, List<CaseInfoDetails.FalvBean> dataAllList) {
        this.context = context;
        this.dataAllList = dataAllList;
    }
    public final void addItem(CaseInfoDetails.FalvBean detailsBeigaoInfo) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_details_falv, parent, false);
        return new ViewHolder(view);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CaseInfoDetails.FalvBean falvBean=dataAllList.get(position);
        holder.tv_title.setText(falvBean.getTitle());
        holder.tv_summary.setText(falvBean.getSummary());

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
       TextView tv_title,tv_summary;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_summary=itemView.findViewById(R.id.tv_summary);
        }
    }

    public interface OnViewClickListener {
        void onItemViewClick(View view, int position);

    }
}
