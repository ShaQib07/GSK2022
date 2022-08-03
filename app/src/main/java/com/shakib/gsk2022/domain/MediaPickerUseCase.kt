package com.shakib.gsk2022.domain

import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.data.repository.MediaPickerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MediaPickerUseCase @Inject constructor(private val mediaPickerRepo: MediaPickerRepo) {

    fun fetchAllImages(): Flow<Resource<List<Image>>> =
        flow {
            val imageListResource: Resource<List<Image>> = try {
                Resource.Success(mediaPickerRepo.fetchAllImages())
            } catch (e: Exception) {
                Resource.Error(e)
            }
            emit(imageListResource)
        }.flowOn(Dispatchers.IO)
}