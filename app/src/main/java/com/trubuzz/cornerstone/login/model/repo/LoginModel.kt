package com.trubuzz.cornerstone.login.model.repo

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.BaseResponse
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.util.TestJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginModel {

    var loginData: MutableLiveData<ApiResult<LoginData>> = MutableLiveData()

    fun loginOrRegister(body: Map<String, String>): MutableLiveData<ApiResult<LoginData>> {
//        RetrofitUtil.getApiService().loginOrRegister(body)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                if(it.errorCode == 0){
//                    loginData.value = ApiResult(it.data, null, null)
//                }else{
//                    loginData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
//                }
//            }, {
//                loginData.value = ApiResult(null, null, it)
//            })

        val type = object : TypeToken<BaseResponse<LoginData>>() {

        }.type
        val testData: BaseResponse<LoginData> = Gson().fromJson(TestJson.getJson(App.instance(), "Login"), type)

        Observable.just(testData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.errorCode == 0) {
                    loginData.value = ApiResult(it.data, null, null)
                } else {
                    loginData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                loginData.value = ApiResult(null, null, it)
            })

        return loginData
    }

    var authCodeData: MutableLiveData<ApiResult<EmptyData>> = MutableLiveData()

    fun sendAuthCode(body: Map<String, String>): MutableLiveData<ApiResult<EmptyData>> {
        RetrofitUtil.getApiService().sendAuthCode(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if(it.errorCode == 0){
                    authCodeData.value = ApiResult(it.data, null, null)
                }else{
                    authCodeData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                authCodeData.value = ApiResult(null, null, it)
            })

        return authCodeData
    }
}