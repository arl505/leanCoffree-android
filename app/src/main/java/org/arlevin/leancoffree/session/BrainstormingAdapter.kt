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
        holder.tv.text = currentItem.getString("text")
    }

    override fun getItemCount() = brainstormingList.size

    class BrainstormingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById<TextView>(R.id.brainstormingTopicTv)
    }

    fun updateBrainstormingList(activity: Activity, newList: ArrayList<JSONObject>) {
        brainstormingList.clear()
        brainstormingList.addAll(newList)
        activity.runOnUiThread{ this.notifyDataSetChanged() }
    }
}