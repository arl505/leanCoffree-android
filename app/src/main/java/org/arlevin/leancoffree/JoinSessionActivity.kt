package org.arlevin.leancoffree

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class JoinSessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_join_session)

        val editText = findViewById<EditText>(R.id.joinInput)
        editText.setOnEditorActionListener() { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSubmitJoinSession(View(applicationContext))
                true
            } else {
                false
            }
        }
    }

    fun goHome(view: View) {
        finish()
    }

    fun onSubmitJoinSession(view: View) {
        val input = findViewById<EditText>(R.id.joinInput).text.toString()
        val regex =
            "[0-9, a-f]{8}-[0-9, a-f]{4}-[0-9, a-f]{4}-[0-9, a-f]{4}-[0-9, a-f]{12}"
                .toRegex(RegexOption.IGNORE_CASE)
        val match = regex.find(input)

        if (match != null && match.value.isNotBlank()) {
            val queue = Volley.newRequestQueue(this)
            val url = "https://leancoffree.com:8085/verify-session/" + match.value

            val stringRequest = StringRequest(Request.Method.POST, url,
                { response ->
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getString("verificationStatus") == "VERIFICATION_SUCCESS") {
                        if (jsonResponse.getJSONObject("sessionDetails")
                                .getString("sessionId") == match.value) {
                            finish()
                            val intent = Intent(this, BrainstormingActivity::class.java).apply {
                                putExtra("id", match.value)
                                putExtra("status",
                                    jsonResponse.getJSONObject("sessionDetails")
                                        .getString("sessionStatus")
                                )
                            }
                            startActivity(intent)
                        }
                    }
                },
                {
                    Toast.makeText(
                        applicationContext, "An error occurred, please retry", Toast.LENGTH_SHORT
                    ).show()
                }
            )

            queue.add(stringRequest)
            return
        }

        Toast.makeText(
            applicationContext, "Invalid submission, please fix and retry", Toast.LENGTH_SHORT
        ).show()
    }
}