package org.arlevin.leancoffree.session

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.arlevin.leancoffree.R
import org.json.JSONObject
import java.util.*

class UsernamePromptFragment : Fragment(R.layout.fragment_username_prompt) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<TextView>(R.id.backButtonUsername)
        backButton.setOnClickListener {
            activity!!.finish()
        }

        val submitButton = view.findViewById<Button>(R.id.submitUsernameButton)
        submitButton.setOnClickListener {
            submitUsername(view)
        }
    }

    private fun submitUsername(view: View) {
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput).text!!
        if (usernameInput.isNotBlank()) {
            val jsonRequest = JSONObject()
            jsonRequest.put("displayName", usernameInput.toString())
            jsonRequest.put("sessionId", arguments!!.getString("sessionId"))
            jsonRequest.put("command", "ADD")
            jsonRequest.put("websocketUserId", UUID.randomUUID().toString())

            val url = "https://leancoffree.com:8085/refresh-users"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonRequest,
                { response ->
                    if (response.getString("status") != "SUCCESS") {
                        Toast.makeText(
                            activity!!.applicationContext,
                            "An error occurred, please retry", LENGTH_SHORT
                        ).show()
                    } else {
                        // idk yet
                    }
                },
                {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "An error occurred, please retry", LENGTH_SHORT
                    ).show()
                })

            val queue = Volley.newRequestQueue(activity)
            queue.add(jsonObjectRequest)
        } else {
            Toast.makeText(
                activity!!.applicationContext,
                "Invalid submission, please fix and retry", LENGTH_SHORT
            ).show()
        }
    }
}