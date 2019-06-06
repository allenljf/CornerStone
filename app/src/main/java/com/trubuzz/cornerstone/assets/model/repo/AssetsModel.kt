package com.trubuzz.cornerstone.assets.model.repo

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.BaseResponse
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.assets.model.bean.AssetsData
import com.trubuzz.cornerstone.util.TestJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AssetsModel {
    var assetsData: MutableLiveData<ApiResult<AssetsData>> = MutableLiveData()

    fun getAssets(): MutableLiveData<ApiResult<AssetsData>> {
//        RetrofitUtil.getApiService().getAssets()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                if(it.errorCode == 0){
//                    assetsData.value = ApiResult(it.data, null, null)
//                }else{
//                    assetsData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
//                }
//            }, {
//                assetsData.value = ApiResult(null, null, it)
//        })

        val type = object : TypeToken<BaseResponse<AssetsData>>() {

        }.type
        val testData: BaseResponse<AssetsData> = Gson().fromJson(TestJson.getJson(App.instance(), "Assets"), type)

        Observable.just(testData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.errorCode == 0) {
                    assetsData.value = ApiResult(it.data, null, null)
                } else {
                    assetsData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                assetsData.value = ApiResult(null, null, it)
            })

        return assetsData
    }


}