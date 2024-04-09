package com.rockbuilds.soundify.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.rockbuilds.soundify.Data_class.AlbumX
import com.rockbuilds.soundify.Fragments.album_tracks
import com.rockbuilds.soundify.R
import com.squareup.picasso.Picasso


class AlbAdapter(val context: Activity, val AlbumList:List<AlbumX>):
    RecyclerView.Adapter<AlbAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val coverimg: ImageView
        val albumname: TextView
        val byartist:TextView

        init{
            coverimg=itemView.findViewById(R.id.alb_cover)
            albumname=itemView.findViewById(R.id.alb_name)
            byartist=itemView.findViewById(R.id.alb_artist)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
      return AlbumList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = AlbumList[position]

        holder.albumname.text = currentData.name
        val artistNames = currentData.artists.joinToString { it.name }
        holder.byartist.text = "By $artistNames"

        currentData.images.firstOrNull()?.url?.let { imageUrl ->
            Picasso.get().load(imageUrl).into(holder.coverimg)
        }

        holder.itemView.setOnClickListener {
            val fragment = album_tracks()
            val bundle = Bundle()
            bundle.putString("album_id",currentData.id)
            bundle.putString("album_name", currentData.name)
            bundle.putString("album_image", currentData.images.firstOrNull()?.url)
            fragment.arguments = bundle

            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_frame, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
            }
        }

        }
