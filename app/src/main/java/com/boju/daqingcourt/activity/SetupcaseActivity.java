package com.boju.daqingcourt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.adapter.CaseBeigaoAddRclAdapter;
import com.boju.daqingcourt.adapter.ImageRclAdapter;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.AnyouTypeInfo;
import com.boju.daqingcourt.bean.CaseAnyouTypeInfo;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;
import com.boju.daqingcourt.bean.CaseZhengjuInfo;
import com.boju.daqingcourt.bean.ListSusongInfoPost;
import com.boju.daqingcourt.bean.MessageEvent;
import com.boju.daqingcourt.bean.SusongPostInfo;
import com.boju.daqingcourt.dialog.BottomDialog;
import com.boju.daqingcourt.dialog.LegalBasisDialog;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.utils.ToastUtil;
import com.boju.daqingcourt.widget.wheelview.WheelView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/14.
 * 手机立案
 */

public class SetupcaseActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.rcl_beigao)
    RecyclerView rcl_beigao;
    @BindView(R.id.ll_law_add)
    LinearLayout ll_law_add;
    @BindView(R.id.rcl_images)
    RecyclerView rcl_images;
    @BindView(R.id.cb_shuomin)
    CheckBox cb_shuomin;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.tv_qisu)
    TextView tv_qisu;
    @BindView(R.id.tv_anyou)
    TextView tv_anyou;
    @BindView(R.id.tv_fayuan)
    TextView tv_fayuan;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_remark)
    EditText et_remark;
    @BindView(R.id.tv_yiju)
    TextView tv_yiju;
    @BindView(R.id.tv_zhegnju_count)
    TextView tv_zhegnju_count;
    @BindView(R.id.tv_zhengju)
    TextView tv_zhengju;
    @BindView(R.id.layout_add_defendant)
    View layout_add_defendant;

    int category_id;
    String category_title;

    ImageRclAdapter adapter;
    private String case_id;
    private int qisu_id;
    private ArrayList<String> imagePathList = new ArrayList<>();
    private static final int REQUEST_LIST_CODE = 0;

    List<AnyouTypeInfo> anyouTypeList = new ArrayList<>();
    List<String> anyouTypeStringList;
    List<CaseAnyouTypeInfo> susongTypeList = new ArrayList<>();//法院类型
    List<CaseAnyouTypeInfo> qisuTypeList = new ArrayList<>();
    List<String> qisuTypeStringList;
    List<String> susongTypeStringList;
    List<String> fayuanYijuTypeStringList;
    private int anjianTypeIndex, susongIndex, fayuanYijuIndex, qisuIndex;

    List<CaseBeigaoInfo> caseBeigaoInfoList = new ArrayList<>();


    //    List<CaseZhengjuInfo> caseZhengjuInfoList = new ArrayList<>();
//    List<String> stringChectedListSelected = new ArrayList<>();
    List<String> zhengjuList = new ArrayList<>();
    BottomDialog bottomDialogAnyou;
    WheelView wvAnyou;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setupcase;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("手机立案");
        tv_sure.setClickable(false);
        case_id = getIntent().getStringExtra("case_id");
        //初始化图片选择器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        initBeigaoRclAdapter();
//        initSusongRclAdapter();
        initRclImages();
//        getZhengjuCategoryRquest(false);
        getQisuCategoryRquest(false);
//        getSusongCategoryRquest(false, null);
        susongAddDetails();
        getBeigaoListRquest();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.common_iv_back, R.id.tv_defendant_add, R.id.cb_shuomin, R.id.tv_sure, R.id.tv_zhengju,
            R.id.tv_qisu, R.id.tv_anyou, R.id.tv_law_add, R.id.tv_fayuan, R.id.tv_yiju, R.id.layout_add_defendant})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.tv_qisu://起诉类型选择
                if (qisuTypeStringList == null || qisuTypeStringList.size() <= 0) {
                    getQisuCategoryRquest(true);
                } else {
                    dialogWheelviewQisu();
                }
                break;
            case R.id.tv_anyou://案由选择
                showWaitDialog();
                getAnyouCategoryRquest(-1);
                break;
            case R.id.tv_fayuan://法院选择
