package com.mikucode.chatapp

class User {
    var userName: String? = null
    var email: String? = null
    var uId: String? = null
    constructor(){}
    constructor(userName: String, email: String, uId: String){
        this.userName = userName
        this.email = email
        this.uId = uId
    }

}