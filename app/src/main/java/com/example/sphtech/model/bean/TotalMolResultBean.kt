package com.example.sphtech.model.bean

import java.io.Serializable

class TotalMolResultBean : Serializable {
    enum class ETrend{
        UP,DOWN,BAL
    }

    var  itemListBeans : ArrayList<ItemListBean>? = null;
    var year :String? = ""


    constructor(itemListBean: ArrayList<ItemListBean>, year: String?) {
        this.itemListBeans = itemListBean
        this.year = year
    }

    fun getTotal() : Double{
        var total : Double = 0.0

        for(item in this.itemListBeans!!){
            total += item.mobileVol!!
        }

        return total
    }

    fun getTotalString() : String{
        return String.format("%.10f",getTotal())
    }

    fun trendStatus() : ETrend{
        var total : Double = 0.0

        for(item in this.itemListBeans!!){
            total -= item.mobileVol!!
        }
        if(total < 0){
            return ETrend.UP
        }else if(total > 0){
            return ETrend.DOWN
        }

        return ETrend.BAL
    }


}