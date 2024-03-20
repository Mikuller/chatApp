package com.mikucode.chatapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class GroupChat : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var groupChatRecyclerView: RecyclerView
    private lateinit var groupChatAdapter: MessageAdapter
    private var messageList: ArrayList<Message> = ArrayList()
    private lateinit var msgBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var gcDBRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var senderUID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val view: View = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null )
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1976D2")))
        supportActionBar?.customView = view
        manageBottomNav()

        msgBox = findViewById(R.id.msgBox)
        sendBtn = findViewById(R.id.sendBtn)
        groupChatRecyclerView = findViewById(R.id.groups_chatlist)

        gcDBRef = Firebase.database.reference
        mAuth = FirebaseAuth.getInstance()
        groupChatAdapter = MessageAdapter(this, messageList )
        groupChatRecyclerView.layoutManager = LinearLayoutManager(this)
        groupChatRecyclerView.adapter = groupChatAdapter

        senderUID = mAuth.currentUser?.uid.toString()

        addMessageToRcyView()
        sendBtn.setOnClickListener {
            addMessageToDB( )
            msgBox.setText("")
            addMessageToRcyView()
        }

    }

    private fun addMessageToRcyView() {

         gcDBRef.child("Group_Chat").child("Message").addValueEventListener(object : ValueEventListener{//when a new ID(value) is added into the "Message" node
             override fun onDataChange(snapshot: DataSnapshot) {
                     messageList.clear()
                      for( postSnapshots in snapshot.children){
                          val currentMsg = postSnapshots.getValue(Message::class.java)
                          messageList.add(currentMsg!!)
                      }
                groupChatAdapter.notifyDataSetChanged()
             }

             override fun onCancelled(error: DatabaseError) {
                 Toast.makeText(this@GroupChat,"Message is not Sent!!!",Toast.LENGTH_SHORT).show()
             }

         })
    }

    private fun addMessageToDB() {
        val txtMessage = msgBox.text.toString()

        val messageObj = Message(txtMessage,senderUID,true)

        gcDBRef.child("Group_Chat").child("Message").push()
            .setValue(messageObj).addOnSuccessListener {Toast.makeText(this,"Message is Sent",Toast.LENGTH_SHORT).show()}
    }

    private fun manageBottomNav() {
        bottomNav = findViewById(R.id.bottomMenu)
        bottomNav.menu.getItem(1).isChecked = true
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.contacts -> {
                    val intent = Intent(this,  MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}