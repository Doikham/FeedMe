package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener



private lateinit var auth: FirebaseAuth
const val RC_SIGN_OUT = 234

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


// ...
// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto = acct.photoUrl

            nameTextView.text = personName.toString()
            emailTextView.text = personEmail.toString()
            imageView2.setImageURI(personPhoto)

        }


        signoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Signing Out", Toast.LENGTH_LONG).show()
            //sleep(200)
            val intentso = Intent(this, LoginActivity::class.java )
            startActivity(intentso)
            finish()
        }


    }




}
