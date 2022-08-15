package com.nasa.apod.presentation.main.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nasa.apod.R
import com.nasa.apod.databinding.ActivityMediaDetailBinding
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.presentation.utils.DateTimeFormatter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaDetailBinding

    companion object {
        const val ARG_SELECTED_MEDIA = "ARG_SELECTED_MEDIA"
        fun getIntent(context: Context, item: MediaEntity?): Intent {
            val intent = Intent(context,MediaDetailActivity::class.java)
            intent.putExtra(ARG_SELECTED_MEDIA,item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedMedia = intent?.getParcelableExtra<MediaEntity>(ARG_SELECTED_MEDIA)

        binding.imageButtonBack.setOnClickListener {
            super.onBackPressed()
        }

        selectedMedia?.let {
            binding.apply {
                binding.viewGroup.visibility = View.VISIBLE
                binding.textViewNoDataAvailable.visibility = View.GONE

                Glide
                    .with(this@MediaDetailActivity)
                    .load(selectedMedia.hdurl)
                    .error(R.drawable.placeholder_poster)
                    .placeholder(R.drawable.placeholder_poster)
                    .into(imageViewHdPoster)

                textViewTitle.text = selectedMedia.title
                textViewDescription.text = selectedMedia.explanation
                textViewDate.text = getString(R.string.captured_date,DateTimeFormatter().displayDateFromSystemDate(selectedMedia.date))

                when {
                    TextUtils.isEmpty(selectedMedia.copyright).not() -> {
                        textViewCopyRight.text = getString(R.string.copyright_text,selectedMedia.copyright)
                    }
                }
            }
        } ?: kotlin.run {
            binding.viewGroup.visibility = View.GONE
            binding.textViewNoDataAvailable.visibility = View.VISIBLE
        }
    }
}