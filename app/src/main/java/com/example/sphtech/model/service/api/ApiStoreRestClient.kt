package com.example.sphtech.model.service.api

import com.example.sphtech.model.service.returns.StoreBean
import com.example.sphtech.util.RetrofitEventListener
import com.example.sphtech.util.UtilRetrofitWs

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiStoreRestClient {

    private var apiStoreBean: ApiStoreBean? = null


    companion object {
        val instance = ApiStoreRestClient()
    }

    fun getList(data :HashMap<String, String> ,retrofitEventListener: RetrofitEventListener) {

        val retrofit = UtilRetrofitWs.retrofitClient
        apiStoreBean = retrofit.create(ApiStoreBean::class.java )

        val apiUserCall = apiStoreBean!!.getUserList(data)

        apiUserCall.enqueue(object : Callback<StoreBean> {
            // Success
            override fun onResponse(call: Call<StoreBean>, response: Response<StoreBean>) {
                if (response?.body() != null) {
                    retrofitEventListener.onSuccess(call, response?.body() as StoreBean)
                }
            }
            // Failure
            override fun onFailure(call: Call<StoreBean>, t: Throwable) {
                retrofitEventListener.onError(call, t)
            }
        })

    }
}