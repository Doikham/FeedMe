package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hanks.passcodeview.PasscodeView
import kotlinx.android.synthetic.main.activity_passcode_view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kevalpatel2106.fingerprintdialog.AuthenticationCallback
import com.kevalpatel2106.fingerprintdialog.FingerprintDialogBuilder
import com.kevalpatel2106.fingerprintdialog.FingerprintUtils


class PasscodeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode_view)

        passcodeView
                .setLocalPasscode("5555")
                .setListener(passcodeListener)

        val dialogBuilder = FingerprintDialogBuilder(this)
                .setTitle("Authentication Required")
                .setSubtitle("We need to make sure it is really you")
                .setDescription("Place your finger on your fingerprint sensor")
                .setNegativeButton("Cancel")

        dialogBuilder.show(supportFragmentManager, callback)
    }

    private val passcodeListener = object : PasscodeView.PasscodeViewListener {
        var count: Int = 5
        override fun onSuccess(number: String) {
            updateUI()
        }

        override fun onFail() {
            if (count == 0) {

                FirebaseAuth.getInstance().signOut()
            } else {
                passcodeView.wrongInputTip = "  $count times remaining. App will automatically sign out after $count tries"
                count.dec()
            }
        }
    }

        private val callback = object : AuthenticationCallback {

            override fun fingerprintAuthenticationNotSupported() {
                // Device doesn't support fingerprint authentication. May be device doesn't have fingerprint hardware or device is running on Android below Marshmallow.
                // Switch to alternate authentication method.
                authenticationCanceledByUser()
            }

            override fun hasNoFingerprintEnrolled() {
                // User has no fingerprint enrolled.
                // Application should redirect the user to the lock screen settings.
                FingerprintUtils.openSecuritySettings(this@PasscodeViewActivity)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                // Unrecoverable error. Cannot use fingerprint scanner. Library will stop scanning for the fingerprint after this callback.
                // Switch to alternate authentication method.
               authenticationCanceledByUser()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                // Authentication process has some warning. such as "Sensor dirty, please clean it."
                // Handle it if you want. Library will continue scanning for the fingerprint after this callback.
            }

            override fun authenticationCanceledByUser() {
                // User canceled the authentication by tapping on the cancel button (which is at the bottom of the dialog).

            }

            override fun onAuthenticationSucceeded() {
                // Authentication success
                // Your user is now authenticated.
               updateUI()
            }

            override fun onAuthenticationFailed() {
                // Authentication failed.
                // Library will continue scanning the fingerprint after this callback.
            }

        }

    private fun updateUI(){

        val intent = Intent(this@PasscodeViewActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }


    }


