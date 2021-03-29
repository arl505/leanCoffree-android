package org.arlevin.leancoffree.session

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import org.arlevin.leancoffree.Constants
import org.arlevin.leancoffree.R
import org.json.JSONArray
import org.json.JSONObject
import ua.naiksoftware.stomp.StompClient

class SessionActivity : AppCompatActivity() {

    companion object {
        var websocketUserId = ""
        var sessionId = ""
        var username = ""
        var topics = JSONObject()
        var users = JSONObject()
        var votesLeft = 3
    }

    private val stompClient: StompClient = StompClient(
        CustomStompProvider(Constants.wsAddress, null, OkHttpClient()))

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_session)

        sessionId = intent.getStringExtra("id")!!

        stompClient.connect()
        stompClient.topic("/topic/users/session/$sessionId").subscribe(
        { message ->
            users = JSONObject(message.payload)
        },
        {
            throw Exception("Unable to subscribe to topic")
        })

        stompClient.topic("/topic/discussion-topics/session/$sessionId").subscribe(
        { message ->
            topics = JSONObject(message.payload)
            BrainstormingFragment.notifyTopics(this, topics.getJSONArray("discussionBacklogTopics"))
            tabulateVotesLeft(topics.getJSONArray("discussionBacklogTopics"))
        },
        {
            throw Exception("Unable to subscribe to topic")
        })

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

    private fun tabulateVotesLeft(backlogTopics: JSONArray) {
        var votesCast = 0
        for (i in 0 until backlogTopics.length()) {
            val voters: JSONArray = JSONObject(backlogTopics[i].toString()).getJSONArray("voters")
            for (j in 0 until voters.length()) {
                if (voters[j] == username) {
                    votesCast += 1
                }
            }
        }
        votesLeft = 3 - votesCast
    }
}