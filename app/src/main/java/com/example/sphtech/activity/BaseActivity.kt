package com.example.sphtech.activity

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_base.*
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.sphtech.R
import com.example.sphtech.util.UtilInternet


abstract class BaseActivity : AppCompatActivity() , UtilInternet.ConnectivityReceiverListener {

    // set uiFlow create Action here
    abstract fun uiFlow(context: Context)
    abstract fun uiData()
    // set XML Id here
    abstract val uiContentId : Int;

    var isOnlineStatus : Boolean = false
    var dialog : AlertDialog? = null
    private val internet = UtilInternet()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        internet.register(this,IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        isOnlineStatus = UtilInternet.isConnected(this)

        val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(uiContentId, null)
        view.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        llContent.addView(view)

        setupDialog()

        uiData()
        uiFlow(this);

    }

    public override fun onResume() {
        super.onResume()
        UtilInternet.connectivityReceiverListener = this
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(isConnected){
            isOnlineStatus = true
            tvOfflineText.visibility = View.GONE
        }else{
            tvOfflineText.visibility = View.VISIBLE
            isOnlineStatus = false
        }
    }

    override fun onDestroy() {
        internet.unregister(this)
        super.onDestroy()
    }

    private fun setupDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.loading)
        builder.setCancelable(false)
        dialog = builder.create()
    }


    protected fun showLoading(show: Boolean) {
        //setup Loading
        if (show)
            dialog?.show()
        else
            dialog?.dismiss()
    }


}
