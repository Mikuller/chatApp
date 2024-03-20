package com.mikucode.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var dbREF: DatabaseReference
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private var senderUid: String? = null
    private var senderRoom: String = ""
    private var receiverRoom: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val receiverName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uID")
        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        supportActionBar?.title = receiverName


        //init Everything
        chatRecyclerView = findViewById(R.id.conversationView)
        messageBox = findViewById(R.id.edtWriteTxt)
        sendBtn = findViewById(R.id.sendBtn)
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageList = ArrayList()
        dbREF = Firebase.database.reference



        messageAdapter = MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        addMessageToRecyclerView()
        //add Message into database

        sendBtn.setOnClickListener{
            addMessageToDataBase()
            messageBox.setText("")
            addMessageToRecyclerView()
        }


    }

    private fun addMessageToRecyclerView() {

        dbREF.child("Chats").child(senderRoom).child("message").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapshot in snapshot.children){
                    val currentMessage = postSnapshot.getValue(Message::class.java)
                    messageList.add(currentMessage!!)}
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(sendBtn,"Message not sent successfully",Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun addMessageToDataBase() {
      dbREF = Firebase.database.reference
        val message = messageBox.text.toString()
        val messageObject = Message(message,senderUid!!)
      dbREF.child("Chats").child(senderRoom).child("message").push()//push means ever time a new data is injected create a new node
          .setValue(messageObject).addOnSuccessListener {
              dbREF.child("Chats").child(receiverRoom).child("message").push()//of a value will always have a unique ID, if Id is not specified it's automatically added
                  .setValue(messageObject)
          }

    }
}