package com.nasa.apod.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasa.apod.R
import com.nasa.apod.databinding.FragmentHomeBinding
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupRecyclerView()
        observe()
        setFragmentResultListener("success_create") { requestKey, bundle ->
            if (bundle.getBoolean("success_create")) {
                viewModel.fetchAllMedias()
            }
        }
    }

    private fun setupRecyclerView() {
        val mAdapter = HomeMainMediaAdapter(mutableListOf())

        binding.mediasRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun fetchMedias() {
        viewModel.fetchAllMedias()
    }

    private fun observeState() {
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeMedias() {
        viewModel.mMediaList
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
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
        binding.mediasRecyclerView.adapter?.let {
            if (it is HomeMainMediaAdapter) {
                it.updateList(media)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}