package com.shakib.gsk2022.presentation.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import com.shakib.gsk2022.common.base.BaseFragment
import com.shakib.gsk2022.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)
}