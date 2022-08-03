package com.shakib.gsk2022.presentation.screens.picker

import androidx.lifecycle.viewModelScope
import com.shakib.gsk2022.common.base.BaseViewModel
import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.domain.MediaPickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    private val mediaPickerUseCase: MediaPickerUseCase
) : BaseViewModel() {

    lateinit var imageListStateFlow: StateFlow<Resource<List<Image>>>

    fun fetchAllImages() =
        viewModelScope.launch {
            imageListStateFlow = mediaPickerUseCase.fetchAllImages().stateIn(
                scope = this,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading()
            )
        }

    override fun onClear() {}
}