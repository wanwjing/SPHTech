package com.example.sphtech.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sphtech.AdpListItem
import com.example.sphtech.OnItemClickListener
import com.example.sphtech.R
import com.example.sphtech.model.bean.ItemListBean
import com.example.sphtech.model.bean.TotalMolResultBean
import com.example.sphtech.model.service.returns.StoreBean
import com.example.sphtech.model.service.api.ApiStoreRestClient
import com.example.sphtech.util.RetrofitEventListener
import com.example.sphtech.util.UtilSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import retrofit2.Call



class MainActivity : BaseActivity() ,RetrofitEventListener {


    var presenter: MainActivityPter = MainActivityPter(this)

    override val uiContentId: Int
        get() = R.layout.activity_main

    override fun uiData() {
        presenter.isOnlineStatus = isOnlineStatus
        presenter.setRetrofitEventListener(this)
        presenter.onFlowData()
    }

    override fun uiFlow(context: Context) {

        // set recycler view adapter
        rv_content.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rv_content.adapter =
            AdpListItem(this, presenter.itemListBeans, object : OnItemClickListener {
                override fun onPress(num: Int, totalMolResultBean: TotalMolResultBean) {
                    var intent = Intent(context, ResultDetailActivity::class.java)
                    intent.putExtra(ResultDetailActivity.stringTag, totalMolResultBean);
                    startActivity(intent)

                }
            })

        //set search
        etSearch.setText(presenter.limit)
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                presenter.limit = s.toString();
            }
        })
        ivSearch.setOnClickListener({
            showLoading(true)
            presenter.onOnlineData()
        })
    }

    override fun onSuccess(call: Call<*>, response: Any) {
        if (response is StoreBean) {
            presenter.setListBeans(response)
            uiUpdate()
            showLoading(false)
        }
    }

    override fun onError(call: Call<*>, t: Throwable) {
        showLoading(false)
        Toast.makeText(getApplicationContext(), t.message, Toast.LENGTH_LONG).show();
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        super.onNetworkConnectionChanged(isConnected)
        uiUpdate()
    }

    private fun uiUpdate() {
        if (isOnlineStatus) {
            this.llSearch.visibility = View.VISIBLE
        } else {
            this.llSearch.visibility = View.GONE
        }
        this.rv_content.adapter?.notifyDataSetChanged();
    }
}

