package com.trubuzz.cornerstone.api

import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.util.SPUtil
import com.trubuzz.cornerstone.util.SystemUtil
import com.trubuzz.cornerstone.util.download.ProgressInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitUtil {
    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {
        if (apiService == null) {
            synchronized(RetrofitUtil::class.java) {
                if (apiService == null) {
                    apiService = buildRetrofit(Constant.MAIN_URL).create(ApiService::class.java)
                }
            }
        }
        return apiService
    }

    private fun buildRetrofit(baseUrl:String) = retrofit2.Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(buildClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private fun buildClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()
            val headersOrigin = original.headers()

            val headers = headersOrigin.newBuilder()
                .set(Constant.LANG, "zh-CN")
                .set(Constant.API_VERSION, "1.0")
                .set(Constant.SESSIONID, SPUtil.session)
                .set(Constant.TOKEN, SPUtil.token)
                .set(Constant.SOURCE, Constant.APP_SOURCE)
                .set(Constant.TRACE_ID, getTraceId())
                .build()
            val requestBuilder = original.newBuilder()
                .headers(headers)
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(ProgressInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun getTraceId(): String {
        val androidId = SystemUtil.getAndroidId(App.instance())
        val timeinterval = (System.currentTimeMillis() / 1000).toString()
        var traceId = ""
        if (SPUtil.session == null || SPUtil.token == "") {
            traceId = androidId + "_" + timeinterval
        } else {
            traceId = androidId + "_" + SPUtil.session + "_" + timeinterval
        }
        return traceId
    }

    fun resetAll() {
        apiService = buildRetrofit(Constant.getDomain(App.instance())).create(ApiService::class.java)
    }

    fun <T> serverError(it: BaseResponse<T>): ServerError {
        return ServerError(it.code, it.errorCode, it.subCode, it.msg)
    }
}