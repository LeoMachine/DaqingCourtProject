package com.boju.daqingcourt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.bean.CaseZhengjuInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 * 证据选择
 */
public class LegalExpandAdapter extends BaseExpandableListAdapter {
    private List<CaseZhengjuInfo> legalGroupInfoList;
    private Context context;
    private CheckInterface checkInterface;

    /**
     * 构造函数
     *
     * @param legalGroupInfoList   组元素列表
     * @param context
     */
    public LegalExpandAdapter(List<CaseZhengjuInfo> legalGroupInfoList, Context context) {
        super();
        this.legalGroupInfoList = legalGroupInfoList;
        this.context = context;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    @Override
    public int getGroupCount() {
        return legalGroupInfoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return legalGroupInfoList.get(groupPosition).getInfo().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return legalGroupInfoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<CaseZhengjuInfo.InfoBean> cartList= legalGroupInfoList.get(groupPosition).getInfo();
        return cartList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_expand_group, null);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        final CaseZhengjuInfo supplierInfo = (CaseZhengjuInfo) getGroup(groupPosition);
        if (supplierInfo != null) {
            gholder.tv_group_name.setText(supplierInfo.getTitle());
        }


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            convertView = View.inflate(context, R.layout.item_expand_child, null);
            cholder.cb_child = (CheckBox) convertView.findViewById(R.id.cb_child);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildHolder) convertView.getTag();
        }

        final CaseZhengjuInfo.InfoBean legalChildInfo = (CaseZhengjuInfo.InfoBean) getChild(groupPosition, childPosition);
        cholder.cb_child.setText(legalChildInfo.getTitle()+legalChildInfo.getSummary());
        cholder.cb_child.setChecked(legalChildInfo.isChoosed());
        cholder.cb_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                legalChildInfo.setChoosed(((CheckBox) v).isChecked());
                cholder.cb_child.setChecked(((CheckBox) v).isChecked());
                checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        TextView tv_group_name;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        public CheckBox cb_child;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doIncrease(int groupPosition, int childPosition, View showCountView, View tv_item_count, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, View tv_item_count, boolean isChecked);
        /**
         * 规格参数选择
         */
        public void  paramsSelected(int groupPosition, int childPosition, View paramsView);

        /**
         *   跳转
         */
        public void itemSelected(int groupPosition, int childPosition);
    }

}
