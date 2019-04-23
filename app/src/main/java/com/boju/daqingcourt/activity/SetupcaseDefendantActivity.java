package com.boju.daqingcourt.activity;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.boju.daqingcourt.R;
import com.boju.daqingcourt.base.BaseActivity;
import com.boju.daqingcourt.bean.CaseAnyouTypeInfo;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;
import com.boju.daqingcourt.bean.MinzuInfo;
import com.boju.daqingcourt.citypicker.CityPicker;
import com.boju.daqingcourt.dialog.BottomDialog;
import com.boju.daqingcourt.retrofitclient.BaseSubscriber;
import com.boju.daqingcourt.retrofitclient.ExceptionHandle;
import com.boju.daqingcourt.retrofitclient.HttpResponse;
import com.boju.daqingcourt.retrofitclient.RetrofitClient;
import com.boju.daqingcourt.utils.DialogHelp;
import com.boju.daqingcourt.utils.StringUtil;
import com.boju.daqingcourt.utils.ToastUtil;
import com.boju.daqingcourt.widget.wheelview.WheelView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/15.
 * 被告信息
 */

public class SetupcaseDefendantActivity extends BaseActivity {
    @BindView(R.id.tv_common_title_name)
    TextView tv_common_title_name;
    @BindView(R.id.rg_check)
    RadioGroup rg_check;
    @BindView(R.id.rb_button1)
    RadioButton rb_button1;
    @BindView(R.id.rb_button2)
    RadioButton rb_button2;
    @BindView(R.id.layout_defendant1)
    View layout_defendant1;
    @BindView(R.id.layout_defendant2)
    View layout_defendant2;
    @BindView(R.id.tv_yuangao_name)
    EditText tv_yuangao_name;
    @BindView(R.id.tv_beigao_name)
    EditText tv_beigao_name;
    @BindView(R.id.et_card)
    TextView et_card;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_minzu)
    TextView tv_minzu;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.et_address_details)
    EditText et_address_details;
    @BindView(R.id.et_faren)
    EditText et_faren;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.et_link)
    TextView et_link;
    @BindView(R.id.et_mobile_beiao2)
    EditText et_mobile_beiao2;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.tv_reg_address)
    TextView tv_reg_address;
    @BindView(R.id.et_reg_address_details)
    EditText et_reg_address_details;
    @BindView(R.id.tv_tong_address)
    TextView tv_tong_address;
    @BindView(R.id.et_tong_address_details)
    EditText et_tong_address_details;
    @BindView(R.id.et_tel)
    EditText et_tel;
    @BindView(R.id.rl_container)
    View rl_container;

    List<MinzuInfo> minzuTypeList = new ArrayList<>();
    List<String> minzuTypeStringList;
    List<String> sexTypeStringList;
    List<String> zhiwuTypeStringList;
    private int minzuTypeIndex, sexTypeIndex = 0, zhiwuTypeIndex = 0;
    int type = 1, updateType = 1;
    String case_id;
    private CityPicker mCityPicker;
    private CityPicker mCityPicker2;
    private CityPicker mCityPicker3;

    CaseBeigaoInfo caseBeigaoInfo;
    int bei_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setupcase_defendant;
    }

    @Override
    public void initView() {
        super.initView();
        tv_common_title_name.setText("被告信息");
        case_id = getIntent().getStringExtra("case_id");
        rb_button1.setChecked(true);
        layout_defendant1.setVisibility(View.VISIBLE);
        layout_defendant2.setVisibility(View.GONE);
//        rg_check.check(R.id.rb_button1);
        rg_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_button1:
                        layout_defendant1.setVisibility(View.VISIBLE);
                        layout_defendant2.setVisibility(View.GONE);
                        type = 1;
                        break;
                    case R.id.rb_button2:
                        layout_defendant1.setVisibility(View.GONE);
                        layout_defendant2.setVisibility(View.VISIBLE);
                        type = 2;
                        break;
                }
            }
        });
        caseBeigaoInfo = (CaseBeigaoInfo) getIntent().getSerializableExtra("caseBeigaoInfo");
        if (caseBeigaoInfo != null) {
            setDataDetails();
        }
        getMinzuCategoryRquest(false);
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void setDataDetails() {
        bei_id = caseBeigaoInfo.getId();
        updateType = caseBeigaoInfo.getType();
        if (updateType == 1) {
            rg_check.check(R.id.rb_button1);
            tv_yuangao_name.setText(caseBeigaoInfo.getName());
            et_card.setText(caseBeigaoInfo.getCard());
            tv_sex.setText(caseBeigaoInfo.getSex());
            tv_minzu.setText(caseBeigaoInfo.getNation());
            et_mobile.setText(caseBeigaoInfo.getMobile());
            tv_address.setText(caseBeigaoInfo.getProvince() + "-" + caseBeigaoInfo.getCity() + "-" + caseBeigaoInfo.getArea());
            et_address_details.setText(caseBeigaoInfo.getAddress());
        } else if (updateType == 2) {
            rg_check.check(R.id.rb_button2);
            tv_beigao_name.setText(caseBeigaoInfo.getName());
            et_faren.setText(caseBeigaoInfo.getFaren());
            tv_job.setText(caseBeigaoInfo.getJob());
            et_link.setText(caseBeigaoInfo.getLink());
            et_mobile_beiao2.setText(caseBeigaoInfo.getMobile());
            et_email.setText(caseBeigaoInfo.getEmail());
            tv_reg_address.setText(caseBeigaoInfo.getReg_province() + "-" + caseBeigaoInfo.getReg_city() + "-" + caseBeigaoInfo.getReg_area());
            et_reg_address_details.setText(caseBeigaoInfo.getReg_address());
            tv_tong_address.setText(caseBeigaoInfo.getProvince() + "-" + caseBeigaoInfo.getCity() + "-" + caseBeigaoInfo.getArea());
            et_tong_address_details.setText(caseBeigaoInfo.getAddress());
            et_tel.setText(caseBeigaoInfo.getTel());
        }
    }

    @OnClick({R.id.common_iv_back, R.id.tv_minzu, R.id.tv_reg_address, R.id.tv_job, R.id.tv_sex, R.id.tv_address,
            R.id.tv_tong_address, R.id.tv_sure, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.tv_sex:
                dialogWheelviewSex();
                break;
            case R.id.tv_minzu://民族选择
                if (minzuTypeStringList == null || minzuTypeStringList.size() <= 0) {
                    getMinzuCategoryRquest(true);
                } else {
                    dialogWheelviewMinzu();
                }
                break;
            case R.id.tv_job:
                dialogWheelviewZhiwu();
                break;
            case R.id.tv_address://自然人地址
                if (mCityPicker == null) {
                    mCityPicker = new CityPicker(SetupcaseDefendantActivity.this, rl_container)
                            .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {
                                @Override
                                public void onCitySelect(String province, String city, String county) {
                                    String address = province + "-" + city + "-" + county;
                                    tv_address.setText(address);
                                }
                            });
                }
                mCityPicker.show();
                break;
            case R.id.tv_reg_address://公司注册地址
                if (mCityPicker2 == null) {
                    mCityPicker2 = new CityPicker(SetupcaseDefendantActivity.this, rl_container)
                            .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {
                                @Override
                                public void onCitySelect(String province, String city, String county) {
                                    String address = province + "-" + city + "-" + county;
                                    tv_reg_address.setText(address);
                                }
                            });
                }
                mCityPicker2.show();
                break;
            case R.id.tv_tong_address://公司通讯地址
                if (mCityPicker3 == null) {
                    mCityPicker3 = new CityPicker(SetupcaseDefendantActivity.this, rl_container)
                            .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {
                                @Override
                                public void onCitySelect(String province, String city, String county) {
                                    String address = province + "-" + city + "-" + county;
                                    tv_tong_address.setText(address);
                                }
                            });
                }
                mCityPicker3.show();
                break;

            case R.id.tv_sure://提交
                if ((caseBeigaoInfo != null) && (updateType == type)) {
                    beigaoAddRequestUpdate();
                } else {
                    beigaoAddRequest();
                }
                break;
            case R.id.tv_delete://删除
                DialogHelp.getConfirmDialog(this, "提示", "确定删除此项被告", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        deleteRequest();

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
                break;

        }
    }

    //性别类型
    public void dialogWheelviewSex() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        String[] arraySex = getResources().getStringArray(R.array.sexArray);
        sexTypeStringList = Arrays.asList(arraySex);
        wv.setItems(sexTypeStringList, sexTypeIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                sexTypeIndex = selectedIndex;
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
                tv_sex.setText(sexTypeStringList.get(sexTypeIndex));
                bottomDialog.cancel();
            }
        });
    }

    //职务类型
    public void dialogWheelviewZhiwu() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        String[] arrayZhiwu = getResources().getStringArray(R.array.zhiwuArray);
        zhiwuTypeStringList = Arrays.asList(arrayZhiwu);
        wv.setItems(zhiwuTypeStringList, zhiwuTypeIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                zhiwuTypeIndex = selectedIndex;
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
                tv_job.setText(zhiwuTypeStringList.get(zhiwuTypeIndex));
                bottomDialog.cancel();
            }
        });
    }

    //民族类型
    public void dialogWheelviewMinzu() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_wheelview, null);
        WheelView wv = viewDialog.findViewById(R.id.wheelView);
        final BottomDialog bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show();//显示对话框

        TextView tv_cancle = viewDialog.findViewById(R.id.tv_cancle);
        TextView tv_sure = viewDialog.findViewById(R.id.tv_sure);

        wv.setItems(minzuTypeStringList, minzuTypeIndex);//init selected position is 0 初始选中位置为0
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                minzuTypeIndex = selectedIndex;
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
                tv_minzu.setText(minzuTypeStringList.get(minzuTypeIndex));
                bottomDialog.cancel();
            }
        });
    }

    //民族类别
    private void getMinzuCategoryRquest(final boolean isShow) {
        RetrofitClient.getInstance().postMapGeMinzuCategory(new BaseSubscriber<HttpResponse<List<MinzuInfo>>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                hideWaitDialog();
                Toast.makeText(SetupcaseDefendantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResponse<List<MinzuInfo>> httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    minzuTypeList = httpResponse.getData();
                    minzuTypeStringList = new ArrayList<>();
                    for (int i = 0; i < minzuTypeList.size(); i++) {
                        minzuTypeStringList.add(minzuTypeList.get(i).getValue());
                    }
                    if (isShow) {
                        dialogWheelviewMinzu();
                    }
                } else {
                    Toast.makeText(SetupcaseDefendantActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (isShow) {
            showWaitDialog();
        }
    }


    //被告接口请求
    private void beigaoAddRequest() {
        String name = "", card = "", sex = "", minzu = "", mobile = "", address = "", addressDetails = "";
        String faren = "", job = "", link = "", email = "", reg_address = "", reg_address_details = "", tel = "";
        String reg_province = "", reg_city = "", reg_area = "";
        String province = "", city = "", area = "";
        if (type == 1) {
            name = tv_yuangao_name.getText().toString();
            card = et_card.getText().toString();
            sex = tv_sex.getText().toString();
            minzu = tv_minzu.getText().toString();
            mobile = et_mobile.getText().toString();
            address = tv_address.getText().toString();
            addressDetails = et_address_details.getText().toString();
            if (StringUtil.isEmpty(name)) {
                Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
                return;
            }
//            if (StringUtil.isEmpty(card)) {
//                Toast.makeText(this, "请选择管辖法院", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (StringUtil.isEmpty(sex)) {
//                Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (StringUtil.isEmpty(minzu)) {
//                Toast.makeText(this, "请选择民族", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (StringUtil.isEmpty(mobile)) {
//                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (StringUtil.isEmpty(address)) {
//                Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (StringUtil.isEmpty(addressDetails)) {
//                Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (!StringUtil.isEmpty(address)) {
                String[] addressArray = address.split("-");
                province = addressArray[0];
                city = addressArray[1];
                area = addressArray[2];
            }
        } else if (type == 2) {
            name = tv_beigao_name.getText().toString();
            faren = et_faren.getText().toString();
            job = tv_job.getText().toString();
            link = et_link.getText().toString();
            mobile = et_mobile_beiao2.getText().toString();
            email = et_email.getText().toString();
            reg_address = tv_reg_address.getText().toString();
            reg_address_details = et_reg_address_details.getText().toString();
            address = tv_tong_address.getText().toString();
            addressDetails = et_tong_address_details.getText().toString();
            tel = et_tel.getText().toString();
            if (StringUtil.isEmpty(name)) {
                Toast.makeText(this, "请输入公司名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(faren)) {
                Toast.makeText(this, "请输入法人代表", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(faren)) {
                Toast.makeText(this, "请输入法人代表", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(job)) {
                Toast.makeText(this, "请输入法人代表职务", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(mobile)) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(reg_address)) {
                Toast.makeText(this, "请选择注册地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(reg_address_details)) {
                Toast.makeText(this, "请输入详细注册地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(address)) {
                Toast.makeText(this, "请选择通讯地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(addressDetails)) {
                Toast.makeText(this, "请输入详细通讯地址", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] regAddressArray = reg_address.split("-");
            reg_province = regAddressArray[0];
            reg_city = regAddressArray[1];
            reg_area = regAddressArray[2];

            String[] addressArray = address.split("-");
            province = addressArray[0];
            city = addressArray[1];
            area = addressArray[2];
        }

        RetrofitClient.getInstance().postMapBeigaoAdd(case_id, type, name, card, faren, job, sex, minzu,
                link, mobile, email, reg_province, reg_city, reg_area, reg_address_details, tel,
                province, city, area, addressDetails, new BaseSubscriber<HttpResponse<String>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        hideWaitDialog();
                        Toast.makeText(SetupcaseDefendantActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onNext(HttpResponse<String> httpResponse) {
                        hideWaitDialog();
                        Gson gson = new Gson();
                        Log.i("onNext", gson.toJson(httpResponse));
                        if (httpResponse.getCode() == 0) {
                            Toast.makeText(SetupcaseDefendantActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            setResult(200);
                            finish();
                        } else {
                            Toast.makeText(SetupcaseDefendantActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        showWaitDialog();
    }

    //被告接口请求
    private void beigaoAddRequestUpdate() {
        String name = "", card = "", sex = "", minzu = "", mobile = "", address = "", addressDetails = "";
        String faren = "", job = "", link = "", email = "", reg_address = "", reg_address_details = "", tel = "";
        String reg_province = "", reg_city = "", reg_area = "";
        String province = "", city = "", area = "";
        if (updateType == 1) {
            name = tv_yuangao_name.getText().toString();
            card = et_card.getText().toString();
            sex = tv_sex.getText().toString();
            minzu = tv_minzu.getText().toString();
            mobile = et_mobile.getText().toString();
            address = tv_address.getText().toString();
            addressDetails = et_address_details.getText().toString();
            if (StringUtil.isEmpty(name)) {
                Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(card)) {
                Toast.makeText(this, "请选择管辖法院", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(sex)) {
                Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(minzu)) {
                Toast.makeText(this, "请选择民族", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(mobile)) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(address)) {
                Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(addressDetails)) {
                Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] addressArray = address.split("-");
            province = addressArray[0];
            city = addressArray[1];
            area = addressArray[2];

        } else if (updateType == 2) {
            name = tv_beigao_name.getText().toString();
            faren = et_faren.getText().toString();
            job = tv_job.getText().toString();
            link = et_link.getText().toString();
            mobile = et_mobile_beiao2.getText().toString();
            email = et_email.getText().toString();
            reg_address = tv_reg_address.getText().toString();
            reg_address_details = et_reg_address_details.getText().toString();
            address = tv_tong_address.getText().toString();
            addressDetails = et_tong_address_details.getText().toString();
            tel = et_tel.getText().toString();
            if (StringUtil.isEmpty(name)) {
                Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(faren)) {
                Toast.makeText(this, "请输入法人代表", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(faren)) {
                Toast.makeText(this, "请输入法人代表", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(job)) {
                Toast.makeText(this, "请输入法人代表职务", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(mobile)) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(reg_address)) {
                Toast.makeText(this, "请选择注册地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(reg_address_details)) {
                Toast.makeText(this, "请输入详细注册地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(address)) {
                Toast.makeText(this, "请选择通讯地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isEmpty(addressDetails)) {
                Toast.makeText(this, "请输入详细通讯地址", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] regAddressArray = reg_address.split("-");
            reg_province = regAddressArray[0];
            reg_city = regAddressArray[1];
            reg_area = regAddressArray[2];

            String[] addressArray = address.split("-");
            province = addressArray[0];
            city = addressArray[1];
            area = addressArray[2];
        }

        RetrofitClient.getInstance().postMapBeigaoUpdate(bei_id, updateType, name, card, faren, job, sex, minzu,
                link, mobile, email, reg_province, reg_city, reg_area, reg_address_details, tel,
                province, city, area, addressDetails, new BaseSubscriber<HttpResponse<String>>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        hideWaitDialog();
                        Toast.makeText(SetupcaseDefendantActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onNext(HttpResponse<String> httpResponse) {
                        hideWaitDialog();
                        Gson gson = new Gson();
                        Log.i("onNext", gson.toJson(httpResponse));
                        if (httpResponse.getCode() == 0) {
                            Toast.makeText(SetupcaseDefendantActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            setResult(200);
                            finish();
                        } else {
                            Toast.makeText(SetupcaseDefendantActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        showWaitDialog();
    }

    //删除
    private void deleteRequest() {
        RetrofitClient.getInstance().postMapBeigaoDelete(bei_id, new BaseSubscriber<HttpResponse>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                Toast.makeText(SetupcaseDefendantActivity.this, responeThrowable.message, Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onNext(HttpResponse httpResponse) {
                hideWaitDialog();
                Gson gson = new Gson();
                Log.i("onNext", gson.toJson(httpResponse));
                if (httpResponse.getCode() == 0) {
                    setResult(200);
                    finish();
                } else {
                    Toast.makeText(SetupcaseDefendantActivity.this, httpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        showWaitDialog();
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
