package com.shakib.gsk2022.data.repository

import com.shakib.gsk2022.data.model.Image
import kotlinx.coroutines.flow.Flow

interface MediaPickerRepo {
    suspend fun fetchAllImages(): List<Image>
}
