package com.shakib.gsk2022.presentation.screens.picker

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.shakib.gsk2022.MainActivity
import com.shakib.gsk2022.common.base.BaseFragment
import com.shakib.gsk2022.common.extensions.collectFlow
import com.shakib.gsk2022.common.extensions.showLongToast
import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.databinding.FragmentMediaPickerBinding
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MediaPickerFragment : BaseFragment<FragmentMediaPickerBinding>() {

    private val viewModel: MediaPickerViewModel by viewModels()
    private lateinit var mediaPickerAdapter: MediaPickerAdapter
    private lateinit var rxPermissions: RxPermissions
    private var maxSelection = 1
    private val selectedImages: ArrayList<Image> = ArrayList()

    override fun getBaseViewModel() = viewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMediaPickerBinding.inflate(layoutInflater, container, false)

    override fun configureViews(savedInstanceState: Bundle?) {
        super.configureViews(savedInstanceState)
        data?.let { maxSelection = it.toInt() }
        configureRecyclerView()
        fetchAllImagesWithPermission()
        binding.fabDone.setOnClickListener {
            (activity as MainActivity).selectedImages = selectedImages
            findNavController().navigateUp()
        }
    }

    override fun bindWithViewModel() {
        super.bindWithViewModel()
        collectFlow(viewModel.imageListStateFlow) {
                when (it) {
                    is Resource.Loading -> Timber.d("Show Loading")
                    is Resource.Success -> mediaPickerAdapter.submitList(it.data)
                    is Resource.Error -> Timber.e(it.throwable.message)
                }
            }
    }

    private fun fetchAllImagesWithPermission() {
        rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe { granted ->
            if (granted)
                viewModel.fetchAllImages()
            else
                context?.showLongToast("Permission Denied!!!")
        }
    }

    private fun configureRecyclerView() {
        mediaPickerAdapter = MediaPickerAdapter(selectedImages, maxSelection)
        binding.rvImage.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = mediaPickerAdapter
        }
    }
}