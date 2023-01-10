package com.anonymouscommunication.anonymouschat.Model
import com.google.firebase.database.*
import java.util.*

class UnknownUser(id: Int, email: String, var knownUser: KnownUser,
                  override var blockedUsers: MutableList<User>,
                  override val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users/UnknownUsers").child(id.toString())
) : User(id, email) {
    // Firebase database references
    val chatsRef: DatabaseReference = userRef.child("chats")

    // member variables
    override var chats: MutableList<Chat> = mutableListOf()

    override fun saveUser(): Boolean {
        // code to save the user to the Firebase database
        userRef.push().setValue(this)
        return true
    }

    override fun blockUser(user: User): Boolean {
        // code to block the user and update the Firebase database
        blockedUsers.add(user)
        userRef.child("blocked_users").child(user.id.toString()).setValue(true)
        return true
    }

    fun startChat(): Chat {
        // code to start the chat and return the Chat object
        val chat = UnknownChat(id = UUID.randomUUID().toString(), user1 = this, knownUser)
        chats.add(chat)
        chatsRef.child(knownUser.id.toString()).setValue(chat)
        return chat
    }

    fun changeKnownUser(newKnownUser: KnownUser) {
        knownUser = newKnownUser
    }

    fun getUserChats(): List<Chat> {
        // code to retrieve the user's chats from the Firebase database
        val chats: MutableList<Chat> = mutableListOf()
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (chatSnapshot in dataSnapshot.children) {
                    val chat = chatSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chats.add(chat)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
        return chats
    }
}