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
 * 诉讼adapter
 */

public class DetailsSusongRclAdapter extends RecyclerView.Adapter<DetailsSusongRclAdapter.ViewHolder> {
    Context context;
    private OnViewClickListener onViewClickListener;
    List<CaseInfoDetails.SusongBean> dataAllList;
    public DetailsSusongRclAdapter(Context context, List<CaseInfoDetails.SusongBean> dataAllList) {
        this.context = context;
        this.dataAllList = dataAllList;
    }
    public final void addItem(CaseInfoDetails.SusongBean detailsBeigaoInfo) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_law, parent, false);
        return new ViewHolder(view);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CaseInfoDetails.SusongBean susongBean=dataAllList.get(position);
        holder.tv_title_name.setText("诉讼请求"+(position+1));
//        holder.tv_type.setText(susongBean.getTypeshow());
        holder.tv_content.setText(susongBean.getContent());
        holder.tv_money.setText(susongBean.getMoney());

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
       TextView tv_type,tv_content,tv_money,tv_title_name;
        public ViewHolder(View itemView) {
            super(itemView);
//            tv_type=itemView.findViewById(R.id.tv_type);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_money=itemView.findViewById(R.id.tv_money);
            tv_title_name=itemView.findViewById(R.id.tv_title_name);
        }
    }

    public interface OnViewClickListener {
        void onItemViewClick(View view, int position);

    }
}
