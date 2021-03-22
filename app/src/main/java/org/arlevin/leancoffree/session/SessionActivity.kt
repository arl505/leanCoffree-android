package org.arlevin.leancoffree.session

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.arlevin.leancoffree.R

class SessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_session)

        val sessionId = intent.getStringExtra("id")!!
        var sessionStatus = "ASK_FOR_USERNAME"

        val brainstormingFragment = BrainstormingFragment()
        val usernamePromptFragment = UsernamePromptFragment()

        if (sessionStatus.contains("ASK_FOR_USERNAME")) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.sessionFrame, usernamePromptFragment)
                commit()
            }
        } else if (sessionStatus.contains("STARTED")) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.sessionFrame, brainstormingFragment)
                commit()
            }
        }
    }
}