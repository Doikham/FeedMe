package com.egci428.feedme

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

               auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val telephone = user.phoneNumber

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid

            editName.setText(name)
            editTel.setText(telephone)
            //profileImg.setImageURI(photoUrl)

            Picasso.with(this).load(photoUrl).into(profileImg)
        }

        saveBtn.setOnClickListener {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(editName.text.toString())
                    .build()

            user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("sup", "User profile updated.")
                        }
                    }
        }



    }
}
