package com.nasa.apod.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nasa.apod.databinding.ItemMediaBinding
import com.nasa.apod.domain.media.entity.MediaEntity

class HomeMainMediaAdapter(private val mediaList: MutableList<MediaEntity>) : RecyclerView.Adapter<HomeMainMediaAdapter.ViewHolder>(){

    fun updateList(mMedia: List<MediaEntity>){
        mediaList.clear()
        mediaList.addAll(mMedia)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemMediaBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(media: MediaEntity){
            itemBinding.mediaNameTextView.text = media.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mediaList[position])

    override fun getItemCount() = mediaList.size
}