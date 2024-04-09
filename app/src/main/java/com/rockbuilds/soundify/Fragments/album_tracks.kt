package com.rockbuilds.soundify.Fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockbuilds.soundify.API_interfaces.DataInterface
import com.rockbuilds.soundify.Data_class.Item
import com.rockbuilds.soundify.MainPlayer
import com.rockbuilds.soundify.R
import com.rockbuilds.soundify.adapters.Tracks_adapter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class album_tracks : Fragment(), Tracks_adapter.OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var tracksAdapter: Tracks_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album_tracks, container, false)

        val album_title = view.findViewById<TextView>(R.id.album_title)
        val album_cover = view.findViewById<ImageView>(R.id.album_cover)
        val back = view.findViewById<ImageView>(R.id.back_btn)
        val like = view.findViewById<ImageView>(R.id.like_icon)
        val download = view.findViewById<ImageView>(R.id.download)
        val play = view.findViewById<ImageView>(R.id.play)
        val shuffle=view.findViewById<ImageView>(R.id.shuffle)
        val dots = view.findViewById<ImageView>(R.id.dots)

        recyclerView = view.findViewById(R.id.track_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val albumName = arguments?.getString("album_name")
        val albumImage = arguments?.getString("album_image")
        val albumId = arguments?.getString("album_id") ?: ""

        albumName?.let {
            album_title.text = it
        }

        albumImage?.let {
            Picasso.get().load(it).into(album_cover)
        }

        //Populating the Data
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataInterface::class.java)

        val retrofitTrack = retrofitBuilder.getAlbumData(albumId)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = retrofitTrack.execute()
                if (response.isSuccessful) {
                    val album = response.body()?.albums?.get(0)
                    val tracks = album?.tracks?.items ?: emptyList<Item>()
                    withContext(Dispatchers.Main) {
                        tracksAdapter = Tracks_adapter(requireActivity(), tracks, this@album_tracks)
                        recyclerView.adapter = tracksAdapter
                    }
                } else {
                    Log.e("AlbumTracksFragment", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        like.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Album Liked", Toast.LENGTH_SHORT)
            toast.show()
        }
        download.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Download feature is only for Premium users", Toast.LENGTH_LONG)
            toast.show()
        }
        dots.setOnClickListener {
            showDialog()
        }
        shuffle.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Album Already in order", Toast.LENGTH_SHORT)
            toast.show()
        }
        play.setOnClickListener {
            onItemClick(0)
        }

        return view
    }

    override fun onItemClick(position: Int) {
        val selectedTrack = tracksAdapter.TrackList[position]
        val albumImage = arguments?.getString("album_image")
        val albumName = arguments?.getString("album_name")
        val intent = Intent(requireContext(), MainPlayer::class.java).apply {
            putExtra("track_name", selectedTrack.name)
            putExtra("artist_name", selectedTrack.artists.joinToString { it.name })
            putExtra("duration_ms", selectedTrack.duration_ms)
            putExtra("album_image", albumImage) // Passing the album image here
            putExtra("album_name", albumName)
            putExtra("Song", selectedTrack.preview_url)
        }
        startActivity(intent)
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        val window: Window = dialog.window!!

        val like: LinearLayout = dialog.findViewById(R.id.like)
        val download: LinearLayout = dialog.findViewById(R.id.download)
        val queue: LinearLayout = dialog.findViewById(R.id.queue)
        val share: LinearLayout = dialog.findViewById(R.id.share)

        like.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Album Liked", Toast.LENGTH_SHORT)
            toast.show()
            dialog.hide()
        }

        download.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Only for Premium users", Toast.LENGTH_SHORT)
            toast.show()
            dialog.hide()
        }

        queue.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Album Added to queue", Toast.LENGTH_SHORT)
            toast.show()
            dialog.hide()
        }
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Share Soundify App")
                putExtra(Intent.EXTRA_TEXT, "Check out Soundify App at: https://play.google.com/store/apps/details?id=soundify.rockbuilds")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Soundify using"))
            dialog.hide()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.BOTTOM)
    }
}
