package com.example.recyclor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class OrderReceived : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_received)

        val buttonBack = findViewById<Button>(R.id.buReturn)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}