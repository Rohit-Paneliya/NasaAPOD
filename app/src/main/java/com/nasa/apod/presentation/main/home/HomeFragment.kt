package com.nasa.apod.presentation.main.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nasa.apod.R
import com.nasa.apod.databinding.FragmentHomeBinding
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.presentation.common.extension.showToast
import com.nasa.apod.presentation.interfaces.OnItemClickListener
import com.nasa.apod.presentation.main.detail.MediaDetailActivity
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
        observe()
        viewModel.fetchAllMedias()
    }

    private fun observeState() {
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeMedias() {
        viewModel.mMediaList
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { medias ->
                handleMedias(medias)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observe() {
        observeState()
        observeMedias()
    }

    private fun handleMedias(media: List<MediaEntity>) {

        val mAdapter = HomeMainMediaAdapter(media, this)

        binding.mediasRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    private fun handleState(state: HomeMainFragmentState) {
        when (state) {
            is HomeMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeMainFragmentState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    override fun onListItemClicked(item: Pair<MediaEntity, ImageView>) {
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            item.second,
            "imagePoster"
        )
        startActivity(MediaDetailActivity.getIntent(requireActivity(), item.first),options.toBundle())
    }
}