package org.arlevin.leancoffree.session

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.arlevin.leancoffree.R
import org.json.JSONObject

class BrainstormingAdapter() : RecyclerView.Adapter<BrainstormingAdapter.BrainstormingViewHolder>() {

    private val brainstormingList: ArrayList<JSONObject> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrainstormingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.brainstorming_item,
            parent, false
        )
        return BrainstormingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BrainstormingViewHolder, position: Int) {
        val currentItem = JSONObject(brainstormingList[position].toString())
        val votesText = "Votes: " + currentItem.getJSONArray("voters").length()
        holder.topicTv.text = currentItem.getString("text").trim()
        holder.votesTv.text = votesText
    }

    override fun getItemCount() = brainstormingList.size

    class BrainstormingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicTv: TextView = itemView.findViewById(R.id.brainstormingTopicTv)
        val votesTv: TextView = itemView.findViewById(R.id.brainstormingVotesTv)
    }

    fun updateBrainstormingList(activity: Activity, newList: ArrayList<JSONObject>) {
        brainstormingList.clear()
        brainstormingList.addAll(newList)
        activity.runOnUiThread{ this.notifyDataSetChanged() }
    }
}