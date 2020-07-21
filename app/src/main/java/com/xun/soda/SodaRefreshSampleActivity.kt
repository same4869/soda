package com.xun.soda

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xun.sodaui.refresh.ISodaRefresh
import com.xun.sodaui.refresh.SodaTextOverView
import kotlinx.android.synthetic.main.activity_soda_refresh_sample.*

class SodaRefreshSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_refresh_sample)

        mSrl.setRefreshOverView(SodaTextOverView(applicationContext))
        mSrl.setRefreshListener(object : ISodaRefresh.SodaRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({ mSrl.refreshFinished() }, 1000)
            }

            override fun enableRefresh(): Boolean {
                return true
            }

        })
        mSrl.setDisableRefreshScroll(true)

        val layoutManager = LinearLayoutManager(this)
        mRv.layoutManager = layoutManager
        mRv.adapter = MyAdapter(
            arrayOf(
                "SodaRefresh",
                "SodaRefresh",
                "SodaRefresh",
                "SodaRefresh",
                "SodaRefresh",
                "SodaRefresh",
                "SodaRefresh"
            )
        )

    }

    class MyAdapter(private val datas: Array<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var textView: TextView = v.findViewById(R.id.tv_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            )
        }

        override fun getItemCount(): Int = datas.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = datas[position]
        }

    }
}