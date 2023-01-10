package com.anonymouscommunication.anonymouschat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anonymouscommunication.anonymouschat.Model.Message
import com.anonymouscommunication.anonymouschat.R

class ChatMessagesAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text)
        val messageSender: TextView = itemView.findViewById(R.id.message_sender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.message
        holder.messageSender.text = message.sender.id.toString()
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun removeMessage(message: Message) {
        val index = messages.indexOf(message)
        if (index >= 0) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}