package com.example.sphtech.model.service

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class BaseBean {
    @SerializedName("help")
    @Expose
    var help: String? = null
    @SerializedName("success")
    @Expose
    var success: String? = null


}
