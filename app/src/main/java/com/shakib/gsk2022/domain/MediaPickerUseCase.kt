package com.shakib.gsk2022.domain

import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.repository.MediaPickerRepo
import com.shakib.gsk2022.data.model.Image
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class MediaPickerUseCase @Inject constructor(private val mediaPickerRepo: MediaPickerRepo) {

    suspend fun fetchAllImages(): Resource<MutableList<Image>> {

        var imageListResource: Resource<MutableList<Image>> = Resource.Loading()

        mediaPickerRepo.fetchAllImages()
            .catch { imageListResource = Resource.Error(it) }
            .collect { imageListResource = Resource.Success(it) }

        return imageListResource
    }
}