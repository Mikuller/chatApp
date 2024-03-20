package com.mikucode.chatapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase


class MessageAdapter(val context: Context,val messageList: ArrayList<Message> ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED = 1
    val ITEM_SENT = 2
    val GROUPCHAT_ITEM = 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {//this is used to initialize(inflate)the created layout for recycler view

        return when (viewType) {
            2 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.sent_message,parent,false)
                SentMessageVH(view) }
            3 -> {
                val view = LayoutInflater.from(context).inflate( R.layout.group_rcvd_message, parent , false)
                groupChatReceivedMessageVH(view) }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.recieved_message,parent,false)
                ReceivedMessageVH(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {//this attach every elements of collection into the elements(row) of RV
        when (holder::class.java) {
            SentMessageVH::class.java -> { // .javaClass and ::class.java are the same

                val viewHolder = holder as SentMessageVH//  //you need a reference val for this, if not you will get "virtual class..." error

                holder.txtSentMsg.text = messageList[position].edtMessage



            }
            groupChatReceivedMessageVH::class.java -> {
                val viewHolder = holder as groupChatReceivedMessageVH

                val senderUID = messageList[position].senderID

                Firebase.database.reference.child("Users").child(senderUID!!).child("userName").get().addOnSuccessListener {

                    holder.txtGroupChatSender.text = it.value.toString()
                    holder.txtGroupChatRcvdMsg.text = messageList[position].edtMessage

                }


            }
            else -> {
                val viewHolder = holder as ReceivedMessageVH//TODO check if you need a reference val for this

                holder.txtRcvdMsg.text = messageList[position].edtMessage
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       val currentMsg = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid == currentMsg.senderID){
            ITEM_SENT
        }
        else if (currentMsg.groupMessage == true){
            GROUPCHAT_ITEM
        }
        else{
            ITEM_RECEIVED
        }
    }
    override fun getItemCount(): Int {
      return messageList.size
    }

    class SentMessageVH(itemView: View): RecyclerView.ViewHolder(itemView){//this is used to initialize views to be used in a row of recycler view
          val txtSentMsg: TextView = itemView.findViewById(R.id.txtSentMessage)
     }
    class ReceivedMessageVH(itemView: View): RecyclerView.ViewHolder(itemView){//this is used to initialize views to be used in a row of recycler view
         val txtRcvdMsg: TextView = itemView.findViewById(R.id.txtReceivedMessage)
    }
    class groupChatReceivedMessageVH(itemView: View): RecyclerView.ViewHolder(itemView){
          val txtGroupChatSender: TextView = itemView.findViewById(R.id.txtGroupChatSender)
          val txtGroupChatRcvdMsg: TextView = itemView.findViewById(R.id.txtReceivedMessageInGroupChat)
    }

}