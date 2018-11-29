package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.widget.Switch
import com.egci428.feedme.R.id.tbSetting
import com.egci428.feedme.R.id.toggle
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        tbSetting.setNavigationOnClickListener{
            finish()
        }
        tbSetting.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        editInfo.setOnClickListener {
            val intent = Intent(this,InfoActivity::class.java)
            startActivity(intent)
            finish()
        }

        clearFav.setOnClickListener{

        }

        val note: String = ("This app was inspired by food brochure and was created by two hungry students")
        devNote.text = note
    }

}
