package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.egci428.feedme.R.id.toggle
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        editInfo.setOnClickListener {
            val intent = Intent(this,InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        /*val switch: Switch = findViewById(R.id.darkTheme)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){

            } else{

            }
        }*/
    }
}
