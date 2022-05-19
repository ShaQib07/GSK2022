package com.shakib.gsk2022.presentation.screens.camera

import com.shakib.gsk2022.common.base.BaseViewModel
import com.shakib.gsk2022.domain.DirectoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(directoryUseCase: DirectoryUseCase) :
    BaseViewModel() {

    val outputDirectory = directoryUseCase.getOutputDirectory()

    override fun onClear() {}
}