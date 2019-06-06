package com.trubuzz.cornerstone.util.net

interface NetStateChangeObserver {

    fun onNetDisconnected()

    fun onNetConnected(networkType: NetworkType)
}
