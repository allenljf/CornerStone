package com.trubuzz.cornerstone.fund.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.fund.model.bean.FundDetailData
import com.trubuzz.cornerstone.fund.model.repo.FundDetailModel
import com.trubuzz.cornerstone.fund.model.repo.FundModel
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.login.model.repo.LoginModel

class FundDetailViewModel : ViewModel(){
    var model = FundDetailModel()

    fun getFundDetail(fundType: String, fundId: String) : MutableLiveData<ApiResult<FundDetailData>>{
        return model.getFundDetail(fundType, fundId)
    }

}