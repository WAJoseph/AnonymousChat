package com.anonymouscommunication.anonymouschat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anonymouscommunication.anonymouschat.Model.Chat
import com.anonymouscommunication.anonymouschat.Model.Message
import com.anonymouscommunication.anonymouschat.R
import com.anonymouscommunication.anonymouschat.ui.adapters.ChatMessagesAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatFragment() : Fragment() {

    companion object {
        private const val TAG = "ChatFragment"
    }

    private lateinit var chat: Chat
    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.chat_fragment, container, false)

        // Get the chat object from the arguments
        chat = arguments?.getSerializable("chat") as Chat

        // Set up the RecyclerView
        view.findViewById<RecyclerView>(R.id.chat_messages).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            chatMessagesAdapter = ChatMessagesAdapter(chat.messages)
            adapter = chatMessagesAdapter
        }

        // Set up a ChildEventListener to update the RecyclerView when messages are added or removed
        val messagesRef = FirebaseDatabase.getInstance().getReference("messages")
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val message = dataSnapshot.getValue(Message::class.java)
                if (message != null) {
                    chatMessagesAdapter.addMessage(message)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                // Do nothing
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val message = dataSnapshot.getValue(Message::class.java)
                if (message != null) {
                    chatMessagesAdapter.removeMessage(message)
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                // Do nothing
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadMessages:onCancelled", databaseError.toException())
            }
        })


            // Set up the send button to send a message in the chat
            view.findViewById<Button>(R.id.chat_send_button).setOnClickListener {
                // get the message text from the input field
                val messageInput = view.findViewById<EditText>(R.id.chat_message_input)
                val messageText = messageInput.text.toString()
                // send the message if the text is not empty
                if (messageText.isNotEmpty()) {
                    chat.sendMessage(chat.user1, chat.user2, messageText)
                    messageInput.text.clear()
                }
            }

            return view
        }
    }
