package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hanks.passcodeview.PasscodeView
import kotlinx.android.synthetic.main.activity_passcode_view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth



class PasscodeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode_view)

        passcodeView
                .setLocalPasscode("5555")
                .setListener(passcodeListener)
    }

    private val passcodeListener = object : PasscodeView.PasscodeViewListener{
        var count: Int = 5
       override fun onSuccess(number: String) {
           val intent = Intent(this@PasscodeViewActivity, TestActivity::class.java)
           startActivity(intent)
           finish()
        }

        override fun onFail() {
            if(count == 0)
            {

                FirebaseAuth.getInstance().signOut()
            }
            else
            {
                passcodeView.wrongInputTip = "  $count times remaining. App will automatically sign out after $count tries"
                count.dec()
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}

