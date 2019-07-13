package com.example.sphtech.model.bean

import java.io.Serializable

class ItemListBean : Serializable {

    var mobileVol : Double = 0.0
    var quarter : String ? = null
    var _id : String ? = null


    constructor(mobileVol: Double, quarter: String?, _id: String?) {
        this.mobileVol = mobileVol
        this.quarter = quarter
        this._id = _id
    }




}