//                dialogWheelviewFayuan();
                if (susongTypeStringList == null || susongTypeStringList.size() <= 0) {
                    getSusongCategoryRquest(true, tv_fayuan);
                } else {
                    dialogWheelviewSusong(tv_fayuan);
                }
                break;
            case R.id.tv_yiju://法院依据选择
                dialogWheelviewFayuanYiju();
                break;
            case R.id.tv_law_add://添加诉讼
                View view_law_add = LayoutInflater.from(this).inflate(R.layout.layout_law_add_first, ll_root, false);
                ll_law_add.addView(view_law_add);
                susongAddDetails();
                break;
            case R.id.tv_defendant_add://添加被告人
                Intent intentDefendant = new Intent(this, SetupcaseDefendantActivity.class);
                intentDefendant.putExtra("case_id", case_id);
                startActivityForResult(intentDefendant, 111);
                break;
            case R.id.layout_add_defendant://添加被告人
                Intent intentDefendant2 = new Intent(this, SetupcaseDefendantActivity.class);
                intentDefendant2.putExtra("case_id", case_id);
                startActivityForResult(intentDefendant2, 111);
                break;

            case R.id.cb_shuomin://说明选中
                if (cb_shuomin.isChecked()) {
                    tv_sure.setBackgroundResource(R.drawable.shape_corners5_red_bg);
                    tv_sure.setClickable(true);
                } else {
                    tv_sure.setBackgroundResource(R.drawable.shape_corners5_gray_bg);
                    tv_sure.setClickable(false);
                }
                break;
//            case R.id.tv_zhengju://证据信息
//                if (caseZhengjuInfoList == null || caseZhengjuInfoList.size() <= 0) {
//                    getZhengjuCategoryRquest(true);
//                } else {
//                    zhengjuDialogShow();
//                }
//                break;
            case R.id.tv_sure:
                updateImagesRequest();
                break;


        }
    }
    //证据选择弹窗
//    private void zhengjuDialogShow(){
//        LegalBasisDialog legalBasisDialog = new LegalBasisDialog(this, R.style.dialog, caseZhengjuInfoList);
//        legalBasisDialog.show();
//        legalBasisDialog.setDialogViewListener(new LegalBasisDialog.DialogViewListener() {
//            @Override
//            public void onListSureClick(View view) {
//                stringChectedListSelected = new ArrayList<>();
//                for (int i = 0; i < caseZhengjuInfoList.size(); i++) {
//                    CaseZhengjuInfo legalGroupInfo = caseZhengjuInfoList.get(i);
//                    List<CaseZhengjuInfo.InfoBean> legalChildInfoList = legalGroupInfo.getInfo();
//                    for (int j = 0; j < legalChildInfoList.size(); j++) {
//                        CaseZhengjuInfo.InfoBean legalChildInfo = legalChildInfoList.get(j);
//                        if (legalChildInfo.isChoosed()) {
//                            stringChectedListSelected.add(legalChildInfo.getId() + "");
//                        }
//                    }
//                }
//                if(stringChectedListSelected.size()>0){
//                    tv_zhengju.setText("已选择");
//                }else{
//                    tv_zhengju.setText("请选择");
//                }
//                Log.e("stringChectedListww---", stringChectedListSelected.size() + "");
//            }
//
//        });
//    }

    //法院依据
    public void dialogWheelviewFayuanYiju() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        String[] arrayZhiwu = getResources().getStringArray(R.array.fayuanYijuArray);
        fayuanYijuTypeStringList = Arrays.asList(arrayZhiwu);
        wv.setItems(fayuanYijuTypeStringList, fayuanYijuIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                fayuanYijuIndex = selectedIndex;
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_yiju.setText(fayuanYijuTypeStringList.get(fayuanYijuIndex));
                bottomDialog.cancel();
            }
        });
    }

    //诉讼添加时间处理
    private void susongAddDetails() {
        for (int i = 0; i < ll_law_add.getChildCount(); i++) {
            View viewchild = ll_law_add.getChildAt(i);
            TextView tv_law_add = viewchild.findViewById(R.id.tv_law_add);
            TextView tv_title_name = viewchild.findViewById(R.id.tv_title_name);
//            final TextView tv_type = viewchild.findViewById(R.id.tv_type);
            EditText et_content = viewchild.findViewById(R.id.et_content);
            EditText et_money = viewchild.findViewById(R.id.et_money);
            tv_title_name.setText("诉讼请求" + (i + 1));
            if (i == 0) {
                tv_law_add.setText("新增诉讼+");
            } else {
                tv_law_add.setText("删除-");
                final int finalI = i;
                tv_law_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_law_add.removeViewAt(finalI);
                        susongAddDetails();
                    }
                });
            }
