package com.shakib.gsk2022.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.shakib.gsk2022.presentation.navigator.DialogNavigator
import com.shakib.gsk2022.presentation.navigator.ScreenNavigator
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    @Inject
    protected lateinit var screenNavigator: ScreenNavigator

    @Inject
    protected lateinit var dialogNavigator: DialogNavigator
    var data: String? = null

    companion object {
        const val DATA = "data"
    }

    protected lateinit var binding: VB

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    open fun getBaseViewModel(): BaseViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        init(savedInstanceState)
    }

    open fun init(savedInstanceState: Bundle?) {
        configureViews(savedInstanceState)
        bindWithViewModel()
    }

    open fun configureViews(savedInstanceState: Bundle?) {}

    open fun bindWithViewModel() {
        getBaseViewModel()?.let { viewModel ->
            viewModel.progress.observe(viewLifecycleOwner) {
                when (it) {
                    is BaseViewModel.Progress.Show -> showProgress()
                    is BaseViewModel.Progress.Hide -> hideProgress()
                    else -> hideProgress()
                }
            }
        }
    }

    private fun showProgress() {
        // TODO - show progress
    }

    private fun hideProgress() {
        findNavController().navigateUp()
    }

    private fun extractArguments() {
        arguments?.let {
            data = it.getString(DATA)
        }
    }

}