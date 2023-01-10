package com.anonymouscommunication.anonymouschat.ui

import com.anonymouscommunication.anonymouschat.Model.Chat
import com.firebase.ui.auth.data.model.User

interface OnChatListItemClick {
    fun onItemClick(chat: Chat)
}