package com.rockbuilds.soundify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import com.rockbuilds.soundify.API_interfaces.DataInterface
import com.rockbuilds.soundify.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search : Fragment() {

    lateinit var searchView: SearchView
    lateinit var search_text: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchView = view.findViewById(R.id.Search_box)
        search_text=view.findViewById(R.id.search_query)


        //Populating the Data
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataInterface::class.java)
        setupSearchView()



        return view
    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // User pressed the search button
                if (query != null) {
                    performSearch(query)
                }
                return false // If true, it consumes the event; otherwise, it passes the event on
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Text has changed, apply filtering logic
                return false
            }
        })
    }
    private fun performSearch(query: String) {
        search_text.visibility=View.VISIBLE
        search_text.text=query
        println("Searching for: $query")
    }
}