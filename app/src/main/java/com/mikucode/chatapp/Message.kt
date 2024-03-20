package com.mikucode.chatapp

class Message {
    var edtMessage : String? = null
    var senderID : String? = null
    var groupMessage: Boolean? = null
    constructor(){}
    constructor(txtMessage: String, senderID: String){
        this.edtMessage = txtMessage
        this.senderID = senderID
    }
    constructor(txtMessage: String, senderID: String, groupMessage: Boolean){
        this.edtMessage = txtMessage
        this.senderID = senderID
        this.groupMessage = groupMessage
    }
}