package com.anonymouscommunication.anonymouschat.Model

import com.google.firebase.database.*

class Admin(id: Int, email: String, override var blockedUsers: MutableList<User>,
            override val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users/AdminUser").child(id.toString())
) : User(id, email) {
    // member variables
    private var users: MutableList<User> = mutableListOf()
    override var chats: MutableList<Chat> = mutableListOf()

    // Firebase database references
    private val usersRef: DatabaseReference = userRef.child("users")
    private val blockedUsersRef: DatabaseReference = userRef.child("blocked_users")

    // member functions
    override fun saveUser(): Boolean {
        // code to save the user to the Firebase database
        userRef.push().setValue(this)
        return true
    }

    override fun blockUser(user: User): Boolean {
        // code to block the user and update the Firebase database
        blockedUsers.add(user)
        blockedUsersRef.child(user.id.toString()).setValue(true)
        return true
    }

    fun addUser(user: User): Boolean {
        // code to add a user and update the Firebase database
        users.add(user)
        usersRef.child(user.id.toString()).setValue(user)
        return true
    }

    fun removeUser(user: User): Boolean {
        // code to remove a user and update the Firebase database
        users.remove(user)
        usersRef.child(user.id.toString()).removeValue()
        return true
    }

    fun getAllUsers(): List<User> {
        // code to retrieve the list of users from the Firebase database
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
        return users
    }
}