package com.example.myloginapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = intent.getStringExtra("TOKEN")
        val tokenTextView = findViewById<TextView>(R.id.textViewToken)
        tokenTextView.text = "Token: $token"
    }
}
