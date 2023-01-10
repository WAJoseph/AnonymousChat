package com.anonymouscommunication.anonymouschat.Model
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects.hash

class KnownUser(id: Int, email: String, val connections: MutableList<UnknownUser>,
                override var blockedUsers: MutableList<User>,
                override val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users/KnownUsers").child(encodeEmail(email))
) : User(id, email) {
    // member variables
    override var chats: MutableList<Chat> = mutableListOf()

    // Firebase database references
    private val connectionsRef: DatabaseReference = userRef.child("connections")
    private val chatsRef: DatabaseReference = userRef.child("chats")

    // member functions
    override fun saveUser(): Boolean {
        // code to save the user to the Firebase database
        userRef.push().setValue(this)
        return true
    }

    override fun blockUser(user: User): Boolean {
        // code to block the user
        // update the Firebase database to reflect the change
        blockedUsers.add(user)
        userRef.child("blocked_users").child(user.id.toString()).setValue(true)
        return true
    }

    fun addConnection(user: UnknownUser): Boolean {
        // code to add the connection
        connections.add(user)
        connectionsRef.child(user.id.toString()).setValue(true)
        return true
    }

    fun removeConnection(user: UnknownUser): Boolean {
        // code to remove the connection
        connections.remove(user)
        connectionsRef.child(user.id.toString()).removeValue()
        return true
    }

    fun startChat(user: KnownUser): KnownChat {
        // generate a unique chat ID based on the IDs of the two users
        val chatId: String = hash(this.id.toString() + user.id.toString()).toString()
        // create the KnownChat object
        val chat = KnownChat(chatId, this, user)
        // add the chat to the list of chats for both users
        chats.add(chat)
        user.chats.add(chat)
        // save the chat to the Firebase database
        chatsRef.child(chatId).setValue(chat)
        user.getChatsRef().child(chatId).setValue(chat)
        // return the KnownChat object
        return chat
    }

    fun getChatsRef(): DatabaseReference {
        return chatsRef
    }
}
fun encodeEmail(email: String) : String{
    return email.replace('.',',')
}