package com.trubuzz.cornerstone.util

import android.app.Application
import android.content.Context
import android.text.TextUtils

import java.util.HashMap

import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.identity.Registration

object CustomerUtils {

    fun initialization(application: Application, key: String, id: String) {
        Intercom.initialize(application, key, id)
    }

    fun register(identity: String) {
        Intercom.client().registerIdentifiedUser(Registration()
                .withUserId(identity))
    }

    fun reset(context: Context) {
        Intercom.client().reset()
    }

    fun logout(){
        Intercom.client().logout()
    }

    fun help(context: Context) {
        Intercom.client().displayConversationsList()
    }

    fun logEvent(event: String, params:HashMap<String, String>?) {
        var accountId = SPUtil.session
        
        if(params != null){
            params["accountId"] = accountId
            params["phone"] = SPUtil.phone
            params["email"] = SPUtil.email
            Intercom.client().logEvent(event, params)
        }else{
            var basicParams = hashMapOf<String, String>()
            basicParams["accountId"] = accountId
            basicParams["phone"] = SPUtil.phone
            basicParams["email"] = SPUtil.email
            Intercom.client().logEvent(event, basicParams)
        }
    }
}
