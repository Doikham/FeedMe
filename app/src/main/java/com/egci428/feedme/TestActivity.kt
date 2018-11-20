package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_test.*
import android.widget.Toast


private lateinit var auth: FirebaseAuth
const val RC_SIGN_OUT = 234

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


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
            //imageView2.set

        }

//        auth.addAuthStateListener {
//
//        }

        signoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(this, "Signing Out", Toast.LENGTH_LONG).show()
            val intentso = Intent(this, LoginActivity::class.java )
            startActivity(intentso)
            finish()

        }


    }



    public override fun onStart() {
        super.onStart()
       // val currentUser = FirebaseAuth.getInstance().addAuthStateListener(auth)
    }



}
