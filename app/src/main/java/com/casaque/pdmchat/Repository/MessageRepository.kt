package com.casaque.pdmchat

import com.casaque.pdmchat.Model.Message
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.ValueEventListener
import android.util.Log

class MessageRepository {

    private val db = FirebaseDatabase.getInstance().reference.child("chat")
    private val messages = mutableListOf<Message>()

    init {
        // Listener para monitorar as mudanças no banco de dados
        db.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                    Log.d("MessageRepository", "Message added: $message")
                } else {
                    Log.e("MessageRepository", "Error: Message is null. DataSnapshot: ${snapshot.value}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    val index = messages.indexOfFirst { it.timestamp == message.timestamp }
                    if (index != -1) {
                        messages[index] = message
                        Log.d("MessageRepository", "Message changed: $message")
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    val index = messages.indexOfFirst { it.timestamp == message.timestamp }
                    if (index != -1) {
                        messages.removeAt(index)
                        Log.d("MessageRepository", "Message removed: $message")
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not applicable
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MessageRepository", "Database error: ${error.message}")
            }
        })

        // Listener para carregar mensagens iniciais
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (child in snapshot.children) {
                    val message = child.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                Log.d("MessageRepository", "Initial messages loaded: ${messages.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MessageRepository", "Failed to load initial messages: ${error.message}")
            }
        })
    }

    fun getMessages(recipient: String, callback: (List<Message>) -> Unit) {
        Log.d("MessageRepository", "Fetching messages for recipient: $recipient")
        val filteredMessages = messages.filter { it.recipient == recipient }
        Log.d("MessageRepository", "Fetched ${filteredMessages.size} messages for recipient $recipient")
        callback(filteredMessages)
    }

    fun sendMessage(message: Message) {
        db.push().setValue(message)
        Log.d("MessageRepository", "Sent message: $message")
    }
}
