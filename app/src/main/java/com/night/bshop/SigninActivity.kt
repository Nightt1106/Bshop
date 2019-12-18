package com.night.bshop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val auth = FirebaseAuth.getInstance()

        button_sigUp.setOnClickListener { view ->
            signUp()
        }

        button_SignIn.setOnClickListener { task ->
            signIn()
        }
    }

    private  fun signUp(){
        val sEmail = textEmail.text.toString()
        val spassword = textPassword.text.toString()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(sEmail,spassword)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    AlertDialog.Builder(this@SigninActivity)
                        .setTitle("Sign Up")
                        .setMessage("Account created")
                        .setPositiveButton("OK") { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SigninActivity)
                        .setTitle("Sign Up")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("OK",null)
                        .show()
                }
            }
    }

    private fun signIn(){
        val sEmail = textEmail.text.toString()
        val spassword = textPassword.text.toString()
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(sEmail,spassword)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    setResult(Activity.RESULT_OK)
                    finish()
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
