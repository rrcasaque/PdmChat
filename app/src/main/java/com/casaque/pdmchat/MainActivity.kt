package com.casaque.pdmchat

import MessageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageRepository: MessageRepository
    private lateinit var usernameInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageRepository = MessageRepository()

        usernameInput = findViewById(R.id.username_input)
        loginButton = findViewById(R.id.login_button)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(emptyList())
        recyclerView.adapter = messageAdapter

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotBlank()) {
                Log.d("MainActivity", "Loading messages for user: $username")
                loadMessagesForUser(username)
            } else {
                Log.d("MainActivity", "Username is blank")
            }
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SendMessageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadMessagesForUser(username: String) {
        messageRepository.getMessages(username) { messages ->
            Log.d("MainActivity", "Updating adapter with ${messages.size} messages")
            messageAdapter.updateMessages(messages)
        }
    }
}
