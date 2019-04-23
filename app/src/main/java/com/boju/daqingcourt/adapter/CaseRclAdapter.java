package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.bean.ArticleInfo;
import com.boju.daqingcourt.bean.CaseInfo;
import com.boju.daqingcourt.utils.GlideUtils;

import java.util.List;


/**
 * Created by Administrator on 2018/1/11.
 * 我的案件列表
 */

public class CaseRclAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private OnRecyclerItemClickListener mOnItemClickListener;//单击事件
    //获取从Activity中传递过来每个item的数据集合
    List<CaseInfo> dateAllList;

    public CaseRclAdapter(Context context, List<CaseInfo> dateAllList) {
        this.context = context;
        this.dateAllList = dateAllList;
    }

    public final void addItem(int position, CaseInfo caseInfo) {
        this.dateAllList.add(position, caseInfo);
        notifyItemInserted(position);
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 暴露给外面的设置单击事件
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ListHolder(mFooterView);
        } else {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
            return new ListHolder(layout);
        }
    }


    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ListHolder) {
                CaseInfo dataInfo;
                if (mHeaderView != null) {
                    dataInfo = dateAllList.get(position - 1);
                } else {
                    dataInfo = dateAllList.get(position);
                }
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                ((ListHolder) holder).tv_title.setText(dataInfo.getTitle());
                ((ListHolder) holder).tv_statusshow.setText(dataInfo.getStatusshow());

                //设置单击事件
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //这里是为textView设置了单击事件,回调出去
                            //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
                            if (mHeaderView != null) {
                                mOnItemClickListener.onItemClick(v, position - 1);
                            } else {
                                mOnItemClickListener.onItemClick(v, position);
                            }
                        }
                    });
                }
                return;
            }
            return;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }
    }


    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_title,tv_statusshow;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_statusshow= itemView.findViewById(R.id.tv_statusshow);
        }
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (mHeaderView != null && position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (mFooterView != null && position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        if (mFooterView != null && mHeaderView != null) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_NORMAL;
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return dateAllList.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return dateAllList.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return dateAllList.size() + 1;
        } else {
            return dateAllList.size() + 2;
        }
    }

    /**
     * 处理item的点击事件
     */
    public interface OnRecyclerItemClickListener {
        public void onItemClick(View view, int position);
    }
}
