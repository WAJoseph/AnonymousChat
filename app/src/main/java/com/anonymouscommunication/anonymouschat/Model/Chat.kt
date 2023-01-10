package com.anonymouscommunication.anonymouschat.Model

import com.google.firebase.database.DatabaseReference

abstract class Chat() {
    abstract val id: String
    abstract val messagesRef: DatabaseReference
    abstract val messages: MutableList<Message>
    abstract val lastMessage: Message
    abstract val chatRef: DatabaseReference
    abstract val users: MutableList<User>
    abstract val user1: User
    abstract val user2: User

    open abstract fun sendMessage(sender: User, receiver: User, message: String)
    open abstract fun getChatMessages(): MutableList<Message>
}