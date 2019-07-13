package com.example.sphtech.model.service.api

import com.example.sphtech.model.service.returns.StoreBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface ApiStoreBean{
    @GET("api/action/datastore_search?")
    fun getUserList(@QueryMap options: Map<String, String>): Call<StoreBean>

}
