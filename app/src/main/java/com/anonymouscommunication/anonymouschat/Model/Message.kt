package com.anonymouscommunication.anonymouschat.Model

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class Message(val sender: User, val receiver: User, val message: String) {
    val id: String = UUID.randomUUID().toString() // generate a unique id for the message

    fun returnMessage(): String {
        return message
    }

    fun saveMessage(): Boolean {
        var success: Boolean = false
        val messageRef = FirebaseDatabase.getInstance().getReference("messages")
        val key = messageRef.push().key // generate a unique key
        val messageData = mapOf("sender" to sender.id, "receiver" to receiver.id, "message" to message)
        messageRef.child(key.toString()).setValue(messageData)
            .addOnSuccessListener {
                // message saved successfully
                success = true
            }
            .addOnFailureListener {
                // error occurred while saving the message
                it.message?.let { it1 -> Log.e("FirebaseError", it1) }
                success = false
            }
        return success
    }
    }
