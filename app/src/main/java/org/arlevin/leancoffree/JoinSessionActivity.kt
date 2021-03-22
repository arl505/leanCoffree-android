package org.arlevin.leancoffree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class JoinSessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_join_session)
    }
}