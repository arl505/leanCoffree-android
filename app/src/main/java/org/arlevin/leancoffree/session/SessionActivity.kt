package org.arlevin.leancoffree.session

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.arlevin.leancoffree.R

class SessionActivity : AppCompatActivity() {

    var sessionId = ""
    var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_session)

        sessionId = intent.getStringExtra("id")!!

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.sessionFrame, UsernamePromptFragment())
            commit()
        }
    }

    fun setBrainstorming() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.sessionFrame, BrainstormingFragment())
            commit()
        }
    }
}