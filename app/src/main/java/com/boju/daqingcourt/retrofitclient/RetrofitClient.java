package com.boju.daqingcourt.retrofitclient;


import android.support.v4.util.ArrayMap;

import com.boju.daqingcourt.AppApplication;
import com.boju.daqingcourt.bean.AnyouTypeInfo;
import com.boju.daqingcourt.bean.ArticleInfo;
import com.boju.daqingcourt.bean.BannerInfo;
import com.boju.daqingcourt.bean.CaseAnyouTypeInfo;
import com.boju.daqingcourt.bean.CaseBeigaoInfo;
import com.boju.daqingcourt.bean.CaseInfo;
import com.boju.daqingcourt.bean.CaseInfoDetails;
import com.boju.daqingcourt.bean.CaseZhengjuInfo;
import com.boju.daqingcourt.bean.DocumentInfo;
import com.boju.daqingcourt.bean.IconGonggaoInfo;
import com.boju.daqingcourt.bean.ListSusongInfoPost;
import com.boju.daqingcourt.bean.MinzuInfo;
import com.boju.daqingcourt.bean.SifaVideoInfo;
import com.boju.daqingcourt.bean.SusongPostInfo;
import com.boju.daqingcourt.bean.UserInfo;
import com.boju.daqingcourt.utils.PropertyUtil;
import com.boju.daqingcourt.utils.StringUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RetrofitClient
 * Created by Tamic on 2016-06-15.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public class RetrofitClient {
    private ApiService apiService;

    private OkHttpClient okHttpClient;
    private static RetrofitClient mInstance;

    public static String baseUrl = ApiService.Base_URL;

    public static RetrofitClient getInstance() {
        mInstance = new RetrofitClient();
        return mInstance;
    }

    public static RetrofitClient getInstance(String url) {
        mInstance = new RetrofitClient(url);
        return mInstance;
    }

    private RetrofitClient() {
        this(null);
    }

    public RetrofitClient(String url) {
        if (StringUtil.isEmpty(url)) {
            url = baseUrl;
        }
        Cache cache = new Cache(AppApplication.getInstance().getCacheDir(), 50 * 1024 * 1024);
        //添加拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                //设置请求读写的超时时间
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    /**
     * @param value
     * @return Part请求带双引号处理
     */
    public RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    //用户端参数基类
    public ArrayMap<String, Object> getBaseMap() {
        ArrayMap<String, Object> map = new ArrayMap<>();
        String user_id = PropertyUtil.getPropertyUtil().getProperty("user_id");
        if (StringUtil.isEmpty(user_id)) {
            map.put("user_id", "");
        } else {
            map.put("user_id", user_id);
        }
        return map;
    }

    //post map请求接受订单消息状态修改
    public void postArticleInfo(int page, int page_count, BaseSubscriber<HttpResponse<List<ArticleInfo>>> subscriber) {
        String url = "api/getArticles/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("page", page);
        parameters.put("page_count", page_count);
        apiService.executeArticleInfoList(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map  shouye 轮播图请求
    public void postIndexBannerList(BaseSubscriber<HttpResponse<BannerInfo>> subscriber) {
        String url = "api/getBanners/";
        Map<String, Object> parameters = new ArrayMap<>();
        apiService.executeIndexBanner(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求接受订单消息状态修改
    public void postIconGonggaoInfo(int page, int page_count, String keywords, String start_time, String end_time, BaseSubscriber<HttpResponse<List<IconGonggaoInfo>>> subscriber) {
        String url = "api/getNotice/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("page", page);
        parameters.put("page_count", page_count);
        parameters.put("keywords", keywords);
        parameters.put("start_time", start_time);
        parameters.put("end_time", end_time);
        apiService.executeIconGonggaoInfoList(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map  shouye 文章详情
    public void postMapArticleDetails( Map<String, Object> parameters,BaseSubscriber<HttpResponse<ArticleInfo>> subscriber) {
        String url = "api/getArticle/";
        apiService.executeArticleInfo(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //post map请求 视频列表
    public void postIconSifaVideoInfo(int page, int page_count, String keywords, BaseSubscriber<HttpResponse<List<SifaVideoInfo>>> subscriber) {
        String url = "api/getVideo/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("page", page);
        parameters.put("page_count", page_count);
        parameters.put("keywords", keywords);
        apiService.executeIconSifaVideoInfoList(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 验证码
    public void postMapSendSms(String mobile, BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "api/getCode/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("mobile", mobile);
        apiService.excutePostSendSmsCode(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 用户端登录
    public void postMapUserLogin(String mobile, String code, String msg_id, BaseSubscriber<HttpResponse<UserInfo>> subscriber) {
        String url = "user/postUser/";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mobile", mobile);
        parameters.put("code", code);
        parameters.put("msg_id", msg_id);
        apiService.executePostMapLoginUser(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求 用户端登录
    public void postMapUserInformation(String user_id,String name, String card, BaseSubscriber<HttpResponse<UserInfo>> subscriber) {
        String url = "user/putUser/";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("name", name);
        parameters.put("card", card);
        apiService.executePostMapLoginUser(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 我的案件列表
    public void postCasesInfoList(int page, int page_count, BaseSubscriber<HttpResponse<List<CaseInfo>>> subscriber) {
        String url = "User/getUserCases/";
        Map<String, Object> parameters = getBaseMap();
        parameters.put("page", page);
        parameters.put("page_count", page_count);
        apiService.executeCaseInfoList(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //post map  文书公开列表请求
    public void postDocumentInfoList(BaseSubscriber<HttpResponse<List<DocumentInfo>>> subscriber) {
        String url = "api/getOpens/";
        Map<String, Object> parameters = new ArrayMap<>();
        apiService.executeDocumentInfoList(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 立案-案由类别
    public void postMapGeAnyouTypeInfoCategory(int parent_id,BaseSubscriber<HttpResponse<List<AnyouTypeInfo>>> subscriber) {
        String url = "api/getCaseCategory/";
        Map<String, Object> parameters = new ArrayMap<>();
        if(parent_id!=-1){
            parameters.put("parent_id",parent_id);
        }
        apiService.executePostAnyouTypeInfo(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求 立案-法院类别
    public void postMapGeSusongTypeInfoCategory(BaseSubscriber<HttpResponse<List<CaseAnyouTypeInfo>>> subscriber) {
        String url = "api/getCategory/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("channel_id",6);
        apiService.executePostCaseAnyouTypeInfo(url,parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求 立案-起诉类别
    public void postMapGeQisuTypeInfoCategory(BaseSubscriber<HttpResponse<List<CaseAnyouTypeInfo>>> subscriber) {
        String url = "api/getCategory/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("channel_id",5);
        apiService.executePostCaseAnyouTypeInfo(url,parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求 被告人-民族类别
    public void postMapGeMinzuCategory(BaseSubscriber<HttpResponse<List<MinzuInfo>>> subscriber) {
        String url = "api/getNation/";
        apiService.executePostMinzuInfo(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求 立案-诉讼类别
    public void postMapGetSusongRequestCategory(BaseSubscriber<HttpResponse<List<CaseZhengjuInfo>>> subscriber) {
        String url = "api/getCategory/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("parent_id",6);
        apiService.executePostCaseZhengjuInfo(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 提交原告信息
    public void postMapYuangao(String mobile,String email,String tel,String code,String now_province,String now_city,String now_area,String now_address,
                               String you_province,String you_city,String you_area,String you_address,BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "Cases/postYuan/";
        Map<String, Object> parameters = getBaseMap();
        parameters.put("mobile",mobile);
        parameters.put("email",email);
        parameters.put("tel",tel);
        parameters.put("code",code);
        parameters.put("now_province",now_province);
        parameters.put("now_city",now_city);
        parameters.put("now_area",now_area);
        parameters.put("now_address",now_address);
        parameters.put("you_province",you_province);
        parameters.put("you_city",you_city);
        parameters.put("you_area",you_area);
        parameters.put("you_address",you_address);
        apiService.executeyuangao(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 获取个人信息
    public void postMapMyInformation(BaseSubscriber<HttpResponse<UserInfo>> subscriber) {
        String url = "user/getUser/";
        Map<String, Object> parameters = getBaseMap();
        apiService.executePostMapInformation(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //post map请求
    public void postMapCaseAdd(String case_id, int category_id, String fayuan, String yiju, String content, List<String>  falv, List<String> listimages, String remark,
                               List<SusongPostInfo> susongPostInfoList, List<String> beigaoInfoList, BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "Cases/postCase/";
        Map<String, Object> parameters = getBaseMap();
        parameters.put("case_id",case_id);
        parameters.put("category_id",category_id);
        parameters.put("fayuan",fayuan);
        parameters.put("yiju",yiju);
        parameters.put("content",content);
        parameters.put("remark",remark);
        apiService.executeCaseAdd(url, parameters,listimages,falv,beigaoInfoList,susongPostInfoList)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //post map请求
    public void postMapCaseAdd2(ListSusongInfoPost listSusongInfoPost,BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "Cases/postCase/";
        apiService.executeCaseAdd2(url, listSusongInfoPost)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 获取原告信息
    public void postMapBeigaoAdd(String case_id, int type,  String name, String card, String faren, String job, String sex, String nation, String link,
                              String mobile, String email, String reg_province,String 	reg_city,String reg_area,String reg_address,
                               String tel,String province,String city,String area,String address,BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "Cases/postBei/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("case_id",case_id);
        parameters.put("type",type);
        parameters.put("name",name);
        parameters.put("card",card);
        parameters.put("faren",faren);
        parameters.put("job",job);
        parameters.put("sex",sex);
        parameters.put("nation",nation);
        parameters.put("link",link);
        parameters.put("mobile",mobile);
        parameters.put("email",email);
        parameters.put("reg_province",reg_province);
        parameters.put("reg_city",reg_city);
        parameters.put("reg_area",reg_area);
        parameters.put("reg_address",reg_address);
        parameters.put("tel",tel);
        parameters.put("province",province);
        parameters.put("city",city);
        parameters.put("area",area);
        parameters.put("address",address);
        apiService.executeyuangao(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 病患端-首页患者提问
    public void postMapImagesAdd( List<String> stringList, BaseSubscriber<HttpResponse<List<String>>> subscriber) {
        Map<String, RequestBody> partParams = new HashMap<>();
        for (String imgStr : stringList) {
            File file = new File(imgStr);
            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            //一定要加("AttachmentKey\"; filename=\"" +，不然失败
            partParams.put("file[]\"; filename=\"" + file.getName(), requestFile);
        }
        apiService.upLoadMultiImgsPartMapQuestion(partParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 立案-诉讼类别
    public void postMapGetBeigaoList(String case_id,BaseSubscriber<HttpResponse<List<CaseBeigaoInfo>>> subscriber) {
        String url = "Cases/getBeis/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("case_id",case_id);
        apiService.executePostCaseBeigaoInfo(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 被告人修改信息
    public void postMapBeigaoUpdate(int bei_id, int type,  String name, String card, String faren, String job, String sex, String nation, String link,
                                 String mobile, String email, String reg_province,String 	reg_city,String reg_area,String reg_address,
                                 String tel,String province,String city,String area,String address,BaseSubscriber<HttpResponse<String>> subscriber) {
        String url = "Cases/putBei/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("bei_id",bei_id);
        parameters.put("type",type);
        parameters.put("name",name);
        parameters.put("card",card);
        parameters.put("faren",faren);
        parameters.put("job",job);
        parameters.put("sex",sex);
        parameters.put("nation",nation);
        parameters.put("link",link);
        parameters.put("mobile",mobile);
        parameters.put("email",email);
        parameters.put("reg_province",reg_province);
        parameters.put("reg_city",reg_city);
        parameters.put("reg_area",reg_area);
        parameters.put("reg_address",reg_address);
        parameters.put("tel",tel);
        parameters.put("province",province);
        parameters.put("city",city);
        parameters.put("area",area);
        parameters.put("address",address);
        apiService.executeyuangao(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 被告删除
    public void postMapBeigaoDelete(int bei_id, BaseSubscriber<HttpResponse> subscriber) {
        String url = "Cases/delBei/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("bei_id", bei_id);
        apiService.excutePostHttpResponse(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 获取案件信息
    public void postMaCaseInfoDetails(int case_id,BaseSubscriber<HttpResponse<CaseInfoDetails>> subscriber) {
        String url = "Cases/getCase/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("case_id",case_id);
        apiService.executePostMapCaseInfoDetails(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //post map请求 获取
    public void postMapBeigaoDetails(int bei_id,BaseSubscriber<HttpResponse<CaseBeigaoInfo>> subscriber) {
        String url = "Cases/getBei/";
        Map<String, Object> parameters = new ArrayMap<>();
        parameters.put("bei_id",bei_id);
        apiService.executePostMapCaseBeigaoInfo(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
