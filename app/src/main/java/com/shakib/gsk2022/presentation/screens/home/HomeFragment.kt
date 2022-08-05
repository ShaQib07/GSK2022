package com.shakib.gsk2022.presentation.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.shakib.gsk2022.MainActivity
import com.shakib.gsk2022.R
import com.shakib.gsk2022.common.base.BaseFragment
import com.shakib.gsk2022.common.extensions.gone
import com.shakib.gsk2022.common.extensions.showShortToast
import com.shakib.gsk2022.common.extensions.visible
import com.shakib.gsk2022.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter

    override fun getBaseViewModel() = viewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun configureViews(savedInstanceState: Bundle?) {
        super.configureViews(savedInstanceState)
        binding.fabAdd.setOnClickListener { showPopUpMenu(it) }
        binding.fabUpload.setOnClickListener { upload() }
    }

    override fun onResume() {
        super.onResume()
        configureRecyclerView()
    }

    private fun showPopUpMenu(anchor: View) {
        val popupMenu = context?.let { PopupMenu(it, anchor) }
        popupMenu?.menu?.apply {
            add(getString(R.string.use_camera)).setOnMenuItemClickListener {
                screenNavigator.toCameraFragment(findNavController(), "3")
                true
            }
            add(getString(R.string.pick_from_gallery)).setOnMenuItemClickListener {
                screenNavigator.toMediaPickerFragment(findNavController(), "5")
                true
            }
        }
        popupMenu?.show()
    }

    private fun upload() {
        (activity as MainActivity).selectedImages.apply {
            if (isEmpty())
                context?.showShortToast("No content to upload")
            else {
                viewModel.createOneTimeWorkRequest(this)
            }
        }
    }

    private fun configureRecyclerView() {
        Timber.d("XYZ | configureRecyclerView called ${(activity as MainActivity).selectedImages}")
        if ((activity as MainActivity).selectedImages.isNotEmpty())
            binding.ivEmptyBg.gone()
        else
            binding.ivEmptyBg.visible()
        imageAdapter = ImageAdapter((activity as MainActivity).selectedImages)
        binding.rvImage.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = imageAdapter
        }
    }
}
