package com.example.sphtech.activity

import android.content.Context
import android.view.View

import com.example.sphtech.R
import com.example.sphtech.model.bean.TotalMolResultBean
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.activity_result.*


import kotlinx.android.synthetic.main.activity_result.view.*





class ResultDetailActivity : BaseActivity() {


    companion object{
        val stringTag = "ResultDetailActivity"
    }
    var totalMolResultBean : TotalMolResultBean? = null

    override val uiContentId: Int
        get() = R.layout.activity_result

    override fun uiData() {
        totalMolResultBean  = getIntent().getSerializableExtra(stringTag) as TotalMolResultBean?
    }

    override fun uiFlow(context: Context) {


        var series = BarGraphSeries(
            setData().toTypedArray()
        )
        series.setSpacing(50);
        // set Max Min plot
        gvGaph.title = "Amount the of data during "+ totalMolResultBean?.year
        gvGaph.getGridLabelRenderer().setNumHorizontalLabels(4);
        gvGaph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        gvGaph.getGridLabelRenderer().horizontalAxisTitle = getString(R.string.qu_year)
        gvGaph.viewport.setMaxX(4.0)

        gvGaph.addSeries(series)
    }

    private fun setData() : ArrayList<DataPoint>{
        var dataPoints = ArrayList<DataPoint>()
        dataPoints.add( DataPoint(1.0, 0.0))
        dataPoints.add( DataPoint(2.0, 0.0))
        dataPoints.add( DataPoint(3.0, 0.0))
        dataPoints.add( DataPoint(4.0, 0.0))

        dataPoints.forEachIndexed{ index, dataPoint ->
            for ( item in totalMolResultBean?.itemListBeans!!){
                if(checkQuater(item.quarter) == dataPoint.x){
                    dataPoints.set(index,DataPoint(dataPoint.x,item.mobileVol))
                }
            }
        }


        return dataPoints
    }

    private fun checkQuater(quaterString : String?) : Double{

        when (quaterString?.split("-")?.get(1)) {
            "Q1" -> {
               return 1.0
            }
            "Q2" -> {
                return 2.0
            }
            "Q3" -> {
                return 3.0
            }
            "Q4" -> {
                return 4.0
            }
        }
        return 0.0;
    }


}