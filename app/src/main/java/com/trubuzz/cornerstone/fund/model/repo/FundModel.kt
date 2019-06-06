package com.trubuzz.cornerstone.fund.model.repo

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.BaseResponse
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.util.TestJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FundModel {
    var fundData: MutableLiveData<ApiResult<FundData>> = MutableLiveData()

    fun getFund(): MutableLiveData<ApiResult<FundData>> {
//        RetrofitUtil.getApiService().getFund()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                if(it.errorCode == 0){
//                    fundData.value = ApiResult(it.data, null, null)
//                }else{
//                    fundData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
//                }
//            }, {
//                fundData.value = ApiResult(null, null, it)
//            })

        val type = object : TypeToken<BaseResponse<FundData>>() {

        }.type
        val testData: BaseResponse<FundData> = Gson().fromJson(TestJson.getJson(App.instance(), "FundList"), type)

        Observable.just(testData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.errorCode == 0) {
                    fundData.value = ApiResult(it.data, null, null)
                } else {
                    fundData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                fundData.value = ApiResult(null, null, it)
            })

        return fundData
    }


}