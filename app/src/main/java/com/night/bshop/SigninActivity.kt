package com.night.bshop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val auth = FirebaseAuth.getInstance()

        button_signin.setOnClickListener { view ->

            val sEmail = textEmail.text.toString()
            val spassword = password.text.toString()

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(sEmail,spassword)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        AlertDialog.Builder(this@SigninActivity)
                            .setTitle("Sign In")
                            .setMessage("Account created")
                            .setPositiveButton("OK") { dialog, which ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            }.show()
                    } else {
                        AlertDialog.Builder(this@SigninActivity)
                            .setTitle("Sign In")
                            .setMessage(task.exception?.message)
                            .setPositiveButton("OK",null)
                            .show()
                    }
                }
        }

    }
}
