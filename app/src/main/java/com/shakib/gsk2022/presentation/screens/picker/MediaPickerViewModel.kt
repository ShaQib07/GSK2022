package com.shakib.gsk2022.presentation.screens.picker

import androidx.lifecycle.viewModelScope
import com.shakib.gsk2022.common.base.BaseViewModel
import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.domain.MediaPickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    private val mediaPickerUseCase: MediaPickerUseCase
) : BaseViewModel() {

    val imageListStateFlow = MutableStateFlow<Resource<MutableList<Image>>>(Resource.Loading())

    fun fetchAllImages() =
        viewModelScope.launch { imageListStateFlow.value = mediaPickerUseCase.fetchAllImages() }

    override fun onClear() {}
}