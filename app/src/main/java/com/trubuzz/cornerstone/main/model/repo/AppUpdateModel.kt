package com.trubuzz.cornerstone.main.model.repo

import androidx.lifecycle.MutableLiveData
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.main.model.bean.AppUpdate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppUpdateModel {

    var appUpdateData: MutableLiveData<ApiResult<AppUpdate>> = MutableLiveData()

    fun getAppUpdate(appVersion:String, device:String): MutableLiveData<ApiResult<AppUpdate>> {
        RetrofitUtil.getApiService().getAppUpdate(appVersion, device)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if(it.errorCode == 0){
                    appUpdateData.value = ApiResult(it.data, null, null)
                }else{
                    appUpdateData.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                }
            }, {
                appUpdateData.value = ApiResult(null, null, it)
            })

        return appUpdateData
    }

}