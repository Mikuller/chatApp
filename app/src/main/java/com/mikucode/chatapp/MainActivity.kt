package com.mikucode.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.internal.NavigationMenuView
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var userRecyclerView: RecyclerView
    lateinit var userList: ArrayList<User>
    lateinit var userAdapter: User_Adapter
    lateinit var userDBRef: DatabaseReference
    val auth: FirebaseAuth = Firebase.auth
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDBRef = Firebase.database.reference
        userList = ArrayList()
        userAdapter = User_Adapter(this, userList)

        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = userAdapter
        fetchUserFromDB()

        bottomNav = findViewById(R.id.bottomMenu)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
            R.id.groups -> {
                val intent = Intent(this, GroupChat::class.java)
                startActivity(intent)
                finish()
            }
            }
            return@setOnItemSelectedListener true
        }


    }

    private fun fetchUserFromDB() {
          userDBRef.child("Users").addValueEventListener(object: ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                  userList.clear()
                  for(postSnapShot in snapshot.children){
                      val currentUser =  postSnapShot.getValue(User::class.java)
                      if(currentUser?.uId != auth.currentUser?.uid ){
                          userList.add(currentUser!!)
                      }
                  }
                  println(userList.toArray())
                  userAdapter.notifyDataSetChanged()
              }
              override fun onCancelled(error: DatabaseError) {
                  Toast.makeText(this@MainActivity, "No User is available for display", Toast.LENGTH_LONG).show()
              }

          })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logOut){
            Firebase.auth.signOut()
            //finish
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}