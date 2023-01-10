package com.anonymouscommunication.anonymouschat.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.anonymouscommunication.anonymouschat.Model.Chat
import com.anonymouscommunication.anonymouschat.Model.KnownUser
import com.anonymouscommunication.anonymouschat.R
import com.anonymouscommunication.anonymouschat.R.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(layout.login_fragment, container, false)

        // Create FireBase authentication object
        val auth = FirebaseAuth.getInstance()

        //create the 4 KnownUsers and 1 Admin
        val knownUser1 = KnownUser(1, "firedelta91@gmail.com", mutableListOf(), mutableListOf() )
        val knownUser2 = KnownUser(2, "manoelyazbeck@hotmail.com", mutableListOf(), mutableListOf())


        // create a reference to the "KnownUsers" nodes in the database
        val userRef1 = FirebaseDatabase.getInstance().getReference("users/KnownUsers").orderByChild(knownUser1.email).equalTo("firedelta91@gmail.com")
        val userRef2 = FirebaseDatabase.getInstance().getReference("users/KnownUsers").orderByChild(knownUser2.email).equalTo("manoelyazbeck@hotmail.com")
        knownUser1.saveUser()
        knownUser2.saveUser()


//        //save these users in the Firebase DB after checking if they already exist in the DB
//        userRef1.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(!snapshot.exists()){
//                    knownUser1.saveUser()
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//            }
//        })
//
//        userRef2.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(!snapshot.exists()){
//                    knownUser2.saveUser()
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//            }
//        })

        knownUser1.startChat(knownUser2).sendMessage(knownUser1,knownUser2,"Hi manoel")


        // Set up an OnClickListener for the login button
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            // Launch the Firebase Auth UI to allow the user to log in
            val loginEmail: EditText = view.findViewById(R.id.login_email)
            val loginPassword: EditText = view.findViewById(R.id.login_password)

            if(loginEmail.text.isNotEmpty() && loginPassword.text.isNotEmpty()){

                auth.signInWithEmailAndPassword(loginEmail.text.toString(),loginPassword.text.toString())
                    .addOnCompleteListener{

                        val loginEmailText: String = loginEmail.text.toString()

                        if(it.isSuccessful) {
                            Toast.makeText(activity, "Login succeeded $loginEmailText", Toast.LENGTH_SHORT).show()
                            //val userEmail = FirebaseAuth.getInstance().currentUser?.email
                            val encodedLoginEmail: String = encodeEmail(loginEmailText)
                            val chatRef = FirebaseDatabase.getInstance().getReference("users/KnownUsers").orderByChild("email").equalTo(encodedLoginEmail)



                            val chats = mutableListOf<Chat>()
                            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // dataSnapshot contains the chat data for the user
                                    // You can iterate through the children of dataSnapshot and retrieve the chat data
                                    for (userSnapshot in dataSnapshot.children) {
                                        val user = userSnapshot.getValue(KnownUser::class.java)
                                        if (user != null) {
                                            for (chatSnapshot in userSnapshot.child("chats").children) {
                                                val chat = chatSnapshot.getValue(Chat::class.java)
                                                if (chat != null) {
                                                    chats.add(chat)
                                                }
                                            }
                                        }
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }


                            })
                            // Create a bundle object to pass data to the ChatListFragment
                            val bundle = bundleOf("UserEmail" to loginEmailText)
                            // Navigate to the ChatListFragment and pass the bundle object as arguments
                            val navController = view.findNavController()
                            navController.navigate(R.id.action_login_fragment_to_chat_list_fragment,bundle)

                        }else{
                            Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(activity, "Empty",Toast.LENGTH_SHORT).show()
            }
            // Launch the login flow using the `signInIntent` variable and passing in the sign in intent created using the available providers

        }

        val registrationButton: Button = view.findViewById(R.id.registration_button)
        registrationButton.setOnClickListener {

            val loginEmail: EditText = view.findViewById(R.id.login_email)
            val loginPassword: EditText = view.findViewById(R.id.login_password)

            if(loginEmail.text.isNotEmpty() && loginPassword.text.isNotEmpty()){

                auth.createUserWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
                    .addOnCompleteListener{
                        if(it.isSuccessful) {
                            Toast.makeText(
                                activity,
                                "Account created! Now try to login",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(activity, "Empty",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    fun encodeEmail(email: String) : String{
        return email.replace('.',',')
    }

    fun decodeEmail(encodedEmail: String): String{
        return encodedEmail.replace(',','.')
    }

}