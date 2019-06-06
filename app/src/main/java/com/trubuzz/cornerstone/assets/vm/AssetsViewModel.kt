package com.trubuzz.cornerstone.assets.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.assets.model.bean.AssetsData
import com.trubuzz.cornerstone.assets.model.repo.AssetsModel

class AssetsViewModel : ViewModel() {
    var model = AssetsModel()

    fun getAssets(): MutableLiveData<ApiResult<AssetsData>> {
        return model.getAssets()
    }

}