package com.rockbuilds.soundify.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rockbuilds.soundify.Data_class.Item
import com.rockbuilds.soundify.R

class Tracks_adapter(val context: Activity, val TrackList: List<Item>, private val listener: OnItemClickListener) : RecyclerView.Adapter<Tracks_adapter.Myviewholder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class Myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.track_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_names)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myviewholder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_tracks, parent, false)
        return Myviewholder(itemView)
    }

    override fun getItemCount(): Int {
        return TrackList.size
    }

    override fun onBindViewHolder(holder: Myviewholder, position: Int) {
        val currentItem = TrackList[position]

        holder.trackName.text = currentItem.name
        holder.artistName.text = currentItem.artists.joinToString { it.name }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}
