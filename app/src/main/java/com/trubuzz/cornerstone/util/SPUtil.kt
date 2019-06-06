package com.trubuzz.cornerstone.util

import android.content.Context
import android.content.SharedPreferences
import com.trubuzz.cornerstone.Constant

object SPUtil {
    private const val NAME = Constant.APP_TAG
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    const val SP_FIRST_RUN = "SP_FIRST_RUN"
    const val SP_SESSION = "SP_SESSION"
    const val SP_TOKEN = "SP_TOKEN"
    const val SP_BUILD_TYPE = "SP_BUILD_TYPE"
    const val SP_DEVICE_UUID = "SP_DEVICE_UUID"
    const val SP_USER_NAME = "SP_USER_NAME"
    const val SP_USER_NICK = "SP_USER_NICK"
    const val SP_ACCOUNT = "SP_ACCOUNT"
    const val SP_PHONE = "SP_PHONE"
    const val SP_EMAIL = "SP_EMAIL"
    const val SP_AVATAR = "SP_AVATAR"
    const val SP_LOGIN_TYPE = "SP_LOGIN_TYPE"
    const val SP_AREA_CODE = "SP_AREA_CODE"
    const val SP_PRIVACY_MODE = "SP_PRIVACY_MODE"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        get() = preferences.getBoolean(SP_FIRST_RUN, false)
        set(value) = preferences.edit {
            it.putBoolean(SP_FIRST_RUN, value)
        }

    var session: String
        get() = preferences.getString(SP_SESSION,"")
        set(value) = preferences.edit {
            it.putString(SP_SESSION, value)
        }

    var token: String
        get() = preferences.getString(SP_TOKEN, "")
        set(value) = preferences.edit {
            it.putString(SP_TOKEN, value)
        }

    var buildType: String
        get() = preferences.getString(SP_BUILD_TYPE, Constant.APP_BUILD_DAILY)
        set(value) = preferences.edit {
            it.putString(SP_BUILD_TYPE, value)
        }

    var deviceUUID: String
        get() = preferences.getString(SP_DEVICE_UUID, "")
        set(value) = preferences.edit {
            it.putString(SP_DEVICE_UUID, value)
        }

    var account: String
        get() = preferences.getString(SP_ACCOUNT, "")
        set(value) = preferences.edit {
            it.putString(SP_ACCOUNT, value)
        }

    var phone: String
        get() = preferences.getString(SP_PHONE, "")
        set(value) = preferences.edit {
            it.putString(SP_PHONE, value)
        }

    var email: String
        get() = preferences.getString(SP_EMAIL, "")
        set(value) = preferences.edit {
            it.putString(SP_EMAIL, value)
        }

    var loginType: String
        get() = preferences.getString(SP_LOGIN_TYPE, Constant.LOGIN_TYPE_PHONE)
        set(value) = preferences.edit {
            it.putString(SP_LOGIN_TYPE, value)
        }

    var areaCode: String
        get() = preferences.getString(SP_AREA_CODE, "")
        set(value) = preferences.edit {
            it.putString(SP_AREA_CODE, value)
        }

    var privacyMode: Boolean
        get() = preferences.getBoolean(SP_PRIVACY_MODE, false)
        set(value) = preferences.edit {
            it.putBoolean(SP_PRIVACY_MODE, value)
        }
}
