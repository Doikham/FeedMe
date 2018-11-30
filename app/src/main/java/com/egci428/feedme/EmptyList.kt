package com.egci428.feedme

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.empty_list.*

class EmptyList: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_list)

        tbEmpty.setNavigationOnClickListener {
            finish()
        }
        tbEmpty.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)
    }
}