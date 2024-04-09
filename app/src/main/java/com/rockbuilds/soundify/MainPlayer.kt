package com.rockbuilds.soundify

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.io.IOException

class MainPlayer : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var durationText: TextView
    private lateinit var startText: TextView
    private val handler = Handler(Looper.getMainLooper())

    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_player)

        val trackName = intent.getStringExtra("track_name")
        val artistName = intent.getStringExtra("artist_name")
        val albumImage = intent.getStringExtra("album_image")
        val songUri = intent.getStringExtra("Song")
        val albumName = intent.getStringExtra("album_name")
        val songDurationMillis = intent.getIntExtra("duration_ms", 0)

        val songDurationMinutes = (songDurationMillis / 1000) / 60
        val songDurationSeconds = (songDurationMillis / 1000) % 60
        val formattedDuration = String.format("%d:%02d", songDurationMinutes, songDurationSeconds)

        val albumText = findViewById<TextView>(R.id.text_album)
        val textTrack = findViewById<TextView>(R.id.track_text)
        val textArtist = findViewById<TextView>(R.id.artist_text)
        val albumCover = findViewById<ImageView>(R.id.album_image)
        val back=findViewById<ImageView>(R.id.back_exit)
        val dots=findViewById<ImageView>(R.id.dots)
        val like=findViewById<ImageView>(R.id.like_btn)
        val shuffle=findViewById<ImageView>(R.id.shuffle)
        val loop=findViewById<ImageView>(R.id.loop)
        val play_next=findViewById<ImageView>(R.id.next)
        val previous=findViewById<ImageView>(R.id.previous)
        durationText = findViewById(R.id.total_dura)
        startText = findViewById(R.id.start_dura)
        seekBar = findViewById(R.id.seek_bar)

        albumText.text = albumName
        textTrack.text = trackName
        textArtist.text = artistName
        durationText.text = formattedDuration

        albumImage?.let {
            Picasso.get().load(it).into(albumCover)
        }

        mediaPlayer = MediaPlayer()

        mediaPlayer.apply {
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            try {
                setDataSource(songUri)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@MainPlayer, "Error setting the media source", Toast.LENGTH_SHORT).show()
            }
        }

        initializeSeekBar(30000) // 30 seconds in milliseconds

        val playPauseButton = findViewById<ImageView>(R.id.play_pause)

        playPauseButton.setOnClickListener {
            if (isPaused) {
                mediaPlayer.start()
                isPaused = false
                playPauseButton.setImageResource(R.drawable.pause_icon)
                initializeSeekBar(mediaPlayer.duration)
            } else {
                mediaPlayer.pause()
                isPaused = true
                playPauseButton.setImageResource(R.drawable.play_icon)
                handler.removeCallbacksAndMessages(null)
            }
        }
        back.setOnClickListener {
                finish()
        }
        dots.setOnClickListener {
            showDialog()
        }
        like.setOnClickListener {
            val toast = Toast.makeText(this, "Song Liked", Toast.LENGTH_SHORT)
            toast.show()
        }
        shuffle.setOnClickListener {
            val toast = Toast.makeText(this, "Tracks Shuffled", Toast.LENGTH_SHORT)
            toast.show()
        }
        loop.setOnClickListener{
            val toast = Toast.makeText(this, "Track Looped", Toast.LENGTH_SHORT)
            toast.show()
        }
        play_next.setOnClickListener {
            val toast = Toast.makeText(this, "API Limitation! can't play Next", Toast.LENGTH_LONG)
            toast.show()
        }
        previous.setOnClickListener {
            val toast = Toast.makeText(this, "API Limitation! can't Stack Back", Toast.LENGTH_LONG)
            toast.show()
        }






    }

    private fun initializeSeekBar(duration: Int) {
        seekBar.max = duration

        handler.post(object : Runnable {
            override fun run() {
                val progress = seekBar.progress + 1000 // incrementing by 1 second
                seekBar.progress = progress
                startText.text = formatDuration(progress)

                if (progress < duration && !isPaused) {
                    handler.postDelayed(this, 1000) // update every second
                }
            }
        })
    }


    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        val window: Window = dialog.window!!

        val like: LinearLayout = dialog.findViewById(R.id.like)
        val download: LinearLayout = dialog.findViewById(R.id.download)
        val queue: LinearLayout = dialog.findViewById(R.id.queue)
        val share: LinearLayout=dialog.findViewById(R.id.share)

        like.setOnClickListener {
            val toast = Toast.makeText(this, "Album Liked", Toast.LENGTH_SHORT)
            toast.show()
            dialog.hide()
        }

        download.setOnClickListener {
            val toast = Toast.makeText(this, "Only for Premium users", Toast.LENGTH_SHORT)
            toast.show()
            dialog.hide()
        }

        queue.setOnClickListener {
            val toast = Toast.makeText(this, "Album Added to queue", Toast.LENGTH_SHORT)
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

    private fun formatDuration(duration: Int): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
}
