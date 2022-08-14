package com.nasa.apod.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasa.apod.R
import com.nasa.apod.databinding.FragmentMainHomeBinding
import com.nasa.apod.domain.product.entity.ProductEntity
import com.nasa.apod.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeMainFragment : Fragment(R.layout.fragment_main_home) {

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainHomeBinding.bind(view)
        setupRecyclerView()
        observe()
        setFragmentResultListener("success_create"){ requestKey, bundle ->
            if(bundle.getBoolean("success_create")){
               viewModel.fetchAllMyProducts()
            }
        }
    }

    private fun setupRecyclerView(){
        val mAdapter = HomeMainProductAdapter(mutableListOf())

        binding.productsRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun fetchProducts(){
        viewModel.fetchAllMyProducts()
    }

    private fun observeState(){
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProducts(){
        viewModel.mProducts
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { products ->
                handleProducts(products)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observe(){
        observeState()
        observeProducts()
    }

    private fun handleProducts(products: List<ProductEntity>){
        binding.productsRecyclerView.adapter?.let {
            if(it is HomeMainProductAdapter){
                it.updateList(products)
            }
        }
    }

    private fun handleState(state: HomeMainFragmentState){
        when(state){
            is HomeMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeMainFragmentState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visibility = View.VISIBLE
        }else{
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}