package org.arlevin.leancoffree.session

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.arlevin.leancoffree.R
import org.json.JSONArray
import org.json.JSONObject


class BrainstormingFragment : Fragment(R.layout.fragment_brainstorming) {

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.brainstormingRv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }
}
