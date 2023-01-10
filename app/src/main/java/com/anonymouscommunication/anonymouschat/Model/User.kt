package com.anonymouscommunication.anonymouschat.Model
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class User(val id: Int, val email: String) {
    // member variables
    abstract protected var blockedUsers: MutableList<User>
    abstract var chats: MutableList<Chat>

    // Firebase database reference
    abstract protected val userRef: DatabaseReference

    // member functions
    abstract fun saveUser(): Boolean

    abstract fun blockUser(user: User): Boolean
}