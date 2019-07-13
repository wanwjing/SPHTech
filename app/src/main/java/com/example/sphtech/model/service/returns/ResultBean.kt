package com.example.sphtech.model.service.returns

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultBean {

    @SerializedName("resource_id")
    @Expose
    var resource_id : String? = ""

    @SerializedName("fields")
    @Expose
    var fields : List<FieldsBean>? = null


    @SerializedName("g")
    @Expose
    var q : String? = ""


    @SerializedName("records")
    @Expose
    var records : ArrayList<RecordsBean>? = null


    @SerializedName("_links")
    @Expose
    var _links : LinksBean? = null


    @SerializedName("limit")
    @Expose
    var limit : Int? = 0
}