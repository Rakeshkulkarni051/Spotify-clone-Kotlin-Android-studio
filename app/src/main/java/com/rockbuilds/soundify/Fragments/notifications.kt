package com.rockbuilds.soundify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.rockbuilds.soundify.R

class notifications : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val back_btn = view.findViewById<ImageView>(R.id.back)


        back_btn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Now you can use back_btn as needed

        return view
    }
}
