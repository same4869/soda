package com.xun.soda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_recycler_view.*

class SodaRecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        mBaseAdapter.setOnClickListener {
            startTargetActivity(SodaRecyclerBaseAdapterActivity::class.java)
        }

        mSimpleAdapter.setOnClickListener {
            startTargetActivity(SodaRecyclerSimpleAdapterActivity::class.java)
        }

        mDiyAdapter.setOnClickListener {
            startTargetActivity(SodaRecyclerDiyAdapterActivity::class.java)
        }
    }

    private fun startTargetActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}