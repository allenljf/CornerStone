package com.trubuzz.cornerstone.fund.model.repo

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.BaseResponse
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.fund.model.bean.FundDetailData
import com.trubuzz.cornerstone.util.TestJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FundDetailModel {
    var fundDetailData: MutableLiveData<ApiResult<FundDetailData>> = MutableLiveData()

    fun getFundDetail(fundType: String, fundId: String): MutableLiveData<ApiResult<FundDetailData>> {
        val type = object : TypeToken<BaseResponse<FundDetailData>>() {

        }.type
        val testData: BaseResponse<FundDetailData> = Gson().fromJson(TestJson.getJson(App.instance(), "FundAI"), type)

        Observable.just(testData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.errorCode == 0) {
                    fundDetailData.value = ApiResult(it.data, null, null)
                } else {
                    fundDetailData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                fundDetailData.value = ApiResult(null, null, it)
            })

//        var observable:Observable<BaseResponse<FundDetailData>>? = null
//        if(fundType == "Fund"){
//            observable = RetrofitUtil.getApiService().getFundDetail(fundId)
//        }else{
//            observable = RetrofitUtil.getApiService().getAiFundDetail(fundId)
//        }
//
//        observable
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                if(it.errorCode == 0){
//                    fundDetailData.value = ApiResult(it.data, null, null)
//                }else{
//                    fundDetailData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
//                }
//            }, {
//                fundDetailData.value = ApiResult(null, null, it)
//            })

        return fundDetailData
    }


}