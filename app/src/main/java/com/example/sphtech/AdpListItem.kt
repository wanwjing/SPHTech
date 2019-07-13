package com.example.sphtech;

import android.app.PendingIntent.getActivity
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sphtech.model.bean.ItemListBean
import com.example.sphtech.model.bean.TotalMolResultBean
import kotlinx.android.synthetic.main.list_item.view.*



class AdpListItem(val context: Context, val items : ArrayList<TotalMolResultBean>, val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.tv_year?.text = items.get(position).year
        holder?.tv_tl_mobile_data?.text = items.get(position).getTotalString()

        when (items.get(position).trendStatus()) {
            TotalMolResultBean.ETrend.DOWN -> {
                holder?.iv_Trend.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_down_trend))
            }
            TotalMolResultBean.ETrend.UP -> {
                holder?.iv_Trend.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_up_trend))
            }
            TotalMolResultBean.ETrend.BAL -> {
                holder?.iv_Trend.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_trend))
            }
        }


        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onPress(position,items.get(position))
        })
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tv_year = view.tv_year
    val tv_tl_mobile_data = view.tv_tl_mobile_data
    val iv_Trend = view.iv_Trend

}

interface OnItemClickListener{
    fun onPress( no : Int, item : TotalMolResultBean)
}
