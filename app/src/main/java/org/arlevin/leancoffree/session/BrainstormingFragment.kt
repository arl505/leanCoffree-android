package org.arlevin.leancoffree.session

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.arlevin.leancoffree.R

class BrainstormingFragment : Fragment(R.layout.fragment_brainstorming) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv1).text = SessionActivity.sessionId
        view.findViewById<TextView>(R.id.tv2).text = SessionActivity.username
    }
}