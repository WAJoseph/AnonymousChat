package com.anonymouscommunication.anonymouschat.Model


import com.google.firebase.database.*
import java.io.Serializable

class UnknownChat(override val id: String, override val user1: UnknownUser, override val user2: KnownUser) : Chat(), Serializable {
    // Firebase database reference for messages
    override val messagesRef = FirebaseDatabase.getInstance().getReference("messages")
    // list to store the chat messages
    override val messages: MutableList<Message> = mutableListOf()
    // Firebase database reference for the chat
    override val chatRef: DatabaseReference = user1.chatsRef.child(user2.email)//users/UnknownUsers/email_1/chats/email_2

    override val users: MutableList<User> = mutableListOf(user1, user2)

    override val lastMessage: Message = messages[messages.size - 1]

    // function to send a message in the chat
    override fun sendMessage(sender: User, receiver: User, message: String) {
        // generate a unique key for the message
        val key = messagesRef.push().key
        // create a map with the message data
        val messageData =
            mapOf("sender" to sender.id, "receiver" to receiver.id, "message" to message)
        // save the message to the Firebase database
        messagesRef.child(key.toString()).setValue(messageData)
    }

    // function to retrieve the chat messages from the Firebase database
    override fun getChatMessages(): MutableList<Message> {
        // add a value event listener to the messages reference
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // loop through all the message snapshots
                for (messageSnapshot in dataSnapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    // check if the message is from the known user or the unknown user and add it to the list
                    if (message != null && ((message.sender == user2 && message.receiver == user1) || (message.sender == user1 && message.receiver == user2))) {
                        messages.add(message)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // print the error stack trace
                error.toException().printStackTrace()
            }
        })
        return messages
    }
}