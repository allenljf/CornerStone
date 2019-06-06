package com.trubuzz.cornerstone.fund.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.fund.model.repo.FundModel
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.login.model.repo.LoginModel

class FundViewModel : ViewModel(){
    var model = FundModel()

    fun getFund() : MutableLiveData<ApiResult<FundData>>{
        return model.getFund()
    }

}