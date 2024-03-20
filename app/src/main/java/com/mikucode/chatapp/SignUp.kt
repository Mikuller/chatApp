package com.mikucode.chatapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var edtUserName: EditText
    private lateinit var edtPassWord: EditText
    private lateinit var edtEmail: EditText
    private lateinit var signUpBtn: Button
    private  lateinit var backBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var chatAppDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()


        edtUserName = findViewById(R.id.txtFullName)
        edtEmail =findViewById(R.id.txtEmail)
        edtPassWord = findViewById(R.id.txtPassword)
        signUpBtn = findViewById(R.id.signUpBtn)

        auth = Firebase.auth

        signUpBtn.setOnClickListener {
            val name = edtUserName.text.toString()
            val email = edtEmail.text.toString()
            val passWord = edtPassWord.text.toString()
            createUser(name, email, passWord)
        }


    }

    private fun createUser( name:String ,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  Toast.makeText(this, "sign up successful",Toast.LENGTH_SHORT).show()
                    addUserToDatabase(auth.currentUser?.uid!!,name, email)
                    finish()
                } else {

                    Toast.makeText(this, "sign up failed",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatabase(uId: String,name:String , email: String) {
        chatAppDB = Firebase.database.reference
        chatAppDB.child("Users").child(uId).setValue(User(name,email,uId))
    }

}