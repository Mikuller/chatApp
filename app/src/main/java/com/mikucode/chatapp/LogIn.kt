package com.mikucode.chatapp

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.os.IResultReceiver.Default
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class LogIn : AppCompatActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassWord: EditText
    lateinit var loginBtn: Button
    lateinit var signUpBtn: Button
    private val mAuth = Firebase.auth
    lateinit var changeLang: TextView
    lateinit var changeLangEN: TextView

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()


        onStart()
        edtEmail = findViewById(R.id.txtEmail)
        edtPassWord = findViewById(R.id.txtPassword)
        loginBtn = findViewById(R.id.loginBtn)
        signUpBtn = findViewById(R.id.signUpBtn)

        signUpBtn.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent) }

        loginBtn.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassWord.text.toString()
            logInAuth(email,password) }


        changeLang = findViewById(R.id.changeLang)
      
        changeLangEN = findViewById(R.id.changeLangEn)

        changeLang.setOnClickListener {
            baseContext.setAppLocale("am")
            recreate()
        }
        changeLangEN.setOnClickListener {
           baseContext.setAppLocale("US")
            recreate()
        }


    }

    private fun Context.setAppLocale(language: String): Context {
        val locale: Locale
        if(language == "US"){
            locale = Locale.getDefault()
            Locale.setDefault(locale)
        }
        else{
            locale = Locale(language)
            Locale.setDefault(locale)
        }

        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)

    }







    private fun logInAuth(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                   Snackbar.make(loginBtn, "Please check your password or Email", Snackbar.LENGTH_LONG).show()
                }
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}