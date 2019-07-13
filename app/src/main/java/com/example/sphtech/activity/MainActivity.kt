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



class MainActivity : BaseActivity() {

    // variable
    var wsRItemListBeans: ArrayList<TotalMolResultBean> = ArrayList() // return bean
    var itemListBeans: ArrayList<TotalMolResultBean> = ArrayList() // adp control bean
    var limit : String = "5" // default value



    override val uiContentId: Int
        get() = R.layout.activity_main


    override fun uiFlow(context: Context, viewUi: View) {

        getDataList()

        // set recycler view adapter
        viewUi.rv_content.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        viewUi.rv_content.adapter =
            AdpListItem(this, itemListBeans, object : OnItemClickListener {
                override fun onPress(num: Int, totalMolResultBean: TotalMolResultBean) {
                    var intent = Intent(context,ResultDetailActivity::class.java)
                    intent.putExtra(ResultDetailActivity.stringTag, totalMolResultBean);
                    startActivity(intent)

                }
            })

        //set search
        viewUi.etSearch.setText(limit)
        viewUi.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                limit = s.toString();
            }
        })
        viewUi.ivSearch.setOnClickListener(View.OnClickListener {
            getDataList()
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        super.onNetworkConnectionChanged(isConnected)
        getDataList()
    }

    // call ws
    private fun getDataList() {

        val data = HashMap<String, String>()
        data["resource_id"] = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
        data["limit"] = limit
      //  data["q"] = "jones"

        itemListBeans.clear()

        if(isOnlineStatus){
            showLoading(true)
            ApiStoreRestClient.instance.getList(data, object : RetrofitEventListener {
                override  fun onSuccess(call: Call<*>, response: Any) {
                    if (response is StoreBean) {
                        var storeBean = response as StoreBean
                        val sortedList = storeBean.result?.records!!.groupBy(keySelector = { it.quarter?.split("-")?.get(0) })
                        for (item in sortedList) {

                            var itemListBean = ArrayList<ItemListBean>()

                            for(data in item.value){
                                itemListBean.add(ItemListBean(
                                    data.volume_of_mobile_data,
                                    data.quarter,
                                    data._id.toString()
                                ))
                            }

                            wsRItemListBeans.add(TotalMolResultBean(itemListBean,item.key))
                            itemListBeans.addAll(wsRItemListBeans)
                            callCacheDataToLocal()
                            uiUpdate()
                        }
                        showLoading(false)

                    }
                }
                override fun onError(call: Call<*>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(getApplicationContext(),t.message,Toast.LENGTH_LONG).show();
                }
            })
        }else{
            callCacheDataFromLocal()
            itemListBeans.addAll(wsRItemListBeans)
            uiUpdate()
        }

    }


    private fun uiUpdate(){
        if(isOnlineStatus){
            this.llSearch.visibility = View.VISIBLE
        }else{
            this.llSearch.visibility = View.GONE
        }
        this.rv_content.adapter?.notifyDataSetChanged();
    }

    // put data to device
    fun callCacheDataToLocal(){
        UtilSharedPreferences.getInstance(this)?.saveData(dataCacheTag,Gson().toJson(wsRItemListBeans))
    }

    // get data from device
    fun callCacheDataFromLocal(){
        val cacheData = UtilSharedPreferences.getInstance(this)?.getData(dataCacheTag)
        val itemType = object : TypeToken<ArrayList<TotalMolResultBean>>() {}.type
        if(cacheData != ""){
            wsRItemListBeans = Gson().fromJson(cacheData, itemType)
        }
    }
}
