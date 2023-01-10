package com.anonymouscommunication.anonymouschat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anonymouscommunication.anonymouschat.Model.Chat
import com.anonymouscommunication.anonymouschat.R
import com.anonymouscommunication.anonymouschat.ui.OnChatListItemClick

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {


    var onChatListItemClick: OnChatListItemClick? = null
    private val chats = mutableListOf<Chat>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define the views for each chat here
        var chatUserID: TextView = itemView.findViewById(R.id.chat_name)
        var chatLastMessage: TextView = itemView.findViewById(R.id.chat_last_message)

        fun bind(chat: Chat) {
            this.itemView.setOnClickListener {
                onChatListItemClick?.onItemClick(chat)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        // Set the chat data on the views in the ViewHolder
        holder.chatUserID.text = chat.id
        holder.chatLastMessage.text = chat.lastMessage.returnMessage()

        holder.bind(chat)

    }

    override fun getItemCount(): Int {
        return chats.size
    }


    fun addChat(chat: Chat) {
        chats.add(chat)
        //notify the ui so it gets updated instantly
        notifyItemInserted(chats.size - 1)
    }

    fun updateChat(chat: Chat) {
        val index = chats.indexOfFirst { it.id == chat.id }
        if (index >= 0) {
            chats[index] = chat
            notifyItemChanged(index)
        }
    }

    fun removeChat(chat: Chat) {
        val index = chats.indexOfFirst { it.id == chat.id }
        if (index >= 0) {
            chats.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}