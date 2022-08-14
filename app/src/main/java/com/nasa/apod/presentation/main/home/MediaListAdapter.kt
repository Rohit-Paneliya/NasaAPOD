package com.nasa.apod.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.apod.R
import com.nasa.apod.databinding.ItemMediaBinding
import com.nasa.apod.domain.media.entity.MediaEntity

class HomeMainMediaAdapter(private val mediaList: List<MediaEntity>) :
    RecyclerView.Adapter<HomeMainMediaAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemMediaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(media: MediaEntity) {
            Glide
                .with(itemBinding.root.context)
                .load(media.url)
                .error(R.drawable.placeholder_poster)
                .placeholder(R.drawable.placeholder_poster)
                .into(itemBinding.imageViewPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mediaList[position])

    override fun getItemCount() = mediaList.size
}