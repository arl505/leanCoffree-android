package org.arlevin.leancoffree.session

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    }

    fun updateVotesLeft(votesLeft: Int) {
        val votesLeftString = "Votes Left: $votesLeft"
        try {
            votesView!!.text = votesLeftString
        } catch (e: Exception) { }
    }
}
