package com.nasa.apod.presentation.main.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import com.bumptech.glide.Glide
import com.nasa.apod.R
import com.nasa.apod.databinding.ActivityMediaDetailBinding
import com.nasa.apod.domain.media.entity.MediaEntity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaDetailBinding

    companion object {
        const val ARG_SELECTED_MEDIA = "ARG_SELECTED_MEDIA"
        fun getIntent(context: Context, item: MediaEntity): Intent {
            val intent = Intent(context,MediaDetailActivity::class.java)
            intent.putExtra(ARG_SELECTED_MEDIA,item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.doOnPreDraw { startPostponedEnterTransition()
        }

        val selectedMedia = intent.getParcelableExtra(ARG_SELECTED_MEDIA) ?: MediaEntity()

        Glide
            .with(this)
            .load(selectedMedia.url)
            .error(R.drawable.placeholder_poster)
            .placeholder(R.drawable.placeholder_poster)
            .into(binding.imageViewHdPoster)
    }
}