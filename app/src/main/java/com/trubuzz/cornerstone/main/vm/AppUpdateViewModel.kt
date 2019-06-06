package com.trubuzz.cornerstone.main.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.main.model.bean.AppUpdate

class AppUpdateViewModel : ViewModel() {
    var model = AppUpdateViewModel()

    fun getAppUpdate(appVersion: String, device: String): MutableLiveData<ApiResult<AppUpdate>> {
        return model.getAppUpdate(appVersion, device)
    }
}