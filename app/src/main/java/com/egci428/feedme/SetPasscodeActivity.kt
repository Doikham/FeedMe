package com.egci428.feedme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hanks.passcodeview.PasscodeView
import kotlinx.android.synthetic.main.activity_set_passcode.*

class SetPasscodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_passcode)

        //Set passcode
        passcodeView
                .setListener(passcodeListener)




    }

    private val passcodeListener = object : PasscodeView.PasscodeViewListener {

        override fun onSuccess(number: String) {
            val passcode = passcodeView.localPasscode

            //Use shared preference
            SharedPreference(this@SetPasscodeActivity).setPasswordEnabled(true)
            SharedPreference(this@SetPasscodeActivity).setPinValue(passcode)


            //Passcode(true,passcode)
            Toast.makeText(this@SetPasscodeActivity,"Passcode is set",Toast.LENGTH_SHORT).show()
            finish()
        }

        override fun onFail() {
            Toast.makeText(this@SetPasscodeActivity,"Failed, Returning Back",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
