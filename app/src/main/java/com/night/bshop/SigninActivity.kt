package com.night.bshop

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    private val RC_GOOGLE_SIGN_IN: Int = 200
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = SigninActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        //google sign in object
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        google_sign_in.setOnClickListener{
            startActivityForResult(googleSignInClient.signInIntent,RC_GOOGLE_SIGN_IN)
        }

        button_sigUp.setOnClickListener { view ->
            signUp()
        }

        button_SignIn.setOnClickListener { task ->
            signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        // getResult 有時候會失敗給予ApiException class
        val account = task.getResult(ApiException::class.java)
        Log.d(TAG,"onActivityResult ${account?.id}")
        //物件傳到Firebase取得帳號並建立資料
        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Log.d(TAG,"onActivityResult ${task.exception?.message}")
                    Snackbar.make(main_sign_in,"Firebase authentication failed",Snackbar.LENGTH_LONG)
                }
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
