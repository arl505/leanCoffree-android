package org.arlevin.leancoffree.session

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.arlevin.leancoffree.R


class UsernamePromptFragment : Fragment(R.layout.fragment_username_prompt) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<TextView>(R.id.backButtonUsername)
        backButton.setOnClickListener {
            activity!!.finish()
        }
    }
}