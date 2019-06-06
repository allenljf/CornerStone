package com.trubuzz.cornerstone.login.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trubuzz.cornerstone.api.ApiResult
import com.trubuzz.cornerstone.api.EmptyData
import com.trubuzz.cornerstone.login.model.bean.LoginData
import com.trubuzz.cornerstone.login.model.repo.LoginModel

class LoginViewModel : ViewModel(){
    var model = LoginModel()

    fun sendAuthCode(body: Map<String, String>) : MutableLiveData<ApiResult<EmptyData>>{
        return model.sendAuthCode(body)
    }

    fun loginOrRegister(body: Map<String, String>) : MutableLiveData<ApiResult<LoginData>>{
        return model.loginOrRegister(body)
    }
}