//            tv_type.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (susongTypeStringList == null || susongTypeStringList.size() <= 0) {
//                        getSusongCategoryRquest(true, tv_type);
//                    } else {
//                        dialogWheelviewSusong(tv_type);
//                    }
//                }
//            });
        }
    }

    //底部案由
    public void dialogWheelviewAnyou() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        wvAnyou = viewDialog.findViewById(R.id.wheelView);
        bottomDialogAnyou = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialogAnyou.setContentView(viewDialog);
        bottomDialogAnyou.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        wvAnyou.setItems(anyouTypeStringList, anjianTypeIndex);//init selected position is 0 初始选中位置为0
        wvAnyou.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                anjianTypeIndex = selectedIndex;
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialogAnyou.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_title = anyouTypeStringList.get(anjianTypeIndex);
                category_id = anyouTypeList.get(anjianTypeIndex).getId();
                getAnyouCategoryRquest(anyouTypeList.get(anjianTypeIndex).getId());
                anjianTypeIndex = 0;
            }
        });
    }

    //起诉类型
    public void dialogWheelviewQisu() {
        if (qisuTypeList.size() <= 0) {
            ToastUtil.shortToast(this, "请录入起诉类型相关数据");
            return;
        }
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        wv.setItems(qisuTypeStringList, qisuIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                qisuIndex = selectedIndex;
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qisu.setText(qisuTypeStringList.get(qisuIndex));
                qisu_id = qisuTypeList.get(qisuIndex).getId();
                bottomDialog.cancel();
            }
        });
    }
    //诉讼类型
    public void dialogWheelviewSusong(final TextView textView) {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        wv.setItems(susongTypeStringList, susongIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                susongIndex = selectedIndex;
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(susongTypeStringList.get(susongIndex));
                bottomDialog.cancel();
            }
        });
    }

    //被告添加adapter
    private void initBeigaoRclAdapter() {
        rcl_beigao.setLayoutManager(new LinearLayoutManager(this));
        CaseBeigaoAddRclAdapter caseBeigaoAddRclAdapter = new CaseBeigaoAddRclAdapter(this, caseBeigaoInfoList);
        rcl_beigao.setAdapter(caseBeigaoAddRclAdapter);
        rcl_beigao.setNestedScrollingEnabled(false);
        //分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shap_recycleview_divider_thin));
        rcl_beigao.addItemDecoration(dividerItemDecoration);
        caseBeigaoAddRclAdapter.setOnViewClickListener(new CaseBeigaoAddRclAdapter.OnViewClickListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                Intent intent = new Intent(SetupcaseActivity.this, SetupcaseDefendantActivity.class);
                intent.putExtra("caseBeigaoInfo", caseBeigaoInfoList.get(position));
                intent.putExtra("case_id", case_id);
                startActivityForResult(intent, 111);
            }
        });
    }


    //图片多选
    public void initRclImages() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rcl_images.setLayoutManager(manager);
        rcl_images.setNestedScrollingEnabled(false);
        adapter = new ImageRclAdapter(this, imagePathList);
        rcl_images.setAdapter(adapter);
        adapter.setOnViewClickListener(new ImageRclAdapter.OnViewClickListener() {
            @Override
            public void onAddViewClick(View view) {
                imageMultiselect(view);
            }

            @Override
            public void onDeleteViewClick(View view) {
                tv_zhegnju_count.setText(imagePathList.size() + "/10");
            }
        });
    }

    //图片多选
    public void imageMultiselect(View view) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(true)
                // 是否记住上次选中记录
                .rememberSelected(false)
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#9f2613"))
                // 返回图标ResId
                .backResId(R.mipmap.icon_back_white)
                .title("图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#9f2613"))
                .allImagesText("所有图片")
                // 最大选择图片数量
                .maxNum(10)
                .build();

        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            imagePathList.addAll(pathList);
            adapter.notifyDataSetChanged();
            tv_zhegnju_count.setText(imagePathList.size() + "/10");

            // 测试Fresco
            // draweeView.setImageURI(Uri.parse("file://"+pathList.get(0)));
