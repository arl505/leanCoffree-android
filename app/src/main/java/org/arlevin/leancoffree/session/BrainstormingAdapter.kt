package org.arlevin.leancoffree.session

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.arlevin.leancoffree.Constants
import org.arlevin.leancoffree.R
import org.json.JSONObject

class BrainstormingAdapter() : RecyclerView.Adapter<BrainstormingAdapter.BrainstormingViewHolder>() {

    private val brainstormingList: ArrayList<JSONObject> = ArrayList()
    private val gson = Gson()

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

        val voters = currentItem.getString("voters")
        val stringArray = object : TypeToken<List<String>>() {}.type
        val votersList: List<String> = gson.fromJson(voters, stringArray)

        when {
            votersList.contains(SessionActivity.username) -> {
                val btnText = "Un-Vote"
                holder.voteBtn.text = btnText
                holder.voteBtn.visibility = VISIBLE
                holder.voteBtn.setOnClickListener {
                    postVoteForTopic(
                        "UNCAST",
                        currentItem.getString("text"),
                        currentItem.getString("authorDisplayName"),
                        holder.itemView.context
                    )
                }
            }
            SessionActivity.votesLeft > 0 -> {
                val btnText = "Vote"
                holder.voteBtn.text = btnText
                holder.voteBtn.visibility = VISIBLE
                holder.voteBtn.setOnClickListener {
                    postVoteForTopic(
                        "CAST",
                        currentItem.getString("text"),
                        currentItem.getString("authorDisplayName"),
                        holder.itemView.context
                    )
                }
            }
            else -> {
                holder.voteBtn.visibility = INVISIBLE
            }
        }

        if (isModerator()) {
            val delete = "Delete"
            holder.deleteBtn.text = delete
            holder.deleteBtn.visibility = VISIBLE
            holder.deleteBtn.setOnClickListener {
                deleteTopic(
                    currentItem.getString("text"),
                    currentItem.getString("authorDisplayName"),
                    holder.itemView.context
                )
            }
        } else {
            holder.deleteBtn.visibility = INVISIBLE
        }

        holder.topicTv.text = currentItem.getString("text").trim()
        holder.votesTv.text = votesText
    }

    override fun getItemCount() = brainstormingList.size

    class BrainstormingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicTv: TextView = itemView.findViewById(R.id.brainstormingTopicTv)
        val votesTv: TextView = itemView.findViewById(R.id.brainstormingVotesTv)
        val voteBtn: Button = itemView.findViewById(R.id.brainstormingVoteBtn)
        val deleteBtn: Button = itemView.findViewById(R.id.brainstormingDeleteBtn)
    }

    fun updateBrainstormingList(activity: Activity, newList: ArrayList<JSONObject>) {
        brainstormingList.clear()
        brainstormingList.addAll(newList)
        activity.runOnUiThread{ this.notifyDataSetChanged() }
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