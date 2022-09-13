package com.shakib.gsk2022.presentation.screens.home

import android.Manifest
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
import com.shakib.gsk2022.common.extensions.showLongToast
import com.shakib.gsk2022.common.extensions.showShortToast
import com.shakib.gsk2022.common.extensions.visible
import com.shakib.gsk2022.databinding.FragmentHomeBinding
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val MAX_SELECTION = "3"
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var rxPermissions: RxPermissions

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
                navigateWithPermission(needCameraPermission = true)
                true
            }
            add(getString(R.string.pick_from_gallery)).setOnMenuItemClickListener {
                navigateWithPermission(needStoragePermission = true)
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

    private fun navigateWithPermission(
        needCameraPermission: Boolean = false,
        needStoragePermission: Boolean = false
    ) {
        rxPermissions = RxPermissions(this)

        if (needCameraPermission) {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe { granted ->
                    if (granted)
                        screenNavigator.toCameraFragment(findNavController(), MAX_SELECTION)
                    else
                        context?.showLongToast(getString(R.string.permission_denied))
                }
        }

        if (needStoragePermission) {
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe { granted ->
                if (granted)
                    screenNavigator.toMediaPickerFragment(findNavController(), MAX_SELECTION)
                else
                    context?.showLongToast(getString(R.string.permission_denied))
            }
        }
    }
}
