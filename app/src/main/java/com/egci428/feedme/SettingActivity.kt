package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        tbSetting.setNavigationOnClickListener{
            finish()
        }
        tbSetting.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)


        setpasscodeBtn.isClickable = false

        val isSet = SharedPreference(this).getPasswordEnabled()

        if(isSet == true)
        {
            passcodeswitch.isChecked = true
            setpasscodeBtn.visibility = View.VISIBLE
            setpasscodeBtn.isClickable = true
        }

        passcodeswitch.setOnClickListener {
            if(passcodeswitch.isChecked == true){
                setpasscodeBtn.visibility = View.VISIBLE
                setpasscodeBtn.isClickable = true
                val intent = Intent(this,SetPasscodeActivity::class.java)
                startActivity(intent)
            }
            else if(passcodeswitch.isChecked == false)
            {
                setpasscodeBtn.visibility = View.INVISIBLE
                setpasscodeBtn.isClickable = false

                SharedPreference(this).setPasswordEnabled(false)
                SharedPreference(this).setPinValue("")
            }
        }

        setpasscodeBtn.setOnClickListener {
            val intent = Intent(this,SetPasscodeActivity::class.java)
            startActivity(intent)
            //finish()
        }

        clearFav.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            var database = FirebaseDatabase.getInstance()
            var myRef = database.getReference()

            myRef.child(user!!.uid).setValue(null)

        }

        val note: String = ("This app was inspired by food brochure and was created by two hungry students")
        devNote.text = note
    }

}
