package com.xun.soda.sodaclean

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xun.soda.R
import kotlinx.android.synthetic.main.activity_soda_clean.*

class SodaCleanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_clean)

        mMvpActivityBtn.setOnClickListener {
            startTargetActivity(SodaCleanMvpActivity::class.java)
        }
    }

    private fun startTargetActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}