package com.example.sphtech.model.service.returns

import com.example.sphtech.model.service.BaseBean
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 class StoreBean : BaseBean(){

     @SerializedName("result")
     @Expose
     var result: ResultBean? = null
}
