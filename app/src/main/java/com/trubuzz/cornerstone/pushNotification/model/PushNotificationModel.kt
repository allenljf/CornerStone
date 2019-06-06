package com.trubuzz.cornerstone.pushNotification.model

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.login.model.bean.LoginData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

@Keep
data class PushNotificationModel(val msg: String) : Serializable {
    companion object {
        var data: MutableLiveData<ApiResult<EmptyData>> = MutableLiveData()

        fun sendUserDeviceInfo(body: Map<String, String>) : MutableLiveData<ApiResult<EmptyData>> {
            RetrofitUtil.getApiService().sendUserDeviceInfo(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.errorCode == 0){
                        data.value = ApiResult(it.data, null, null)
                    }else{
                        data.value = ApiResult(null, RetrofitUtil.serverError(it), null)
                    }
                }, {
                    data.value = ApiResult(null, null, it)
                })

            return data
        }
    }
}

@Keep
data class CommonNotice(
    val noticeType: String,
    val title: String,
    val body: String
)

@Keep
data class Payment(
    val noticeType: String,
    val title: String,
    val body: String,
    val status: String
)

@Keep
data class OpenWeb(
    val noticeType: String,
    val title: String,
    val body: String,
    val url: String
)