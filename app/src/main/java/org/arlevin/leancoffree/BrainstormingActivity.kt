package org.arlevin.leancoffree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BrainstormingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_brainstorming)

        val sessionId = intent.getStringExtra("id")
        val sessionStatus = intent.getStringExtra("status")

        val tv1 = findViewById<TextView>(R.id.tv1).apply {
            text = sessionId
        }

        val tv2 = findViewById<TextView>(R.id.tv2).apply {
            text = sessionStatus
        }
    }
}