package com.anonymouscommunication.anonymouschat.Model

import com.google.firebase.database.*

class KnownChat(override val id: String, override val user1: KnownUser, override val user2: KnownUser) : Chat() {
    // Firebase database references
    override val messagesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("messages")
    override val messages: MutableList<Message> = mutableListOf()
    override val lastMessage: Message = messages[messages.size - 1]

    override val chatRef: DatabaseReference = user1.getChatsRef().child(user2.id.toString())//
    override val users: MutableList<User> = mutableListOf(user1, user2)

    override fun sendMessage(sender: User, receiver: User, message: String) {
        // code to send a message and update the Firebase database
        // generate a unique key for the message
        val key = messagesRef.push().key
        // create a map with the message data
        val messageData = mapOf("sender" to sender.id, "receiver" to receiver.id, "message" to message)
        // save the message to the Firebase database
        messagesRef.child(key.toString()).setValue(messageData)
    }

    override fun getChatMessages(): MutableList<Message> {
        // code to retrieve the chat messages from the Firebase database
        // retrieve the chat messages from the Firebase database
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (messageSnapshot in dataSnapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    // check if the message is part of this chat and add it to the messages list
                    if (message != null && ((message.sender == user1 && message.receiver == user2) || (message.sender == user2 && message.receiver == user1))) {
                        messages.add(message)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // log the error if the chat messages couldn't be retrieved
                error.toException().printStackTrace()
            }
        })
        // map the messages to a list of strings and return it
        return messages
    }
}
