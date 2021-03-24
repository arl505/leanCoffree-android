package org.arlevin.leancoffree

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.arlevin.leancoffree.session.Constants
import org.arlevin.leancoffree.session.SessionActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    fun onJoinClick(view: View) {
        val intent = Intent(this, JoinSessionActivity::class.java).apply {}
        startActivity(intent)
    }

    fun onCreateClick(view: View) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.POST, Constants.backendBaseUrl + "/create-session",
            { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.getString("id").isNotBlank()) {
                    val intent = Intent(this, SessionActivity::class.java).apply {
                        putExtra("id", jsonResponse.getString("id"))
                        putExtra("status", "STARTED")
                    }
                    startActivity(intent)
                }
            },
            {
                Toast.makeText(
                    applicationContext, "An error occurred, please retry", Toast.LENGTH_SHORT
                ).show()
            }
        )

        queue.add(stringRequest)
    }
}