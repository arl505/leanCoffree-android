package org.arlevin.leancoffree.session

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.lang3.StringUtils
import org.arlevin.leancoffree.Constants
import org.arlevin.leancoffree.R
import org.json.JSONObject


class BrainstormingFragment : Fragment(R.layout.fragment_brainstorming) {

    private val gson = Gson()
    var votesView: TextView? = null
    private var brainstormingListContainer: LinearLayout? = null
    private var brainstormingList: LinearLayout? = null

    fun updateVotesLeft(votesLeft: Int) {
        val votesLeftString = "Votes Left: $votesLeft"
        try {
            votesView!!.text = votesLeftString
        } catch (e: Exception) { }
    }

    fun updateTopics() {
        if (brainstormingListContainer != null) {
            brainstormingList!!.removeAllViews()
            generateBrainstormingList()

            brainstormingListContainer!!.removeAllViews()
            brainstormingListContainer!!.addView(brainstormingList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votesView = view.findViewById(R.id.votesLeftTv)
        val votesLeftString = "Votes Left: 3"
        votesView!!.text = votesLeftString

        brainstormingListContainer = view.findViewById(R.id.brainstormingList)

        brainstormingList = LinearLayout(view.context)
        brainstormingList!!.orientation = LinearLayout.VERTICAL

        generateBrainstormingList()
        brainstormingListContainer!!.addView(brainstormingList)

        val submitTopicButton = view.findViewById<Button>(R.id.submitTopicBtn)
        submitTopicButton.setOnClickListener {
            submitTopic(view)
        }
    }

    private fun generateBrainstormingList() {
        if (SessionActivity.topics.has("discussionBacklogTopics")) {
            val topicsList = SessionActivity.topics.getJSONArray("discussionBacklogTopics")
            for (i in 0 until topicsList.length()) {
                val currentItem = topicsList.getJSONObject(i)
                val currentElement = layoutInflater.inflate(R.layout.brainstorming_item, null)

                val topicTv: TextView = currentElement.findViewById(R.id.brainstormingTopicTv)
                val votesTv: TextView = currentElement.findViewById(R.id.brainstormingVotesTv)
                val voteBtn: Button = currentElement.findViewById(R.id.brainstormingVoteBtn)
                val deleteBtn: Button = currentElement.findViewById(R.id.brainstormingDeleteBtn)

                val votesText = "Votes: " + currentItem.getJSONArray("voters").length()

                val voters = currentItem.getString("voters")
                val stringArray = object : TypeToken<List<String>>() {}.type
                val votersList: List<String> = gson.fromJson(voters, stringArray)

                when {
                    votersList.contains(SessionActivity.username) -> {
                        val btnText = "Un-Vote"
                        voteBtn.text = btnText
                        voteBtn.visibility = View.VISIBLE
                        voteBtn.setOnClickListener {
                            postVoteForTopic(
                                "UNCAST",
                                currentItem.getString("text"),
                                currentItem.getString("authorDisplayName"),
                                currentElement.context
                            )
                        }
                    }
                    SessionActivity.votesLeft > 0 -> {
                        val btnText = "Vote"
                        voteBtn.text = btnText
                        voteBtn.visibility = View.VISIBLE
                        voteBtn.setOnClickListener {
                            postVoteForTopic(
                                "CAST",
                                currentItem.getString("text"),
                                currentItem.getString("authorDisplayName"),
                                currentElement.context
                            )
                        }
                    }
                    else -> {
                        voteBtn.visibility = View.INVISIBLE
                    }
                }

                if (isModerator() || StringUtils.equalsAnyIgnoreCase(
                        currentItem.getString("authorDisplayName"),
                        SessionActivity.username
                    )
                ) {
                    val delete = "Delete"
                    deleteBtn.text = delete
                    deleteBtn.visibility = View.VISIBLE
                    deleteBtn.setOnClickListener {
                        deleteTopic(
                            currentItem.getString("text"),
                            currentItem.getString("authorDisplayName"),
                            currentElement.context
                        )
                    }
                } else {
                    deleteBtn.visibility = View.INVISIBLE
                }

                topicTv.text = currentItem.getString("text").trim()
                votesTv.text = votesText

                brainstormingList!!.addView(currentElement)
            }
        }
    }

    private fun isModerator(): Boolean {
        val moderators = SessionActivity.users.getJSONArray("moderator")
        for (i in 0 until moderators.length()) {
            if (moderators[i] == SessionActivity.username) {
                return true
            }
        }
        return false
    }

    private fun submitTopic(view: View) {
        val topicEt = view.findViewById<EditText>(R.id.composeEditText)
        if (StringUtils.isNotBlank(topicEt.text)) {
            val jsonRequest = JSONObject()
            jsonRequest.put("sessionId", SessionActivity.sessionId)
            jsonRequest.put("submissionText", topicEt.text)
            jsonRequest.put("displayName", SessionActivity.username)

            val url = Constants.backendBaseUrl + "/submit-topic"
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonRequest, {}, {})

            val queue = Volley.newRequestQueue(context)
            queue.add(jsonObjectRequest)
        } else {
            Toast.makeText(view.context, "Cannot submit blank topic", Toast.LENGTH_SHORT).show()
        }
        topicEt.setText("")
    }

    private fun postVoteForTopic(command: String, text: String, authorDisplayName: String, context: Context) {
        val jsonRequest = JSONObject()
        jsonRequest.put("sessionId", SessionActivity.sessionId)
        jsonRequest.put("text", text)
        jsonRequest.put("authorDisplayName", authorDisplayName)
        jsonRequest.put("voterDisplayName", SessionActivity.username)
        jsonRequest.put("command", command)

        val url = Constants.backendBaseUrl + "/post-vote"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonRequest, {}, {})

        val queue = Volley.newRequestQueue(context)
        queue.add(jsonObjectRequest)
    }

    private fun deleteTopic(text: String, name: String, context: Context) {
        val jsonRequest = JSONObject()
        jsonRequest.put("sessionId", SessionActivity.sessionId)
        jsonRequest.put("topicText", text)
        jsonRequest.put("authorName", name)

        val url = Constants.backendBaseUrl + "/delete-topic"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonRequest, {}, {})

        val queue = Volley.newRequestQueue(context)
        queue.add(jsonObjectRequest)
    }
}
