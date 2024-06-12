package com.casaque.pdmchat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.casaque.pdmchat.Model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SendMessageActivity : AppCompatActivity() {

    private lateinit var messageRepository: MessageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        messageRepository = MessageRepository()

        val etSender: EditText = findViewById(R.id.etSender)
        val etRecipient: EditText = findViewById(R.id.etRecipient)
        val etMessage: EditText = findViewById(R.id.etMessage)
        val btnSend: Button = findViewById(R.id.btnSend)

        btnSend.setOnClickListener {
            val sender = etSender.text.toString()
            val recipient = etRecipient.text.toString()
            val message = etMessage.text.toString()
            if (sender.isNotBlank() && recipient.isNotBlank() && message.isNotBlank()) {
                val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
                val message = Message(sender, time.toString(), message, recipient)
                messageRepository.sendMessage(message)
                finish()
            }
        }
    }
}
