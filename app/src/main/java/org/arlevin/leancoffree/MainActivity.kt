package org.arlevin.leancoffree

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    fun onJoinClick(view: View) {
        val intent = Intent(this, JoinSessionActivity::class.java).apply {}
        startActivity(intent)
    }
}