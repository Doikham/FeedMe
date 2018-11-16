package com.egci428.feedme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    //private val mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mAuth = FirebaseAuth.getInstance()

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                //Log.w(FragmentActivity., "signInWithEmail:success")
                val user = mAuth.currentUser
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                //Log.w(FragmentActivity.TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }

            // ...
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.getCurrentUser()
        //updateUI(currentUser)
    }
}
