package com.example.sphtech.activity


import androidx.test.rule.ActivityTestRule

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import org.robolectric.RuntimeEnvironment
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat
import com.example.sphtech.R
import com.example.sphtech.model.bean.ItemListBean
import com.example.sphtech.model.bean.TotalMolResultBean
import com.example.sphtech.util.RetrofitEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.robolectric.shadows.ShadowNetworkInfo
import org.robolectric.Shadows
import org.junit.*
import org.robolectric.Robolectric
import retrofit2.Call


@RunWith(RobolectricTestRunner::class)

class MainActivityTest{



    @Rule
    @JvmField
    var rule : ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    val activity = Robolectric.setupActivity(MainActivity::class.java)
    var presenter : MainActivityPter? = null


    @Before
    fun setUp() {
        presenter = MainActivityPter(activity.applicationContext)
    }

    @Test
    fun testUIAvailable() {
        val activity = Robolectric.setupActivity(MainActivity::class.java)
        assertNotNull(activity)
        assertNotNull(activity.llContent.isShown)
        assertNotNull(activity.rv_content.isShown)
        assertNotNull(activity.rv_content.adapter)
    }

    @Test
    fun testRecyclerView(){
        val activity = Robolectric.setupActivity(MainActivity::class.java)
        // mock data
        var itemListBean = ArrayList<ItemListBean>();
        itemListBean.add(ItemListBean(0.111,"2004-Q1","1"))
        itemListBean.add(ItemListBean(0.123,"2004-Q2","1"))
        itemListBean.add(ItemListBean(0.124,"2004-Q3","1"))
        itemListBean.add(ItemListBean(0.125,"2004-Q4","1"))

        itemListBean.add(ItemListBean(0.111,"2005-Q1","1"))
        itemListBean.add(ItemListBean(0.123,"2002-Q2","1"))


        activity?.presenter?.itemListBeans?.add(TotalMolResultBean(itemListBean,"2004"))
        activity?.presenter?.itemListBeans?.add(TotalMolResultBean(itemListBean,"2005"))

        activity.rv_content.adapter?.notifyDataSetChanged();

        // check adpter item
        assertTrue(activity?.presenter?.itemListBeans?.size == activity.rv_content.adapter?.itemCount)
        assertTrue( 2 == activity.rv_content.adapter?.itemCount)

    }

    @Test
    fun testRVCardDisplay(){

        // mock data
        // item increase
        var items1 = ArrayList<ItemListBean>();
        items1.add(ItemListBean(0.111,"2004-Q1","1"))
        items1.add(ItemListBean(0.223,"2004-Q2","1"))
        items1.add(ItemListBean(0.324,"2004-Q3","1"))
        items1.add(ItemListBean(0.425,"2004-Q4","1"))
        // item decrease
        var items2 = ArrayList<ItemListBean>();
        items2.add(ItemListBean(0.311,"2005-Q1","1"))
        items2.add(ItemListBean(0.223,"2005-Q2","1"))
        items2.add(ItemListBean(0.124,"2005-Q3","1"))
        items2.add(ItemListBean(0.111,"2005-Q4","1"))
        // item bal
        var items3 = ArrayList<ItemListBean>();
        items3.add(ItemListBean(0.111,"2006-Q1","1"))
        items3.add(ItemListBean(0.111,"2006-Q2","1"))
        items3.add(ItemListBean(0.111,"2006-Q3","1"))
        items3.add(ItemListBean(0.111,"2006-Q4","1"))

        activity?.presenter?.itemListBeans?.clear()
        activity?.presenter?.itemListBeans?.add(TotalMolResultBean(items1,"2004"))
        activity?.presenter?.itemListBeans?.add(TotalMolResultBean(items2,"2005"))
        activity?.presenter?.itemListBeans?.add(TotalMolResultBean(items3,"2006"))


        activity?.rv_content?.adapter?.notifyDataSetChanged();

        // check card status
        var img1 = (activity?.rv_content?.findViewHolderForLayoutPosition(0)?.itemView?.iv_Trend?.drawable as BitmapDrawable).bitmap
        var img2 =  (activity?.rv_content?.findViewHolderForLayoutPosition(1)?.itemView?.iv_Trend?.drawable as BitmapDrawable).bitmap
        var img3 =  (activity?.rv_content?.findViewHolderForLayoutPosition(2)?.itemView?.iv_Trend?.drawable as BitmapDrawable).bitmap

        var imgDown = (ContextCompat.getDrawable(activity.applicationContext, R.mipmap.ic_down_trend) as BitmapDrawable).bitmap
        var imgUp = (ContextCompat.getDrawable(activity.applicationContext, R.mipmap.ic_up_trend) as BitmapDrawable).bitmap
        var imgBal =(ContextCompat.getDrawable(activity.applicationContext, R.mipmap.ic_trend) as BitmapDrawable).bitmap


        assertEquals(img1,imgUp)
        assertEquals(img2,imgDown)
        assertEquals(img3,imgBal)

    }

    @Test
    fun tesEditSearch() {
        val valueLimit = "12"
        val activity = Robolectric.setupActivity(MainActivity::class.java)
        activity.etSearch.setText(valueLimit)

        assertEquals(activity?.presenter?.limit,rule?.activity?.etSearch?.text.toString())
    }

    @Test
    fun testCache() {

        // mock data
        var totalMolResultBeans = ArrayList<TotalMolResultBean>();
        var itemListBean = ArrayList<ItemListBean>();
        itemListBean.add(ItemListBean(0.111,"2004-Q1","1"))
        itemListBean.add(ItemListBean(0.123,"2004-Q2","1"))
        itemListBean.add(ItemListBean(0.124,"2004-Q3","1"))
        itemListBean.add(ItemListBean(0.125,"2004-Q4","1"))

        var itemListBean1 = ArrayList<ItemListBean>();
        itemListBean1.add(ItemListBean(0.111,"2005-Q1","1"))
        itemListBean1.add(ItemListBean(0.123,"2002-Q2","1"))

        totalMolResultBeans.add(TotalMolResultBean(itemListBean,"2004"))
        totalMolResultBeans.add(TotalMolResultBean(itemListBean1,"2005"))
        presenter?.setWsListBeans(totalMolResultBeans)
        presenter?.isOnlineStatus = false
        // cache to device
        presenter?.callCacheDataToLocal()
        // clear bean data
        presenter?.setWsListBeans(ArrayList())
        // get from device
        presenter?.callCacheDataFromLocal()

        // same size means get back data
        assertEquals(totalMolResultBeans.size,presenter?.getWsListBeans()?.size)

    }

    @Test
    fun handleBroadcastReceiverConnected() {
        val connectivityManager = getConnectivityManager()
        val shadowConnManager = Shadows.shadowOf(connectivityManager)

        //Create shadow Network Info - Connected, WIFI, No Subtype, available, connected
        val netInfoShadow = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_WIFI, 0, true, true
        )
        shadowConnManager.setNetworkInfo(ConnectivityManager.TYPE_WIFI, netInfoShadow)

        //Trigger BroadcastReceiver
        RuntimeEnvironment.application.sendBroadcast(Intent("android.net.wifi.STATE_CHANGE"))

        val activeInfo = connectivityManager.getActiveNetworkInfo()

        assertTrue(activeInfo != null && activeInfo!!.isConnected())

    }

    fun getConnectivityManager(): ConnectivityManager {
        return RuntimeEnvironment.application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @After
    fun tearDown() {

    }


}