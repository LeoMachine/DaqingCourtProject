package com.boju.daqingcourt.retrofitclient;


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

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Ｔａｍｉｃ on 2016-07-08.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public interface ApiService {
    public final String HTTP = "http://";
    public final String HOST = "daqing.tdfy.net";
    public final String URL = "/api/";

    public static final String Base_URL = HTTP + HOST + URL;


    @POST
    @FormUrlEncoded
    Observable<HttpResponse> excutePostHttpResponse(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 首页文章列表
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<ArticleInfo>>> executeArticleInfoList(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 首页轮播图
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<BannerInfo>> executeIndexBanner(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 首页文章列表
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<IconGonggaoInfo>>> executeIconGonggaoInfoList(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 首页轮播图
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<ArticleInfo>> executeArticleInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 首页文章列表
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<SifaVideoInfo>>> executeIconSifaVideoInfoList(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap  短信验证码
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<String>> excutePostSendSmsCode(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 登录
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<UserInfo>> executePostMapLoginUser(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 我的案件列表
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<CaseInfo>>> executeCaseInfoList(
            @Url String url,
            @FieldMap Map<String, Object> maps);


    //POST请求  QueryMap 帮助中心一级列表
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<DocumentInfo>>> executeDocumentInfoList(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap  立案-案由类别2
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<AnyouTypeInfo>>> executePostAnyouTypeInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap  立案-案由类别
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<CaseAnyouTypeInfo>>> executePostCaseAnyouTypeInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);
    //POST请求  QueryMap  立案-证据类别
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<CaseZhengjuInfo>>> executePostCaseZhengjuInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap  原告-民族类别
    @POST
    Observable<HttpResponse<List<MinzuInfo>>> executePostMinzuInfo(
            @Url String url);

    //POST请求  QueryMap 原告
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<String>> executeyuangao(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap  登录
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<UserInfo>> executePostMapInformation(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求 图片上传接口
    @POST("api/uploads/")
    @Multipart
    Observable<HttpResponse<List<String>>> upLoadMultiImgsPartMapQuestion(
            @PartMap Map<String, RequestBody> params);

    //POST请求  QueryMap  立案-证据类别
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<List<CaseBeigaoInfo>>> executePostCaseBeigaoInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);


    //POST请求  QueryMap 原告
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<String>> executeCaseAdd(
            @Url String url,
            @FieldMap Map<String, Object> maps,
            @Field("images[]")  List<String> listimages,
            @Field("falv[]")  List<String> falv,
            @Field("beigao[]")  List<String> beigaoInfoList,
            @Query("susong[]")  List<SusongPostInfo> susongPostInfoList);

    //POST请求  QueryMap 原告
    @POST
    Observable<HttpResponse<String>> executeCaseAdd2(
            @Url String url,
            @Body ListSusongInfoPost listSusongInfoPost);

    //POST请求  QueryMap  立案信息
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<CaseInfoDetails>> executePostMapCaseInfoDetails(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    //POST请求  QueryMap 被告人信息
    @POST
    @FormUrlEncoded
    Observable<HttpResponse<CaseBeigaoInfo>> executePostMapCaseBeigaoInfo(
            @Url String url,
            @FieldMap Map<String, Object> maps);
}
