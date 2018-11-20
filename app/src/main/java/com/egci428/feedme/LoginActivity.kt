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
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


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
        signInBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //handleSignInResult(task)
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
        updateUI(currentUser)

//        if (currentUser != null) {
//            signInBtn.visibility = View.GONE
//            alrSignInBtn.visibility = View.VISIBLE
//
//        }


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

