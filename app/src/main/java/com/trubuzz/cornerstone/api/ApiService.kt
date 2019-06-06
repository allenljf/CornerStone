package com.trubuzz.cornerstone.api

import com.trubuzz.cornerstone.assets.model.bean.AssetsData
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.fund.model.bean.FundDetailData
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.main.model.bean.AppUpdate
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    //登入
    @Headers("Content-Type:application/json", "version: 1.0")
    @POST("user/loginOrRegister")
    fun loginOrRegister(@Body body: Map<String, String>): Observable<BaseResponse<LoginData>>

    //發送驗證碼
    @Headers("Content-Type:application/json", "version: 1.0")
    @POST("user/sendAuthCode")
    fun sendAuthCode(@Body body: Map<String, String>): Observable<BaseResponse<EmptyData>>

    //註冊手機推送資訊
    @Headers("Content-Type:application/json", "version: 1.0")
    @POST("user/sendUserDeviceInfo")
    fun sendUserDeviceInfo(@Body body: Map<String, String>): Observable<BaseResponse<EmptyData>>

    //檢查是否強制更新
    @GET("biz/customizeModule.do?code=updateApp&params=appVersion,device")
    fun getAppUpdate(
        @Query("appVersion") appVersion: String,
        @Query("device") device: String
    ): Observable<BaseResponse<AppUpdate>>

    //基金首頁
    @GET("biz/customizeModule.do?code=jishiFundsDefault")
    fun getFund(): Observable<BaseResponse<FundData>>

    //一般基金詳情
    @GET("biz/customizeModule.do?code=jishiFundDetail&params=fundId")
    fun getFundDetail(@Query("fundId") fundId: String): Observable<BaseResponse<FundDetailData>>

    //AI基金詳情
    @GET("biz/customizeModule.do?code=jishiAiFundDetail&params=fundId")
    fun getAiFundDetail(@Query("fundId") fundId: String): Observable<BaseResponse<FundDetailData>>

    //基金首頁
    @GET("biz/customizeModule.do?code=jishiAssetsDefault")
    fun getAssets(): Observable<BaseResponse<AssetsData>>

    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>
}