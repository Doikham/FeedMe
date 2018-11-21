package com.egci428.feedme

import android.app.PendingIntent.getActivity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Intent
import android.nfc.Tag
import android.os.SystemClock.sleep
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DialogTitle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kevalpatel2106.fingerprintdialog.AuthenticationCallback
import com.kevalpatel2106.fingerprintdialog.FingerprintDialogBuilder
import com.lmntrx.android.library.livin.missme.ProgressDialog


const val RC_SIGN_IN = 123
private lateinit var auth: FirebaseAuth





class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


// ...
// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("519576378371-1g1kr79eapmgcn10nrvbn00goummucf4.apps.googleusercontent.com")
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)





        signInBtn.setSize(SignInButton.SIZE_STANDARD)
        signInBtn.setColorScheme(SignInButton.COLOR_AUTO)
        signInBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            mGoogleSignInClient.signOut()


        }




    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val progressDialog = ProgressDialog(this)
            // Set message
            progressDialog.setMessage("Signing In")

            // Set cancelable
            progressDialog.setCancelable(false)

            // Show dialog
            progressDialog.show()

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.d(Tag, "firebaseAuthWithGoogle:" + acct.id!!)


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        // Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser

                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        updateUI(null)

                    }

                    // ...
                }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //Need to change if condition to check if setting for fingerprint scanner is true
        if(currentUser != null) {
            val dialogBuilder = FingerprintDialogBuilder(this)
                    .setTitle("Authentication Required")
                    .setSubtitle("We need to make sure it is really you")
                    .setDescription("Do your thang ")
                    .setNegativeButton("Cancel")

            val callback = object : AuthenticationCallback {

                override fun fingerprintAuthenticationNotSupported() {
                    // Device doesn't support fingerprint authentication. May be device doesn't have fingerprint hardware or device is running on Android below Marshmallow.
                    // Switch to alternate authentication method.
                }

                override fun hasNoFingerprintEnrolled() {
                    // User has no fingerprint enrolled.
                    // Application should redirect the user to the lock screen settings.
                    // FingerprintUtils.openSecuritySettings(this@SecureActivity)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    // Unrecoverable error. Cannot use fingerprint scanner. Library will stop scanning for the fingerprint after this callback.
                    // Switch to alternate authentication method.
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
                    updateUI(currentUser)
                }

                override fun onAuthenticationFailed() {
                    // Authentication failed.
                    // Library will continue scanning the fingerprint after this callback.
                }
            }

            dialogBuilder.show(supportFragmentManager, callback)

        }



    }


    private fun updateUI(completedTask: FirebaseUser?) {
        try {

            if(completedTask != null) {
                val intent = Intent(this, TestActivity::class.java)
                startActivity(intent)
                finish()
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            failTextView.visibility = View.VISIBLE
        }


    }


}

