package org.arlevin.leancoffree.session

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.apache.commons.lang3.StringUtils
import org.arlevin.leancoffree.Constants
import org.arlevin.leancoffree.R
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class BrainstormingFragment : Fragment(R.layout.fragment_brainstorming) {

    var votesView: TextView? = null

    companion object {
        var adapter = BrainstormingAdapter()

        fun notifyTopics(activity: Activity, newTopics: JSONArray) {
            val topicsList = ArrayList<JSONObject>()
            for (i in 0 until newTopics.length()) {
                topicsList.add(newTopics.getJSONObject(i))
            }
            adapter.updateBrainstormingList(activity, topicsList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votesView = view.findViewById(R.id.votesLeftTv)
        val votesLeftString = "Votes Left: 3"
        votesView!!.text = votesLeftString

        val recyclerView = view.findViewById<RecyclerView>(R.id.brainstormingRv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val submitTopicButton = view.findViewById<Button>(R.id.submitTopicBtn)
        submitTopicButton.setOnClickListener {
            submitTopic(view)
        }
    }

    fun updateVotesLeft(votesLeft: Int) {
        val votesLeftString = "Votes Left: $votesLeft"
        try {
            votesView!!.text = votesLeftString
        } catch (e: Exception) { }
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
}
