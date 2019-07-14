package com.example.sphtech.activity

import android.content.Context
import com.example.sphtech.model.bean.ItemListBean

import com.example.sphtech.model.bean.TotalMolResultBean
import com.example.sphtech.model.service.api.ApiStoreRestClient
import com.example.sphtech.model.service.returns.StoreBean
import com.example.sphtech.util.RetrofitEventListener
import com.example.sphtech.util.UtilSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivityPter(context: Context) : MainActivityCon {

    //init
    private var context : Context = context
    private var retrofitEventListener  : RetrofitEventListener? = null

    // variable
    private var wsRItemListBeans: ArrayList<TotalMolResultBean> = ArrayList() // return bean
    var itemListBeans: ArrayList<TotalMolResultBean> = ArrayList()  // adp control bean

    var limit : String = "5" // default value

    private val dataCacheTag = "cache value sph"
    var isOnlineStatus = false


    override fun onFlowData() {
        if(isOnlineStatus){
            onOnlineData()
        }else{
            callCacheDataFromLocal()
        }
    }

    fun onOnlineData() {
        val data = HashMap<String, String>()
        data["resource_id"] = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
        data["limit"] = limit
        ApiStoreRestClient.instance.getList(data, retrofitEventListener!!)
    }


    fun setListBeans(storeBean : StoreBean){
        this.wsRItemListBeans = wsRItemListBeans
        this.itemListBeans.clear()
        val sortedList = storeBean.result?.records!!.groupBy(keySelector = { it.quarter?.split("-")?.get(0) })
        for (item in sortedList) {

            var itemListBean = ArrayList<ItemListBean>()

            for(data in item.value){
                itemListBean.add(
                    ItemListBean(
                        data.volume_of_mobile_data,
                        data.quarter,
                        data._id.toString()
                    )
                )
            }

            wsRItemListBeans.add(TotalMolResultBean(itemListBean,item.key))
            itemListBeans.addAll(wsRItemListBeans)

        }
        callCacheDataToLocal()
    }

    fun setWsListBeans(wsRItemListBeans: ArrayList<TotalMolResultBean>){
        this.wsRItemListBeans = wsRItemListBeans
    }

    fun getWsListBeans() : ArrayList<TotalMolResultBean>{
        return this.wsRItemListBeans
    }


    fun setRetrofitEventListener(retrofitEventListener: RetrofitEventListener){
     this.retrofitEventListener = retrofitEventListener
    }

    // put data to device
    fun callCacheDataToLocal(){
        UtilSharedPreferences.getInstance(context)?.saveData(dataCacheTag, Gson().toJson(wsRItemListBeans))
    }


    // get data from device
    fun callCacheDataFromLocal(){
        val cacheData = UtilSharedPreferences.getInstance(context)?.getData(dataCacheTag)
        val itemType = object : TypeToken<ArrayList<TotalMolResultBean>>() {}.type
        if(cacheData != ""){
            wsRItemListBeans = Gson().fromJson(cacheData, itemType)
            this.itemListBeans.clear()
            this.itemListBeans.addAll(wsRItemListBeans)
        }
    }


}