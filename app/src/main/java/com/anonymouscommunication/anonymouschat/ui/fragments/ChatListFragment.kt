package com.anonymouscommunication.anonymouschat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anonymouscommunication.anonymouschat.Model.Chat
import com.anonymouscommunication.anonymouschat.Model.KnownUser
import com.anonymouscommunication.anonymouschat.R
import com.anonymouscommunication.anonymouschat.ui.OnChatListItemClick
import com.anonymouscommunication.anonymouschat.ui.adapters.ChatListAdapter
import com.google.firebase.database.*
import java.io.Serializable

class ChatListFragment : Fragment(), OnChatListItemClick {

    companion object {
        private const val TAG = "ChatListFragment"
    }

    private var chatListAdapter = ChatListAdapter()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.chat_list_fragment, container, false)



        // Set up the RecyclerView
        view.findViewById<RecyclerView>(R.id.chat_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = chatListAdapter
        }

        // Get the UserID and chats arguments passed from the LoginFragment
        val userEmail = arguments?.getString("UserEmail")
//        val chats = arguments?.getSerializable("chats") as? MutableList<Chat>

//        val addButton = view.findViewById<Button>(R.id.add_button)
//        addButton.setOnClickListener {
//            val receiverID: String = view.findViewById<EditText>(R.id.add_receiver_id).toString()
//
//        }

        // Set up a ChildEventListener to update the RecyclerView when chats are added or removed
        val chatRef = FirebaseDatabase.getInstance().getReference("users/KnownUsers/$userEmail").child("chats")
        chatRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                if (chat != null) {
                    chatListAdapter.addChat(chat)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                if (chat != null) {
                    chatListAdapter.updateChat(chat)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                if (chat != null) {
                    chatListAdapter.removeChat(chat)
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                // Do nothing
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadChats:onCancelled", databaseError.toException())
            }
        })

        return view
    }



        override fun onItemClick(chat: Chat) {
            val bundle = Bundle()
            bundle.putSerializable("chat", chat as Serializable)
            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(androidx.navigation.fragment.R.id.nav_host_fragment_container,chatFragment)?.commit()
            val navController = findNavController()
            navController.navigate(R.id.action_chat_list_fragment_to_chat_fragment)

        }
    }