//            for (String path : pathList) {
//                tvResult.append(path + "\n");
//            }
        } else if (resultCode == 200 && requestCode == 111) {
            getBeigaoListRquest();
        }
    }


    //案由类别
    private void getAnyouCategoryRquest(final int parent_id) {
        RetrofitClient.getInstance().postMapGeAnyouTypeInfoCategory(parent_id, new BaseSubscriber<HttpResponse<List<AnyouTypeInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideWaitDialog();
                Toast.makeText(SetupcaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<AnyouTypeInfo>> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    anyouTypeList = httpResponse.getData();
                    anyouTypeStringList = new ArrayList<>();
                    for (int i = 0; i < anyouTypeList.size(); i++) {
                        anyouTypeStringList.add(anyouTypeList.get(i).getTitle());
                    }
                    if (parent_id == -1) {
                        dialogWheelviewAnyou();
                    } else {
                        if (anyouTypeList.size() > 0) {
                            wvAnyou.setItems(anyouTypeStringList, 0);//init selected position is 0 初始选中位置为0
                        } else {
                            tv_anyou.setText(category_title);
                            bottomDialogAnyou.cancel();
                        }
                    }
                } else {
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //起诉类别
    private void getQisuCategoryRquest(final boolean isShow) {
        RetrofitClient.getInstance().postMapGeQisuTypeInfoCategory(new BaseSubscriber<HttpResponse<List<CaseAnyouTypeInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideWaitDialog();
                Toast.makeText(SetupcaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<CaseAnyouTypeInfo>> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    qisuTypeList = httpResponse.getData();
                    qisuTypeStringList = new ArrayList<>();
                    for (int i = 0; i < qisuTypeList.size(); i++) {
                        qisuTypeStringList.add(qisuTypeList.get(i).getTitle());
                    }
                    if (isShow) {
                        dialogWheelviewQisu();
                    }
                } else {
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //法院类别
    private void getSusongCategoryRquest(final boolean isShow, final TextView textView) {
        RetrofitClient.getInstance().postMapGeSusongTypeInfoCategory(new BaseSubscriber<HttpResponse<List<CaseAnyouTypeInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideWaitDialog();
                Toast.makeText(SetupcaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<CaseAnyouTypeInfo>> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    susongTypeList = httpResponse.getData();
                    susongTypeStringList = new ArrayList<>();
                    for (int i = 0; i < susongTypeList.size(); i++) {
                        susongTypeStringList.add(susongTypeList.get(i).getTitle());
                    }
                    if (isShow) {
                        dialogWheelviewSusong(textView);
                    }
                } else {
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (isShow) {
            showWaitDialog();
        }
    }

    //证据信息请求类别
//    private void getZhengjuCategoryRquest(final boolean isShow) {
//        RetrofitClient.getInstance().postMapGetSusongRequestCategory(new BaseSubscriber<HttpResponse<List<CaseZhengjuInfo>>>() {
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//                hideWaitDialog();
//                Toast.makeText(SetupcaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNext(HttpResponse<List<CaseZhengjuInfo>> httpResponse) {
//                hideWaitDialog();
//                Gson gson = new Gson();
//                Log.i("onNext", gson.toJson(httpResponse));
//                if (httpResponse.getCode() == 0) {
//                    caseZhengjuInfoList = httpResponse.getData();
//                    if (isShow) {
//                        zhengjuDialogShow();
//                    }
//                } else {
//                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


    //添加案件接口请求
    private void caseAddRequest() {
        String qisuString = tv_qisu.getText().toString();
        String anyou = tv_anyou.getText().toString();
        String fayuan = tv_fayuan.getText().toString();
        String yiju = tv_yiju.getText().toString();
        String content = et_content.getText().toString();
        if (StringUtil.isEmpty(qisuString)) {
            Toast.makeText(SetupcaseActivity.this, "请选择起诉类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(anyou)) {
            Toast.makeText(SetupcaseActivity.this, "请选择案由", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(fayuan)) {
            Toast.makeText(SetupcaseActivity.this, "请选择管辖法院", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(yiju)) {
            Toast.makeText(SetupcaseActivity.this, "请选择管辖依据", Toast.LENGTH_SHORT).show();
            return;
        }

        if (caseBeigaoInfoList.size() <= 0) {
            Toast.makeText(SetupcaseActivity.this, "请添加被告人", Toast.LENGTH_SHORT).show();
            return;
        }
        //被告
        List<String> caseBeigaoInfoStringList = new ArrayList<>();
        for (int i = 0; i < caseBeigaoInfoList.size(); i++) {
            caseBeigaoInfoStringList.add(caseBeigaoInfoList.get(i).getId() + "");
        }

        if (StringUtil.isEmpty(content)) {
            Toast.makeText(SetupcaseActivity.this, "请输入案件内容", Toast.LENGTH_SHORT).show();
            return;
        }

        //法律依据
//        if (stringChectedListSelected.size() <= 0) {
//            Toast.makeText(SetupcaseActivity.this, "请选择法律依据", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //证据描述
        String remark = et_remark.getText().toString();

        //诉讼信息
        ListSusongInfoPost listSusongInfoPost = new ListSusongInfoPost();
        listSusongInfoPost.setQisu(qisu_id);
        listSusongInfoPost.setCase_id(case_id);
        listSusongInfoPost.setCategory_id(category_id);
        listSusongInfoPost.setContent(content);
        listSusongInfoPost.setFayuan(fayuan);
        listSusongInfoPost.setBeigao(caseBeigaoInfoStringList);
//        listSusongInfoPost.setFalv(stringChectedListSelected);
        listSusongInfoPost.setImages(zhengjuList);
        listSusongInfoPost.setYiju(yiju);
        listSusongInfoPost.setRemark(remark);
        //s诉讼添加
        List<SusongPostInfo> caseSusongInfoArrayList = new ArrayList<>();
        for (int i = 0; i < ll_law_add.getChildCount(); i++) {
            View viewchild = ll_law_add.getChildAt(i);
//            TextView tv_type = viewchild.findViewById(R.id.tv_type);
            EditText et_content = viewchild.findViewById(R.id.et_content);
            EditText et_money = viewchild.findViewById(R.id.et_money);
//            String type = tv_type.getText().toString();
            String susongcontent = et_content.getText().toString();
            String money = et_money.getText().toString();
//            if (StringUtil.isEmpty(type)) {
//                Toast.makeText(SetupcaseActivity.this, "请选择诉讼请求" + (i + 1) + "的类型", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (StringUtil.isEmpty(susongcontent)) {
                Toast.makeText(SetupcaseActivity.this, "请选择诉讼请求" + (i + 1) + "的内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(money)) {
                Toast.makeText(SetupcaseActivity.this, "请选择诉讼请求" + (i + 1) + "的补偿金额", Toast.LENGTH_SHORT).show();
                return;
            }
            SusongPostInfo susongPostInfopost = new SusongPostInfo();
            susongPostInfopost.setCase_id(case_id);
            susongPostInfopost.setContent(susongcontent);
            susongPostInfopost.setMoney(money);
//            for (int k = 0; k < susongTypeList.size(); k++) {
//                if (type.equals(susongTypeList.get(k).getTitle())) {
//                    susongPostInfopost.setType(susongTypeList.get(k).getId() + "");
//                }
//            }

            caseSusongInfoArrayList.add(susongPostInfopost);
        }
        listSusongInfoPost.setSusong(caseSusongInfoArrayList);
        RetrofitClient.getInstance().postMapCaseAdd2(listSusongInfoPost, new BaseSubscriber<HttpResponse<String>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                hideWaitDialog();
                Toast.makeText(SetupcaseActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(HttpResponse<String> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    Toast.makeText(SetupcaseActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MessageEvent<String>("updateCase"));
                    finish();
                } else {
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //图片上传接口请求
    private void updateImagesRequest() {
        if (imagePathList.size() <= 0) {
            Toast.makeText(SetupcaseActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().postMapImagesAdd(imagePathList, new BaseSubscriber<HttpResponse<List<String>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                Toast.makeText(SetupcaseActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }


            @Override
            public void onNext(HttpResponse<List<String>> httpResponse) {
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    zhengjuList = httpResponse.getData();
                    caseAddRequest();
                } else {
                    hideWaitDialog();
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        showWaitDialog();
    }


    //获取被告信息
    private void getBeigaoListRquest() {
        RetrofitClient.getInstance().postMapGetBeigaoList(case_id, new BaseSubscriber<HttpResponse<List<CaseBeigaoInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideWaitDialog();
                Toast.makeText(SetupcaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<CaseBeigaoInfo>> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    caseBeigaoInfoList = httpResponse.getData();
                    initBeigaoRclAdapter();
                    if (caseBeigaoInfoList.size() > 0) {
                        layout_add_defendant.setVisibility(View.GONE);
                    } else {
                        layout_add_defendant.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(SetupcaseActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
