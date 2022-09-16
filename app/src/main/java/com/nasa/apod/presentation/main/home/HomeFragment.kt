package com.nasa.apod.presentation.main.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nasa.apod.R
import com.nasa.apod.databinding.FragmentHomeBinding
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.presentation.interfaces.OnItemClickListener
import com.nasa.apod.presentation.main.detail.MediaDetailActivity
import com.nasa.apod.presentation.utils.UiStateHandler
import com.nasa.apod.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    OnItemClickListener<Pair<MediaEntity, ImageView>> {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        setObservers()

        viewModel.fetchAllMedias()
    }

    private fun setObservers() {
        viewModel.mHandleResponse
            .onEach { state -> handleStateAndData(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleMedias(media: List<MediaEntity>) {

        when {
            media.isNotEmpty() -> {
                binding.mediasRecyclerView.visibility = View.VISIBLE
                binding.textViewErrorMessage.visibility = View.GONE

                val mAdapter = HomeMainMediaAdapter(media, this)
                binding.mediasRecyclerView.apply {
                    adapter = mAdapter
                    setHasFixedSize(true)
                }
            }
            else -> {
                binding.mediasRecyclerView.visibility = View.GONE
                binding.textViewErrorMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun handleStateAndData(state: UiStateHandler<List<MediaEntity>>) {
        when (state) {
            is UiStateHandler.Loading -> handleLoading(state.isLoading)
            is UiStateHandler.Success -> handleMedias(state.data)
            is UiStateHandler.Error -> handleError(state)
            is UiStateHandler.ShowMessage -> context?.showToast(state.message)
            is UiStateHandler.Init -> Unit
        }
    }

    private fun handleError(state: UiStateHandler.Error<List<MediaEntity>>) {
        binding.apply {
            mediasRecyclerView.visibility = View.GONE
            loadingProgressBar.visibility = View.GONE
            textViewErrorMessage.visibility = View.VISIBLE
            textViewErrorMessage.text = state.message
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        when {
            isLoading -> {
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.mediasRecyclerView.visibility = View.GONE
                binding.textViewErrorMessage.visibility = View.GONE
            }
            else -> {
                binding.loadingProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onListItemClicked(item: Pair<MediaEntity, ImageView>) {
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            item.second,
            getString(R.string.transition_name)
        )
        startActivity(
            MediaDetailActivity.getIntent(requireActivity(), item.first),
            options.toBundle()
        )
    }
}