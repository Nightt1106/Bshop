package com.night.bshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(),FirebaseAuth.AuthStateListener{

    private val RC_SIGNIN = 100
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button_verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful)
                        Snackbar.make(it,"Verify email sent",Snackbar.LENGTH_LONG).show()
                }
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onAuthStateChanged(auth: FirebaseAuth) {
            val user = FirebaseAuth.getInstance().currentUser
            Log.d("TAG","onActivityResult : ${user?.uid}")
            if(user != null) {
                text_user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
                button_verify_email.visibility = if(user.isEmailVerified) View.GONE else View.VISIBLE
            } else {
                text_user_info.text = "Not login"
                button_verify_email.visibility = View.GONE
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signIn -> {
                val whiteList = listOf<String>("tw","cn","hk","au")
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.FacebookBuilder().build(),
                                AuthUI.IdpConfig.PhoneBuilder()
                                    .setWhitelistedCountries(whiteList)
                                    .setDefaultCountryIso("tw")
                                    .build()
                            )
                        )
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.shop)
                        .build(),
                        RC_SIGNIN)
                /*startActivityForResult(Intent(this,SigninActivity::class.java),RC_SIGNIN)*/
                true
            }
            R.id.action_signOut -> {
                FirebaseAuth.getInstance().signOut()
                Log.d("SignOut", "User is Sign out")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
