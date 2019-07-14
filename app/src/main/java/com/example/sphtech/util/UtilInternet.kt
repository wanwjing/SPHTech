package com.example.sphtech.util

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.core.content.ContextCompat.getSystemService
import android.content.IntentFilter







class UtilInternet : BroadcastReceiver() {
    var isRegistered = false


    override fun onReceive(context: Context, arg1: Intent) {

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(context))
        }
    }
    fun register(context: Context, filter: IntentFilter): Intent? {
        try {

            return if (!isRegistered)
                context.registerReceiver(this, filter)
            else
                null
        } finally {
            isRegistered = true
        }
    }


    fun unregister(context: Context): Boolean {
        // additional work match on context before unregister
        // eg store weak ref in register then compare in unregister
        // if match same instance
        return isRegistered && unregisterInternal(context)
    }

    private fun unregisterInternal(context: Context): Boolean {
        context.unregisterReceiver(this)
        isRegistered = false
        return true
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }


    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        fun isConnected(context: Context) : Boolean{
            return UtilInternet().isConnectedOrConnecting(context)
        }
    }
}
